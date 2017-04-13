#-optimizationpasses 5                                                           # 指定代码的压缩级别
#-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
#-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
#-dontpreverify                                                                  # 混淆时是否做预校验
#-verbose                                                                        # 混淆时是否记录日志
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆，因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}


-keep class com.guang.app.widget.** {  *; }
###############   友盟 Begin  ###############
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.guang.app.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
###############   友盟 End  ###############


###############   butterknife Begin ###############
#-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
#-keep class butterknife.*
#-keepclasseswithmembernames class * { @butterknife.* <methods>; }
#-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewInjector { *; }
#-keepnames class * { @butterknife.InjectView *;}
###############   butterknife End ###############

# ButterKnife 7
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
###############   butterknife End ###############


###############   litepal Begin ###############
-keep class org.litepal.** {
    *;
}
-keep class * extends org.litepal.crud.DataSupport {
    *;
}
###############   litepal End ###############

###############   logutils Begin  ###############
#-keep public class com.apkfuns.logutils.R$*{
#    public static final int *;
#}
###############   logutils End  ###############


###############   BaseRecyclerViewAdapterHelper Begin  ###############
-keep class com.chad.library.adapter.** {
   *;
}
###############   BaseRecyclerViewAdapterHelper End  ###############


###############   Retrofit Begin  ###############
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class io.reactivex.** { *; }
###############   Retrofit End  ###############
-keep class org.lzh.framework.updatepluginlib.** {
   *;
}

##gson
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**
##gson


