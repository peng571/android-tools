-optimizationpasses 5
-mergeinterfacesaggressively
-dontpreverify
-optimizations !code/simplification/arithmetic
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''
-allowaccessmodification
-useuniqueclassmembernames
-keeppackagenames doNotKeepAThing
-ignorewarnings


-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService


#-repackageclasses 'dev.momo.library.connect'
-allowaccessmodification
-useuniqueclassmembernames
-keeppackagenames doNotKeepAThing

-keep public class dev.momo.library.**{
   public protected *;
}


# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
   public static *;
}

-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
