package com.henni.writebuddy.extension

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import com.henni.writebuddy.model.Padding

/**
 * Checks if this rectangle contains another rectangle.
 * A rectangle contains another if it fully encloses the other rectangle.
 *
 * @param other The rectangle to check.
 * @return `true` if this rectangle contains the other, `false` otherwise.
 */
internal fun Rect.contains(other: Rect): Boolean = (this.left <= other.left) &&
  (this.right >= other.right) &&
  (this.top <= other.top) &&
  (this.bottom >= other.bottom)

/**
 * Translates the rectangle by the given matrix transformation.
 * This shifts the rectangle's position based on the matrix's translation values.
 *
 * @param matrix The matrix that contains translation values.
 * @return A new rectangle that is translated by the matrix.
 */
internal fun Rect.translate(matrix: Matrix): Rect = this.translate(
  matrix.values[12],
  matrix.values[13],
)
