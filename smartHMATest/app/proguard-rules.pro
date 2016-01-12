-optimizations !code/simplification/arithmetic
-allowaccessmodification
-dontobfuscate
-printmapping mapping.txt
-useuniqueclassmembernames
-repackageclasses ''
-keepattributes *Annotation*
-dontpreverify
-dontwarn javax.annotation.Nullable,javax.annotation.CheckReturnValue,com.google.api.client.json.gson.GsonFactory,com.google.api.client.json.gson.GsonFactory,com.google.api.client.json.jackson.JacksonFactory,com.google.api.client.json.jackson2.JacksonFactory,com.squareup.okhttp.internal.huc.HttpURLConnectionImpl,com.squareup.okhttp.internal.huc.JavaApiConverter$CacheHttpURLConnection,org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement,java.nio.file.*
-dontwarn com.squareup.okhttp.**
-dontwarn android.support.v4.**
-dontwarn com.google.api.client.http.apache.ApacheHttpRequest,com.google.api.client.http.apache.HttpExtensionMethod
-dontwarn com.octo.android.robospice.SpiceService
-dontwarn com.wunderlist.slidinglayer.SlidingLayer
-dontwarn org.acra.ErrorReporter

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }