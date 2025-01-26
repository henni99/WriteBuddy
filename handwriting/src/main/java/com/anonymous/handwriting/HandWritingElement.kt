package com.anonymous.handwriting

import android.graphics.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path

data class HandWritingElement(
    val path: Path,
    val paint: Paint,
    val pathCoordinates: Set<Rect>,
    val matrix: Matrix = Matrix()
)