package com.henni.handwriting.kmp.ext

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