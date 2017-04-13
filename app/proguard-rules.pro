# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhengtongyu/Development/Kit/Android/SDK/tools/proguard/proguard-android.txt
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
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keep  class com.squareup.picasso.*{ *; }
-dontwarn rx.internal.util.**
-keep class rx.internal.util.*{ *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

#support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告

#保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
#保持Serializable实现类不被混淆
-keepnames class * implements java.io.Serializable
#保持Serializable不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举enum类不被混淆
-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}
#自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

###-----------第三方jar包library混淆配置------------
#ormlite混淆配置
#-libraryjars libs/ormlite-android-5.0.jar
#-libraryjars libs/ormlite-core-5.0.jar
#-dontwarn com.j256.ormlite.**
#-keep class com.j256.ormlite.** { *;}
#-keep class com.envy15.cherry.base.orm.** { *;}
#json-lib混淆配置
#-libraryjars libs/json-lib-2.4-jdk15.jar
#-dontwarn net.sf.json.**
#-keep class net.sf.json.** { *;}
#json-lib关联包
#-libraryjars libs/commons-beanutils-1.8.3.jar
-dontwarn org.apache.commons.**
-keep class org.apache.commons.** { *;}
#universalimageloader图片加载框架不混淆
#-keep class com.nostra13.universalimageloader.** { *; }
#-dontwarn com.nostra13.universalimageloader.**
#Gson相关的不混淆配置
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keep class com.envy15.cherry.fragment.crossover.model.** { *; }
-dontwarn com.envy15.cherry.fragment.crossover.model.**
-keep class com.envy15.cherry.fragment.discover.model.** { *; }
-dontwarn com.envy15.cherry.fragment.discover.model.**
-keep class com.envy15.cherry.fragment.local.model.** { *; }
-dontwarn com.envy15.cherry.fragment.local.model.**
-keep class com.envy15.cherry.fragment.setting.model.** { *; }
-dontwarn com.envy15.cherry.fragment.setting.model.**
#prt-lib下拉刷新框架不混淆
#-keep class in.srain.cube.views.ptr.** { *; }
#-dontwarn in.srain.cube.views.ptr.**
#PullToRefreshLibrary下拉刷新框架不混淆
#-keep class com.handmark.pulltorefresh.library.** { *; }
#-dontwarn com.handmark.pulltorefresh.library.**

#-dontoptimize
#-dontpreverify
#
#-dontwarn cn.jpush.**
#-keep class cn.jpush.** { *; }
#
#-dontwarn cn.jiguang.**
#-keep class cn.jiguang.** { *; }
#
##==================gson && protobuf==========================
#-dontwarn com.google.**
#-keep class com.google.gson.** {*;}
#-keep class com.google.protobuf.** {*;}


#-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
#-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
#-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
#-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
#-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
#-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
#-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆

#-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
#    native <methods>;
#}
#
#-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
#}
#
#-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
#   public void *(android.view.View);
#}
#
#-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
#  public static final android.os.Parcelable$Creator *;
#}

#-keep class MyClass;