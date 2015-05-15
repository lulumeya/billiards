# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Dalton/Desktop/adt-bundle-mac-x86_64-20140624/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes Exceptions

-keepnames public class * extends io.realm.RealmObject
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**

-dontwarn rx.internal.**
-dontwarn java.lang.invoke.*
-dontwarn retrofit.**
-keep class retrofit.** { *; }

-keep class **Lambda.** { *; }

-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-dontwarn **CompatHoneycomb
-dontwarn **Api20
-dontwarn **Api20$Builder
-dontwarn **v7.app.ActionBar
-keep class android.support.v4.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.healing.mybeauty.view.** {
    public <init>(**);
}

#Maintain java native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

#To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#Maintain enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}