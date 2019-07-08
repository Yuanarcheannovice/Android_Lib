# Android_Lib
安卓端 工具 仓库
引入方式
```java
allprojects {
    repositories {
        google()
        jcenter()
        //工具类
        maven { url 'https://raw.githubusercontent.com/Yuanarcheannovice/Android_Lib/master/maven' }
    }
 }
 
 dependencies {
    implementation 'com.archeanx.android:@libName:@version'
  }
```
lib 和 版本号:

![](https://github.com/Yuanarcheannovice/Android_Lib/blob/master/lib_version.xml)
