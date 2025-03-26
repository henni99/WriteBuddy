<h1 align="center">HandWriting</h1></br>

<p align="center">
📝 Everything about drawing: A Compose-based drawing tool library, built with Kotlin Multiplatform. ✍️
<br>
Supports drawing, erasing, selection and manipulation, laser undo/redo, and zoom functionality.
</p>

<br>

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-orange)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue)](https://kotlinlang.org/docs/reference/multiplatform.html)

![Platform](https://img.shields.io/badge/Android-3aab58)
![Platform](https://img.shields.io/badge/IOS-d32408)
![Platform](https://img.shields.io/badge/Desktop(Experimentinal)-097cd5)
![Platform](https://img.shields.io/badge/Web(Experimentinal)-99CC33)
    
</div>

<br>

<p align="center">

🖊️ Pen Tool | 🧽 Stroke Eraser Tool | 🪢  Lasso Tool | 🔦  Laser Tool |
| :---------------: | :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/user-attachments/assets/c479a0eb-3369-47dc-a099-5568fa63bce2" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/7892d2df-babb-49d8-a0ea-501c655b0516" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/4987ae28-e910-4935-b521-58b98b49810c" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/888210c6-834e-42a4-9470-99b989f1ee6a" align="center" width="100%"/> |

<br>
<br>

<div align="center">
    
↩️ Undo | ↪️ Redo |
| :---------------: | :---------------: |
| <img src="https://github.com/user-attachments/assets/955e8c06-c549-4ec9-bb75-deb66ba31ac3" align="center" width="250"/> | <img src="https://github.com/user-attachments/assets/ad126df7-f579-4b57-8491-8c22a2c9d909" align="center" width="250"/> |
</div>


</p>

<br>

## Gradle
Add the dependency below to your **module**'s `build.gradle` file:

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.henni99/handwriting)

```kotlin

commonMain.dependencies {
    implementation("io.github.henni99:handwriting:<latest-version>")
}

```

<br>

## How to use
**HandwritingController** for managing handwriting data, such as pen strokes, eraser actions, and lasso selection. It supports undo/redo functionality, tool modes, and manages the state for various elements like strokes, selected paths, and the lasso area.

```kotlin
val controller = rememberHandwritingController(
    isZoomable = true,
    isEraserPointShowed = true,
    penColor = Color.Black,
    penStrokeWidth = 14f,
    eraserPointColor = Color.LightGray,
    eraserPointRadius = 20f,
    lassoColor = Color.Black,
    lassoBoundBoxColor = Color.Black,
    lassoBoundBoxPadding = Padding(20, 20, 20, 20),
    laserColor = Color.Black,
    contentBackgroundColor = Color.Transparent,
    contentBackgroundImage = null,
    contentBackgroundImageColor = Color.Transparent,
    contentBackgroundImageContentScale = ContentScale.None
)

// For the laser function, it is optional
val laserState = animateLaserAlphaFloatAsState(controller)


HandWritingNote(
    modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    controller = controller,
    laserState = laserState,
    containerBackgroundColor = Color.LightGray,
    contentWidthRatio = 0.9f,
    contentHeightRatio = 0.9f,
)

```

You can also use the 🔍 zoom feature!

<img src="https://github.com/user-attachments/assets/2245a5c8-14cb-4cef-94cf-e4726f387ee3" align="center" width="250"/> 

<br>
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


