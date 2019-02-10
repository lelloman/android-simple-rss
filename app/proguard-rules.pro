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


#-dontobfuscate
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
-optimizationpasses 10
-repackageclasses "a"
-allowaccessmodification
-flattenpackagehierarchy
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-assumenosideeffects interface com.lelloman.common.logger.Logger {
    *** i(...);
    *** d(...);
}

-optimizations code/*,method/*,class/merging/*
#class/marking/final
#class/unboxing/enum
#class/merging/vertical
#class/merging/horizontal (⇒ code/removal/advanced)
#field/removal/writeonly
#field/marking/private (⇒ code/simplification/advanced)
#field/propagation/value
#method/marking/private (⇒ code/removal/advanced)
#method/marking/static
#method/marking/final (⇒ code/removal/advanced)
#method/removal/parameter(⇒ code/simplification/advanced)
#method/propagation/parameter(⇒ code/simplification/advanced)
#method/propagation/returnvalue
#method/inlining/short
#method/inlining/unique
#method/inlining/tailrecursion
#code/merging
#code/simplification/variable
#code/simplification/arithmetic
#code/simplification/cast
#code/simplification/field (⇒ code/removal/simple)
#code/simplification/branch
#code/simplification/string (best used with code/removal/advanced)
#code/simplification/advanced (⇒ code/removal/exception)
#code/removal/advanced (⇒ code/removal/exception)
#code/removal/simple
#code/removal/variable
#code/removal/exception
#code/allocation/variable