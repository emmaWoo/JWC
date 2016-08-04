-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontshrink
-dontoptimize
-dontpreverify
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.*
-keep public class com.ideaheap.io.* { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class android.support.** { *; }
-dontwarn android.support.**

-keep class com.google.android.** { *; }
-dontwarn com.google.android.**

-keep class android.net.http.** { *; }
-dontwarn android.net.http.**

-keep class android.sec.multiwindow.** { *; }
-dontwarn android.sec.multiwindow.**

-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

-keep class org.apache.** { *; }
-dontwarn org.apache.**
-keepclassmembers public class org.apache.http.** { *; }

-keep class oauth.signpost.signature.** { *; }
-dontwarn oauth.signpost.signature.**
-keepattributes Signature

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-dontwarn org.apache.http.entity.mime.**
-dontwarn com.google.**
-dontwarn com.testflightapp.**
-dontwarn com.google.zxing.**
-dontwarn org.msgpack.**
-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn com.android.volley.**
-dontwarn net.hockeyapp.android.**
-dontwarn com.squareup.**
-dontwarn com.jakewharton.**
-dontwarn com.joooonho.**
-dontwarn org.apache.httpcomponents.**
-dontwarn com.github.nkzawa.**
-dontwarn com.facebook.**
-dontwarn me.leolin.**
-dontwarn java.lang.invoke.**

-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class org.apache.http.entity.mime.** { *; }
-keep class com.google.** { *; }
-keep class com.testflightapp.** { *; }
-keep class com.google.zxing.** { *; }
-keep class com.squareup.** { *; }
-keep class okio.** { *; }
-keep class com.android.volley.** { *; }
-keep class net.hockeyapp.android.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.squareup.** { *; }
-keep class com.jakewharton.** { *; }
-keep class com.joooonho.** { *; }
-keep class org.apache.httpcomponents.** { *; }
-keep class com.github.nkzawa.** { *; }
-keep class com.facebook.android.** { *; }
-keep class me.leolin.** { *; }