# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/etiennelawlor/Downloads/adt-bundle-mac-x86_64-20130729/sdk/tools/proguard/proguard-android.txt
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
# myproject for OrmLite
-keep class com.okitoki.okcart.database.** { *; }
-keep class com.okitoki.okcart.network.model.** { *; }

-keep class com.okitoki.okcart.ui.ViewHolder.** { *; }
-keepclassmembers class com.okitoki.okcart.holiday.** {
  *;
}

-keep class com.firebase.** { *; }
-dontwarn com.firebase.**
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Daummap
-dontwarn android.opengl.alt.**
-dontwarn net.daum.mf.map.common.**
-dontwarn net.daum.mf.map.n.api.**

-keep class net.daum.mf.map.** {*;}
-keep class net.daum.mf.map.api.** {*;}
-keep class net.daum.android.map.** {*;}

## lobmbok
-dontwarn javax.**
-dontwarn lombok.**
-dontwarn org.apache.**
-dontwarn com.sun.**

# google
-dontobfuscate
-dontwarn com.google.android.**
-dontwarn org.hamcrest.**

-keep class com.google.android.** {
   *;
}
-keep class com.google.common.** {
   *;
}
-keep class org.hamcrest.** {
   *;
}

# Ensure annotations are kept for runtime use.
-keepattributes *Annotation*
# Don't remove any GreenRobot classes
-keep class de.greenrobot.** {*;}
# Don't remove any methods that have the @Subscribe annotation
-keepclassmembers class ** {
    @de.greenrobot.event.Subscribe <methods>;
}
-keepclassmembers class ** {
	public void onEvent*(**);
}
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.**
#-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#FAM
-keep class com.flask.floatingactionmenu.** { *; }

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.
# Gson specific classes
-dontwarn sun.misc.Unsafe
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
# -keep class mypersonalclass.data.model.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keepattributes InnerClasses,EnclosingMethod
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-libraryjars libs
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}
-dontwarn com.androidquery.**
-dontwarn com.daimajia.**

-dontwarn com.facebook.android.BuildConfig
-keep class com.facebook.** { *; }

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class android.webkit.WebViewClient
-keep class * extends android.webkit.WebViewClient
-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}

-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
-keep class com.google.api.** { *; }

# Needed by google-http-client-android when linking against an older platform version
-dontwarn com.google.api.client.extensions.android.**
# Needed by google-api-client-android when linking against an older platform version
-dontwarn com.google.api.client.googleapis.extensions.android.**
# Needed by google-play-services when linking against an older platform version
-dontwarn com.google.android.gms.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# OrmLite uses reflection
-keepattributes *DatabaseField*
-keepattributes *DatabaseTable*
-keepattributes *SerializedName*
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-dontwarn com.bea.xml.stream.**
-dontwarn org.simpleframework.xml.stream.**
-keep class org.simpleframework.xml.**{ *; }
-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}

-keep class com.google.ads.** # Don't proguard AdMob classes
-dontwarn com.google.ads.** # Temporary workaround for v6.2.1. It gives a warnin

-dontwarn org.springframework.**

-dontwarn javax.xml.stream.events.**

-dontwarn  jp.wasabeef.recyclerview.**
#wasabeef recyclerview
-keep class jp.wasabeef.recyclerview.** { *; }

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
