# StackBlur
[![](https://img.shields.io/badge/Minimum%20Sdk-21-2196F3)](https://github.com/RohitVermaOP/StackBlur)
[![](https://jitpack.io/v/RohitVermaOP/StackBlur.svg)](https://jitpack.io/#RohitVermaOP/StackBlur)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](./LICENSE)

Simple library which Blur bitmaps native Way

## Screenshot

![](screenshot.jpg)

## Download 

#### Add to project's build.gradle
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

#### Add to module-level build.gradle
```gradle
dependencies { 
    implementation 'com.github.RohitVermaOP:StackBlur:<latest-version>'
}
```

## Usage

#### Add to application or activity (Not requried)
```java
StackBlur.initLib();
```

#### blurBitmap
```java
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
blurImageView.setImageBitmap(StackBlur.blurBitmap(bitmap, 40));
```

#### blurBitmap2 (Support multi thread and faster)
```java
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
blurImageView.setImageBitmap(StackBlur.blurBitmap2(bitmap, 40));
```

## Developer

Developed by ```Rohit Verma```
+ [Instagram](http://instagram.com/mr_rohitverma88)
+ [Telegram](http://t.me/RohitVerma88)

## Special Thanks
+ [Telegram](https://github.com/DrKLO/Telegram)
+ [android-stackblur](https://github.com/kikoso/android-stackblur)
+ [sample_img](https://unsplash.com/photos/LSFuPFE9vKE) from [Unsplash](https://unsplash.com)

## License

[Apache 2.0](./LICENSE)
