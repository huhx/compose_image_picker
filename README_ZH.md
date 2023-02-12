Language: [English](README.md) | [中文简体](README_ZH.md)

## 📸&nbsp;适用于Jetpack Compose的图片选择库

一个易于使用并支持可配置的类QQ UI的图片选择器，另外也支持视频的选择与录制。


## 🐱&nbsp;功能
- [x] 图片和视频的选取
- [x] 支持拍照并加以选取
- [x] 图片和视频的预览
- [x] 图片或视频根据目录分组展示
- [x] 支持GIF图片的选取与预览
- [x] 支持亮/暗主题
- [x] 支持国际化
- [x] 实现选取图片和视频的权限
- [ ] 未完待续....


## 🎬&nbsp;概览

图片选取    |         分组展示      |       图片预览        |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/15972372/181038075-b268f17b-9799-4a87-9dec-bbd865fe516e.gif)  |  ![](https://user-images.githubusercontent.com/15972372/181038392-d1bf6886-4bba-4a8c-bb14-ea454a0d52ba.gif)  |  ![](https://user-images.githubusercontent.com/15972372/181038444-e54fe454-d158-4b2c-ad7a-95d2e8bfe9a7.gif)

国际化   |         暗黑主题     |        朋友圈例子       |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/15972372/182802765-0e091698-2994-49e6-8a57-1367fb39ef45.gif)  |  ![](https://user-images.githubusercontent.com/15972372/182802666-a82ef410-2a52-4f7d-854f-425e06e1896a.gif)  |  ![](https://user-images.githubusercontent.com/15972372/182802821-a6c0c2d9-f997-4e89-9e6f-64b9297ec92b.gif)


## 💻&nbsp;前置

1. 在build.gradle中添加Gradle的依赖:
```groovy
implementation "io.github.huhx:compose-image-picker:1.0.8"
```

<br>

2. 在AndroidManifest.xml中添加权限:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```


## 🎨&nbsp;使用

1. 创建一个ImagePicker的@Composable方法，并且你可以自定义onPicked和onClose的回调函数
```kotlin
@Composable
fun ImagePicker(
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    PickerPermissions(permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
        AssetPicker(
            assetPickerConfig = AssetPickerConfig(gridCount = 3),
            onPicked = onPicked,
            onClose = onClose
        )
    }
}
```
<br>

2. 将创建的@Composable方法添加到navigation的路由中
```kotlin
composable("asset_picker") {
    ImagePicker(
        onPicked = { assets -> 
            // implement the onPicked logic, pass the assets list that you picked
            viewModel.selectedList.clear()
            viewModel.selectedList.addAll(assets)
            navController.navigateUp()
        },
        onClose = { assets ->
        // implement the onClose logic, pass the assets list that you picked
            viewModel.selectedList.clear()
            navController.navigateUp()
        }
    )
}
```
<br>

3. 调用navController.navigate的方法，触发图片的选取
```kotlin
navController.navigate("asset_picker") 
```
> 注意参数route string应该和第二步的路由string保持一致

<br>

4. AssetPickerConfig类包含了配置项
```kotlin
data class AssetPickerConfig(
    val maxAssets: Int = 9, // 选取图片的最大数量
    val gridCount: Int = 3, // 图片Grid布局中的列数
    val requestType: RequestType = RequestType.COMMON,
)
```
基于上述你可以配置maxAssets和gridCount以满足对不同屏幕的需求
```kotlin
AssetPicker(
    assetPickerConfig = AssetPickerConfig(gridCount = 4, maxAssets = 20),
    onPicked = onPicked,
    onClose = onClose
)
```
> 关于compose-image-picker的详细使用，可以参考代码里面的例子，里面实现了一个简易的朋友圈功能

## Drop a ⭐ if you like it. New features to be continue...


