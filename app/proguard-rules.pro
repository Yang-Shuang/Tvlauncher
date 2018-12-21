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
#通用配置
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keep class **.R$* { *; }
-keep class **.R{ *; }
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
}

-keep class com.youth.banner.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.orhanobut.** { *;}
-keep class retrofit.** { *; }
-dontwarn retrofit2.Platform$Java8
-keep public class * extends android.support.v4.app.Fragment
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
