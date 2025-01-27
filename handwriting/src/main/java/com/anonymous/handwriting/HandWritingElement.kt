package com.anonymous.handwriting

import android.graphics.Rect
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import java.util.UUID

@Immutable
data class HandWritingElement(
    val elementId: String = UUID.randomUUID().toString(),
    val path: Path,
    val paint: Paint,
    val pathCoordinates: Set<Rect>,
    val matrix: Matrix = Matrix()
) {
//    fun drawToCanvas(canvas: Canvas, matrix:Matrix) {
//        canvas.save()
//        canvas.concat(matrix)
//
//        canvas.drawPath(path, paint)
//
//        canvas.restore()
//    }
}