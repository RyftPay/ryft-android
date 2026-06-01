# Migration Guide

## v1.x → v2.x

v2.0.0 replaces the Checkout.com 3DS SDK with the Ravelin 3DS SDK. The 3DS flow is now
two-stage (identify → challenge) rather than one-stage, and the SDK uses coroutine-based
suspend functions instead of callbacks.

**Minimum SDK version bumped: 23 → 24 (Android 7.0)**

---

### 1. Update Gradle dependencies

**Root `build.gradle` — add the Ravelin Maven repository:**

```groovy
allprojects {
    repositories {
        // ...existing repositories...
        maven { url 'https://maven.ravelin.com/public/repositories/threeds2service/' }
    }
}
```

**App / `ryft-ui` `build.gradle` — swap the 3DS SDK and enable desugaring:**

```groovy
android {
    // ...
    compileOptions {
        coreLibraryDesugaringEnabled true
        // ...
    }
}

dependencies {
    // Remove:
    // implementation 'com.checkout:checkout-sdk-3ds-android:3.3.6'

    // Add:
    implementation 'com.ravelin.threeds2service:threeds2service-sdk:2.0.2'
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.1.5'
}
```

---

### 2. Handle the new `Challenge` required action type

`RequiredActionType` has a new `Challenge` value. Any exhaustive `when` expression over this
enum will fail to compile until it is handled:

```kotlin
// v1.x
enum class RequiredActionType { Redirect, Identify, Unknown }

// v2.x
enum class RequiredActionType { Redirect, Identify, Challenge, Unknown }
```

Update any `when` expressions accordingly:

```kotlin
when (requiredAction.type) {
    RequiredActionType.Redirect  -> { /* unchanged */ }
    RequiredActionType.Identify  -> { /* unchanged */ }
    RequiredActionType.Challenge -> handleChallenge(requiredAction.challenge!!)
    RequiredActionType.Unknown   -> { /* unchanged */ }
}
```

`RequiredAction` now carries an additional nullable field:

```kotlin
// v2.x
data class RequiredAction(
    val type: RequiredActionType,
    val url: String?,
    val identify: IdentifyAction?,
    val challenge: ChallengeAction?   // new
)
```

---

### 3. Update `IdentifyAction` field usage

`IdentifyAction` no longer contains Checkout.com session credentials. It now carries
Ravelin-specific fields:

```kotlin
// v1.x
data class IdentifyAction(
    val sessionId: String,
    val sessionSecret: String,
    val scheme: String,
    val paymentMethodId: String
)

// v2.x
data class IdentifyAction(
    val scheme: String,
    val paymentMethodId: String,
    val protocolVersion: String,
    val ravelinPublicKey: String
)
```

Remove any references to `sessionId` and `sessionSecret`. The `ravelinPublicKey` is passed
directly to the SDK — you do not need to handle it explicitly if you are using
`RyftPaymentFragment`.

---

### 4. Implement the `onPaymentRequiresChallenge` callback

`RyftPaymentResultListener` has a new callback for the challenge step. If you have a custom
implementation of this listener you must add it:

```kotlin
override fun onPaymentRequiresChallenge(challengeAction: ChallengeAction) {
    // Handle the 3DS challenge — see section 5 if building a custom integration.
    // RyftPaymentFragment handles this automatically.
}
```

---

### 5. Custom integrations: update the two-stage 3DS flow

> **If you use `RyftPaymentFragment` drop-in UI, no changes are required beyond sections 1–4.**

If you drive the 3DS flow manually (e.g. via `RyftRequiredActionActivity` or a custom
activity), the flow has changed from one stage to two:

**v1.x — single-stage identification via callback:**

```kotlin
// Create service and start identification
val threeDsService = DefaultCheckoutThreeDsService(
    Checkout3dsServiceFactory.create(context, environment, returnUrl)
)
threeDsService.handleIdentification(identifyAction, listener = this)

// Callback fires when done
override fun onThreeDsIdentificationResult(result, paymentMethodId) {
    ryftPaymentService.attemptPayment(PaymentMethod.id(paymentMethodId), ...)
}
```

**v2.x — two-stage flow using coroutines:**

```kotlin
// Stage 1: Identification — send transaction params to server
override fun onPaymentRequiresIdentification(returnUrl: String, identifyAction: IdentifyAction) {
    lifecycleScope.launch {
        val ravelinService = RavelinThreeDsServiceFactory.create(
            context = applicationContext,
            ryftEnvironment = environment,
            ravelinPublicKey = identifyAction.ravelinPublicKey,
            coroutineScope = lifecycleScope
        ).also { threeDsService = it }

        val transactionParams = ravelinService.createTransaction(identifyAction)

        ryftPaymentService.continuePayment(
            clientSecret = clientSecret,
            subAccountId = subAccountId,
            threeDsTransactionParams = transactionParams,
            listener = this@YourActivity
        )
    }
}

// Stage 2: Challenge — triggered by server response if further verification needed
override fun onPaymentRequiresChallenge(challengeAction: ChallengeAction) {
    lifecycleScope.launch {
        when (val result = threeDsService?.doChallenge(this@YourActivity, challengeAction)) {
            is ThreeDsChallengeResult.Completed -> {
                ryftPaymentService.continuePaymentAfterChallenge(
                    clientSecret = clientSecret,
                    subAccountId = subAccountId,
                    transactionStatus = result.transactionStatus,
                    threeDSServerTransactionId = result.threeDSServerTransactionId,
                    listener = this@YourActivity
                )
            }
            is ThreeDsChallengeResult.Cancelled -> { /* handle cancellation */ }
            is ThreeDsChallengeResult.Failed    -> { /* handle failure */ }
            null -> { /* handle missing service */ }
        }
    }
}

// Clean up the Ravelin SDK when the activity is destroyed
override fun onDestroy() {
    super.onDestroy()
    threeDsService?.cleanup()
    threeDsService = null
}
```

**Removed types — delete any direct references:**

| Removed (v1.x) | Replaced by (v2.x) |
|---|---|
| `Checkout3dsServiceFactory` | `RavelinThreeDsServiceFactory` |
| `DefaultCheckoutThreeDsService` | `DefaultRavelinThreeDsService` (internal) |
| `ThreeDsIdentificationResult` | `ThreeDsChallengeResult` |
| `ThreeDsIdentificationResultListener` | Coroutine return values from `doChallenge()` |

---