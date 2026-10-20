# 保留 kotlinx.serialization 生成器
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.karios.xiuxian.** {
    *** Companion;
}
-keepclasseswithmembers class com.karios.xiuxian.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.karios.xiuxian.model.**$$serializer { *; }
-keepclassmembers class com.karios.xiuxian.model.** {
    *** Companion;
}
