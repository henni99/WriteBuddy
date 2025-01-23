package com.anonymous.handwriting

import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path

data class HandWritingElement(
    val path: Path,
    val paint: Paint,
    val matrix: Matrix = Matrix()
)