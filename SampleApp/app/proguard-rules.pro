# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pg/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-dontshrink
#-dontoptimize
#-dontpreverify
#-verbose
#-keep public class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

-keepattributes *Annotation*
#-keep @**annotation** class ** {*;}
-keep class org.apache.http.* {*;}
-dontwarn org.apache.http.**
-keep class org.apache.harmony.** {*;}
-dontwarn org.apache.harmony.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class com.sun.mail.imap.protocol.** {*;}
-dontwarn com.sun.mail.imap.protocol.**
-keep class okio.** {*;}
-dontwarn okio.**
-keep class com.squareup.okhttp.** {*;}
-dontwarn com.squareup.okhttp.**
-keep class javax.activation.** {*;}
-dontwarn javax.activation.**
-dontwarn org.json.**
-keep public class * extends android.app.Application
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class javax.** { *; }
-keep class org.** { *; }
-keepclassmembers class org.jio.sdk.common.customview.ProgressAnimDialog** {*;}
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class androidx.lifecycle.*
-keep class android.view.View.OnClickListener
-keep class io.agora.**{*;}



