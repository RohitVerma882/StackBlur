# StackBlur
[![](https://jitpack.io/v/RohitVermaOP/StackBlur.svg)](https://jitpack.io/#RohitVermaOP/StackBlur)

Simple library which Blur bitmaps native Way

### Screenshot

![](screenshot.jpg)

### Dowload 

Add to project's build.gradle
```
maven { url 'https://jitpack.io' }
```

Add to module-level build.gradle
```
implementation 'com.github.RohitVermaOP:StackBlur:<latest-version>'
```

### Usage

Method 1
```java
// StackBlur#blurBitmap
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
blurImageView.setImageBitmap(StackBlur.blurBitmap(bitmap, 40));
```
OR

Method 2
```java
// StackBlur#blurBitmap2 method suports multi thered and faster
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
blurImageView.setImageBitmap(StackBlur.blurBitmap2(bitmap, 40));
```

## Developer

Developed by ```Rohit Verma```
+ [Instagram](http://instagram.com/mr_rohitverma88)
+ [Telegram](http://t.me/RohitVerma88)
