> ## 简介：本项目选择的方向是**TODO**App
>
> 源代码整体由Kotlin语言进行开发
>
> 基本实现了本地存储的功能（不过过于简陋，只有一个简单的room映射的SQLite数据库存放task数据，SharedPreferences 存放注册和登录数据存储，且只写了单例存储，即注册会覆盖前一次的注册数据）
>
> - 实现了本地登陆注册功能
> - 能够显示已创建任务
> - 目前只能实现创建任务，还未来得及开发剩下的删改查功能
> - 未实现任务的侧滑删除和置顶功能（开始是想用自定义view完成，结果我太菜了，学不会，准备用Androidx库的DrawerLayout来实现，但是时间不够，只写好了xml文件里的大致布局，列表中的滑动动画的回调已完成，但置顶和删除的逻辑未实现，只应用到了childtask的item布局文件）
> - 使用RecyclerView实现了仿折叠列表的功能（目前的效果可能展现不太出来，毕竟没有实现输入数据task的分级展示（数据库那边的接口还没实现））
> - 在登录注册界面使用了databinding+ViewModel来完成输入的检测和数据的放置（利用双向绑定，简化Fragment中对输入框的输入判定和checkBox的状态判定，把对行为的描述转变未对数据的描述），不过我留了一点”答辩“在输入框的内置动态效果的开关那里，那里的逻辑我们没有用viewmodel的数据进行判定
> - 学习了ViewBinding和DataBinding的简单封装，并实现拿了两个简单的fragment 对ViewBinding和DataBinding的封装

下面是本项目的第三方依赖

```groovy
dependencies {

    def lifecycle_version = "2.5.1"
    def room_version = "2.5.0"
    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.10'

    // Room necessary
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")


    // Room optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // Room optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")
    // Room optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    //RxJava
    implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
    //RxAndroid
    implementation 'io.reactivex.rxjava3:rxjava:3.0.0'

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
```

还有VB和DB

```groovy
viewBinding {
    enabled = true
}

buildFeatures {
    dataBinding true
}
```

### 本项目的缺点：

1. ##### bug太多……写起来完全是在debug

2. ##### 实现的功能太少，UI界面太素了，项目结构不太清晰

3. ##### 在实现登录界面的时，偶尔莫名会发生bug，导致存放注册和登录数据的SharedPreferences会直接崩掉导致内存泄漏？（小黄鸟儿给的提示，猜测可能是AndroidViewModel的初始化格式错了，不小心把activity的实例传进去了？） 

4. ##### 项目期间由于Room版本问题（还有以前builde.gradle文件不规范留下的一些问题），换了一个版本新一点的AS并更新了几次gradle版本，可能项目源码的兼容性不太好，目前gradle版本8.0.1(比较低的版本甚至都构建不了，7.2.2版本和7.3版本运行起来也是问题一大堆)





##### 更新，目前经过抢修与部分代码重构，（在本人的实体机Android 11 上还是闪退，但报错都是些和系统层面的支持问题了下main是日志中的error信息）

2023-05-04 01:30:08.249 6104-6127/? E/IHansComunication: HANS printMessageBody: RCVed message body: type = 0, port = 86870, caller_pid = 1477, caller_uid = 1000, target_pid = 13846, target_uid = 10264, pkg_cmd = -1
2023-05-04 01:30:08.250 2481-2481/? E/Launcher.ColorBubbleTextView: mForceHideDot is false
2023-05-04 01:30:08.251 2481-2481/? E/Launcher.ColorBubbleTextView: mForceHideDot is false
2023-05-04 01:30:08.325 6819-6845/? E/Parcel: Reading a NULL string not supported here.
2023-05-04 01:30:08.419 4052-4224/? E/PASe8f28c6220715.PolicyManager: acceptHandleResult, hanleResult is null
2023-05-04 01:30:08.592 838-891/? E/UxUtility: notifyAppState error = NULL
2023-05-04 01:30:08.649 3235-3235/? E/oiface: packageSwitch pkg com.oppo.launcher, enter
2023-05-04 01:30:08.650 3235-3235/? E/oiface: current package is com.oppo.launcher and pid is 2481
2023-05-04 01:30:08.681 3235-3246/? E/oiface: Average freq 1462370 for 0s.
2023-05-04 01:30:08.682 3235-3246/? E/oiface: Average freq 1356329 for 0s.
2023-05-04 01:30:08.682 3235-3246/? E/oiface: Average freq 1607153 for 0s.
2023-05-04 01:30:08.683 1477-2878/? E/ORMS_Platform : OplusResourceManagerPlatformUtils: readFile function exception:/sys/devices/platform/soc/5000000.qcom,kgsl-3d0/kgsl/kgsl-3d0/freq_table_mhz: open failed: ENOENT (No such file or directory)
2023-05-04 01:30:08.683 3235-3246/? E/oiface: get gpu freq table failed
2023-05-04 01:30:08.693 3235-3246/? E/oiface: client  not found, skipped
2023-05-04 01:30:08.728 3235-3246/? E/oiface: get gpu freq table failed
2023-05-04 01:30:08.728 1477-2878/? E/ORMS_Platform : OplusResourceManagerPlatformUtils: readFile function exception:/sys/devices/platform/soc/5000000.qcom,kgsl-3d0/kgsl/kgsl-3d0/freq_table_mhz: open failed: ENOENT (No such file or directory)
2023-05-04 01:30:09.149 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = 2404:6800:4012:1::200a, port 443
2023-05-04 01:30:10.177 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = 2404:6800:4012:2::200a, port 443
2023-05-04 01:30:11.201 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = 2404:6800:4012:3::200a, port 443
2023-05-04 01:30:12.225 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = 2404:6800:4012:4::200a, port 443
2023-05-04 01:30:13.003 21667-21667/? E/xample.todolis: Unknown bits set in runtime_flags: 0x40000000
2023-05-04 01:30:13.027 21667-21667/? E/RefClass: java.lang.reflect.InvocationTargetException
2023-05-04 01:30:13.244 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = ::ffff:172.217.163.42, port 443
2023-05-04 01:30:14.268 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = ::ffff:142.251.42.234, port 443
2023-05-04 01:30:15.293 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = ::ffff:172.217.160.74, port 443
2023-05-04 01:30:16.321 779-1102/? E/libnetd_resolv: FwmarkServer connect uid = 10116 addr = ::ffff:142.251.43.10, port 443
2023-05-04 01:30:17.278 819-1126/? E/hwcomposer: [DRMDEV] failed to drmWaitVBlank with dpy_0: -1  
2023-05-04 01:30:19.572 819-1126/? E/hwcomposer: [DRMDEV] failed to drmWaitVBlank with dpy_0: -1  
2023-05-04 01:30:23.491 819-1126/? E/hwcomposer: [DRMDEV] failed to drmWaitVBlank with dpy_0: -1  
2023-05-04 01:30:26.649 819-1126/? E/hwcomposer: [DRMDEV] failed to drmWaitVBlank with dpy_0: -1  
2023-05-04 01:30:26.650 819-1126/? E/hwcomposer: [DRMDEV] failed to drmWaitVBlank with dpy_0: -1  

而且很神奇的是我同学的Android 11和 虚拟机就没问题……