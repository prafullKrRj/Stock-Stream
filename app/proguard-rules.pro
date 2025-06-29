# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# Keep your DTOs/data classes
-keep class com.prafullkumar.stockstream.data.remote.dtos.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items)
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not kept
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.material3.** { *; }

# Compose Navigation
-keep class androidx.navigation.compose.** { *; }
-keep class androidx.hilt.navigation.compose.** { *; }

# Keep your composable functions
-keep class com.prafullkumar.stockstream.ui.** { *; }
-keep class com.prafullkumar.stockstream.navigation.** { *; }

# Keep composable function signatures
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}


# Compose stability
-keepattributes RuntimeVisibleAnnotations
-keep class androidx.compose.runtime.Stable
-keep class androidx.compose.runtime.Immutable

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.prafullkumar.stockstream.**$$serializer { *; }
-keepclassmembers class com.prafullkumar.stockstream.** {
    *** Companion;
}
-keepclasseswithmembers class com.prafullkumar.stockstream.** {
    kotlinx.serialization.KSerializer serializer(...);
}