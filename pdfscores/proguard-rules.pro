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


# Disable all Notes
-dontnote **

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes EnclosingMethod

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable, InnerClasses

-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn org.conscrypt.**
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keep @com.squareup.moshi.JsonQualifier interface *
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }

# remote service aidl
-keep class com.lelloman.pdfscores.IPublicPdfScoresService$** {
    public <fields>;
    public <methods>;
}

-keep class com.lelloman.pdfscores.IPublicPdfScoresService$Stub.** {
    public <fields>;
    public <methods>;
}

-keep interface com.lelloman.pdfscores.IPublicPdfScoresService$** {*;}