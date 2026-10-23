# Ryft API and model classes are (de)serialised by Jackson via reflection
-keep class com.ryftpay.android.core.api.** { *; }
-keep class com.ryftpay.android.core.model.** { *; }

# Jackson
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keep class com.fasterxml.jackson.annotation.** { *; }
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.* *;
}
-dontwarn com.fasterxml.jackson.databind.**

# Ravelin 3DS SDK depends on WorkManager / Room
-keep class * extends androidx.room.RoomDatabase { <init>(); }

# Ravelin 3DS SDK ships ~630 pre-obfuscated classes in 3- and 4-letter top-level packages
# (AuX, COM3, LPT5, NUL, prN, ...). Its own consumer rules do not keep them, and R8 leaves the
# challenge screen unable to submit or cancel. Real roots (com, org, net) are excluded.
-keep class !com.**,!org.**,!net.**,???.** { *; }
-keep class !com.**,!org.**,!net.**,????.** { *; }
