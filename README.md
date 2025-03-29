<h1 align="center">WriteBuddy</h1></br>

<p align="center">
📝 Write smarter, smoother: A Compose-based handwriting assistant ✍️
<br>
Supports Sticky(PostIt, TextBox, Image), Tool(Laser, Tape)
</p>

<br>

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-orange)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue)](https://kotlinlang.org/docs/reference/multiplatform.html)

![Platform](https://img.shields.io/badge/Android-3aab58)
![Platform](https://img.shields.io/badge/IO(Experimentinal)S-d32408)
![Platform](https://img.shields.io/badge/Desktop(Experimentinal)-097cd5)
![Platform](https://img.shields.io/badge/Web(Experimentinal)-99CC33)
    
</div>

<br>

## Gradle
Add the dependency below to your **module**'s `build.gradle` file:

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.henni99/writebuddy)

```kotlin

commonMain.dependencies {
    implementation("io.github.henni99:writebuddy:<latest-version>")
}

```

<br>

## Sticky Mode

<p align="center">

📌 Post-it | 🔤 TextBox |  🖼️  Image | 
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/user-attachments/assets/3e96f67a-9d6d-4a05-914d-e252134345de" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/d19ca745-cb76-4906-a270-b26d9effaaf7" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/ee91b3bc-9246-4fdd-a8ae-85cbc11e18c4" align="center" width="100%"/> |

<br>


### How to use
Sticky mode is a feature that allows users to place and manage various sticky items on the screen, such as Post-its, images, and text boxes. Users can add sticky items, move them around, adjust their size, and modify their properties.

```kotlin
val controller = rememberStickyItemController(
    painterImageProperty = PainterImagePropertyExample(),
    vectorImageProperty = VectorImagePropertyExample(),
  )

  LaunchedEffect(Unit) { // For BitmapImage
    controller.updateBitmapImageImageBitmap(
      Res.readBytes("drawable/ic_launcher.webp").decodeToImageBitmap(),
    )
  }


StickyNote(
  modifier = Modifier
    .padding(innerPadding)
    .fillMaxSize(),
  controller = controller,
)

```

<br>

## Tool Mode

<p align="center">

🔦 Laser | 🩹 Tape | 
| :---------------: | :---------------: |
| <img src="https://github.com/user-attachments/assets/0e7e3e61-a65f-4fde-be37-1557e508cf1a" align="center" width="100%"/> | <img src="https://github.com/user-attachments/assets/73fb2bea-d2f5-4bde-9774-77a5be8e74b1" align="center" width="100%"/> |

<br>

### How to use
ToolMode provides new functionalities through user touch interactions. Users can enjoy fun note-taking with Laser Mode and Tape Mode.
To use Laser and Tape modes, you need to declare a separate controller for each.

```kotlin
val laserPointerController = rememberLaserPointerController()

val progress = animateLaserAlphaFloatAsState(laserPointerController) {
  laserPointerController.clearLaserPaths()
}

val tapeController = rememberTapeController()

var selectedMode by remember { mutableStateOf(ToolMode.LineLaserMode) }


Box(
  modifier = modifier
    .padding(innerPadding)
    .fillMaxSize()
    .useLaserPointerMode(
      laserPointerController,
      progress,
      selectedMode == ToolMode.LineLaserMode,
    )
    .useTapeMode(tapeController, selectedMode == ToolMode.TapeMode),
)
```


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


