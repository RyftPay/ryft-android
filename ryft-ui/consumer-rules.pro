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
