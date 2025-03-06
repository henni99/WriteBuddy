<h1 align="center">HandWriting</h1></br>

<p align="center">
📝 Everything about drawing: A Compose-based drawing tool library, built with Kotlin Multiplatform. ✍️
<br>
Supports drawing, erasing, selection and manipulation, undo/redo, and zoom functionality.
</p>

<br>

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-orange)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue)](https://kotlinlang.org/docs/reference/multiplatform.html)

![Platform](https://img.shields.io/badge/Android-3aab58)
![Platform](https://img.shields.io/badge/Desktop-097cd5)
![Platform](https://img.shields.io/badge/IOS-d32408)
    
</div>

<br>

<p align="center">

🖊️ Pen Tool | 🧽 Stroke Eraser Tool | 🪢  Lasso Tool | 🔦  Laser Tool |
| :---------------: | :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/user-attachments/assets/4b02d4c5-a0ec-4e64-abd6-6dda230b1a80" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/e82efeaf-505b-4f97-82ae-f4116ef42037" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/a4d61037-7cff-4a71-bb6f-dc44718e04c3" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/048e71cf-f0b7-405a-89c7-e4a6fda28362" align="center" width="100%"/> |

</p>

<br>

## Gradle
Add the dependency below to your **module**'s `build.gradle` file:

```kotlin
dependencies {
    implementation("io.github.henni99:handwriting:1.0.3")
}
```

<br>

## How to use
**HandwritingController** for managing handwriting data, such as pen strokes, eraser actions, and lasso selection. It supports undo/redo functionality, tool modes, and manages the state for various elements like strokes, selected paths, and the lasso area.

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

You can also use the 🔍 zoom feature!

<img src="https://github.com/user-attachments/assets/2245a5c8-14cb-4cef-94cf-e4726f387ee3" align="center" width="250"/> 

<br>
<br>

## Next Step !!
- Implement pencil effect
- Provide various functionalities for the selected object (zoom in, zoom out, rotate, copy)

<br>

## Issue & Enhancement & Contributions

If you find any bugs or have suggestions for better features, please feel free to share your feedback. Contributions are always welcome!

<br>

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/henni99/Handwriting/stargazers)__ for this repository. :star: <br>
Also, __[follow me](https://github.com/henni99)__ on GitHub for my next creations! 🤩

<br>

## Document

For more details, you can check out the [documentations](https://henni99.github.io/Handwriting/index.html)


