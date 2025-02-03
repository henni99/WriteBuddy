package com.anonymous.handwriting

import android.graphics.Rect
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import java.util.UUID

@Immutable
data class HandWritingElement(
    val elementId: String = UUID.randomUUID().toString(),
    val path: Path,
    val originalPoints: List<Point> = emptyList(),
    val paint: Paint,
    val matrix: Matrix = Matrix()
) {
    fun getTransformPoints(): List<Point> {
        val translateX = matrix.values[12]
        val translateY = matrix.values[13]
        return originalPoints.map {
            Point(
                x = it.x + translateX.toInt(),
                y = it.y + translateY.toInt()
            )
        }
    }

//    fun drawToCanvas(canvas: Canvas, matrix:Matrix) {
//        canvas.save()
//        canvas.concat(matrix)
//
//        canvas.drawPath(path, paint)
//
//        canvas.restore()
//    }
}

data class Point(
    val x: Int,
    val y: Int
)