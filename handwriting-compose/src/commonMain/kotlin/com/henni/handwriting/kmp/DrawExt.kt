package com.henni.handwriting.kmp

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.clearCanvas() {
    drawRect(
        color = Color.Transparent,
        blendMode = BlendMode.Clear
    )
}