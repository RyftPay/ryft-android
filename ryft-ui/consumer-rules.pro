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

# Ravelin 3DS SDK ships ~630 pre-obfuscated classes in Windows device-name top-level packages
# (AuX, Con, NUL, Prn, COM1..9, LPT1..9 case variants). Its own consumer rules do not keep them,
# and R8 leaves the challenge screen unable to submit or cancel.
# The package list matches threeds2service-sdk 2.0.2 and must be rechecked whenever that dependency changes.
-keep class AuX.**,Con.**,cOn.**,con.**,NUL.**,Nul.**,nuL.**,Prn.**,prN.** { *; }
-keep class COM?.**,COm?.**,CoM?.**,Com?.**,cOM?.**,cOm?.**,coM?.**,com?.** { *; }
-keep class LPT?.**,LPt?.**,LpT?.**,Lpt?.**,lPT?.**,lPt?.**,lpT?.**,lpt?.** { *; }
