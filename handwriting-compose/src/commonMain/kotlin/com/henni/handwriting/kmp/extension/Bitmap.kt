package com.henni.handwriting.kmp.extension

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.unit.IntSize


internal inline fun getBitmap(size: IntSize): ImageBitmap {
    return ImageBitmap(
        size.width,
        size.height,
        ImageBitmapConfig.Argb8888
    )
}