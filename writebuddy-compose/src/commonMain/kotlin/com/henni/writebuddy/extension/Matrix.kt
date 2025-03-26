package com.henni.writebuddy.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Creates a rotation transformation matrix based on two given points.
 *
 * @param firstPoint The first reference point.
 * @param lastPoint The second point used to determine the rotation angle.
 * @return A transformation matrix with applied rotation and translation based on the given points.
 */

fun rotateMatrix(firstPoint: Offset, lastPoint: Offset): Matrix {
  val matrix = Matrix()

  val deltaX = lastPoint.x - firstPoint.x
  val deltaY = lastPoint.y - firstPoint.y

  val angleRad = atan2(deltaY, deltaX)
  val angle = angleRad * (180 / PI)

  matrix.rotateZ(angle.toFloat())
  matrix.translate(
    x = -firstPoint.x,
    y = -firstPoint.y,
  )

  return matrix
}
