package com.henni.handwriting.kmp

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.unit.IntSize


inline fun getBitmap(size: IntSize): ImageBitmap {
    return ImageBitmap(
        size.width,
        size.height,
        ImageBitmapConfig.Argb8888
    )
}