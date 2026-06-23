# Add project specific ProGuard rules here.

# Keep data classes for Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable annotated classes
-keep,includedescriptorclasses class com.modernapp.app.**$$serializer { *; }
-keepclassmembers class com.modernapp.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.modernapp.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# Hilt
-dontwarn com.google.dagger.**
-keep class com.google.dagger.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Coil
-dontwarn coil.**
