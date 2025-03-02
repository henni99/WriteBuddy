<h1 align="center">HandWriting</h1></br>

<p align="center">
📝 Everything about drawing: A Compose-based drawing tool library, built with Kotlin Multiplatform. ✍️
</p>

<p align="center">
</p>

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:

```kotlin
dependencies {
    implementation("io.github.henni99:handwriting:1.0.0")
}
```

### How to use


```kotlin
val controller = rememberHandwritingController {
        isZoomable = true
        isEraserPointShowed = true
        eraserPointRadius = 20f
        lassoBoundBoxPadding = Padding(20, 20, 20, 20)
    }


HandWritingNote(
    modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
    controller = controller,
    contentWidth = 300.dp,
    contentHeight = 300.dp
)

```

### Document

For more details, you can check out the [documentations](https://henni99.github.io/Handwriting/index.html)


