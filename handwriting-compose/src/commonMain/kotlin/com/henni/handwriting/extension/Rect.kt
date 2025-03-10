package com.henni.handwriting.extension

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import com.henni.handwriting.model.Padding

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
 * Returns a new rectangle that is the union of this rectangle and another rectangle.
 * The resulting rectangle will cover both rectangles, expanding as needed to include both.
 *
 * @param other The rectangle to union with this one.
 * @return A new rectangle that covers both this and the other rectangle.
 */
internal fun Rect.unions(
  other: Rect,
): Rect {
  if (this == Rect.Zero && other != Rect.Zero) {
    return Rect(
      left = other.left,
      top = other.top,
      right = other.right,
      bottom = other.bottom,
    )
  }

  if (this != Rect.Zero && other == Rect.Zero) {
    return Rect(
      left = other.left,
      top = other.top,
      right = other.right,
      bottom = other.bottom,
    )
  }

  if (this == Rect.Zero && other == Rect.Zero) {
    return Rect.Zero
  }

  return Rect(
    left = minOf(this.left, other.left),
    top = minOf(this.top, other.top),
    right = maxOf(this.right, other.right),
    bottom = maxOf(this.bottom, other.bottom),
  )
}

/**
 * Adds padding to the rectangle, expanding it outward by the specified padding values.
 *
 * @param padding The padding to apply to the rectangle.
 * @return A new rectangle that is expanded by the padding.
 */
internal fun Rect.addPadding(padding: Padding): Rect = Rect(
  this.left - padding.left,
  this.top - padding.top,
  this.right + padding.right,
  this.bottom + padding.bottom,
)

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
