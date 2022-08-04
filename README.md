## 📸&nbsp;Image and Video Picker Library for Jetpack Compose

Easy to use and configurable Compose library to Pick an image or video from the Gallery.


## 🐱&nbsp;Features:
- [x] Pick Image or Video from device, It is configurable
- [x] Capture Camera Image
- [x] Preview Image and Video
- [x] Play and control the Video
- [x] Directory is group by name
- [x] Pick gif image and preview
- [x] Dart and Light Theme
- [x] Internationalization support
- [x] Implememt the permission to pick images
- [ ] To be continue....


## 🎬&nbsp;Preview

Image Picker    |         Directory Selector      |       Image Preview        |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/15972372/181038075-b268f17b-9799-4a87-9dec-bbd865fe516e.gif)  |  ![](https://user-images.githubusercontent.com/15972372/181038392-d1bf6886-4bba-4a8c-bb14-ea454a0d52ba.gif)  |  ![](https://user-images.githubusercontent.com/15972372/181038444-e54fe454-d158-4b2c-ad7a-95d2e8bfe9a7.gif)


## 💻&nbsp;Preparation

1. Gradle dependency:
```groovy
implementation "io.github.huhx:compose-image-picker:1.0.2-alpha"
```

</br>

2. Add permission in AndroidManifest.xml file:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```


## 🎨&nbsp;Usage

1. Create the picker Composable that you can implement the callback onClose and onClose
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
</br>

2. Put the Composable you created in navigation routes
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
</br>

3. Trigger the image picker by navigate to the specified route
```kotlin
navController.navigate("asset_picker") 
```
> route name("asset_picker") should be the same as name in the step two
</br>

4. You can customize the properties in AssetPickerConfig
```kotlin
data class AssetPickerConfig(
    val maxAssets: Int = 9, // the maximum count you picked
    val gridCount: Int = 3, // the column counts of LazyVerticalGrid that layout the images
    val requestType: RequestType = RequestType.COMMON,
)
```
> The usage of detail, you can refer to the example. 

## Drop a ⭐ if you like it. New fetures to be continue...


