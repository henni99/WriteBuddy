package com.henni.handwriting.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

/**
 * Checks if two paths overlap by performing an intersection operation.
 * If the intersection result is not empty, it returns true, indicating that the paths overlap.
 *
 * @param path1 The first path to check.
 * @param path2 The second path to check.
 * @return `true` if the paths overlap, `false` otherwise.
 */

internal inline fun overlaps(path1: Path, path2: Path): Boolean {
  return Path().apply {
    this.op(path1, path2, PathOperation.Intersect)
  }.isEmpty.not()
}

/**
 * Adds a deformation point to the path at the specified offset.
 * The deformation is represented by a set of lines forming a small shape near the offset point for testing hit.
 *
 * @param offset The position to add the deformation point.
 */

internal inline fun Path.addDeformationPoint(offset: Offset) {
  this.lineTo(offset.x + 3, offset.y + 3)
  this.lineTo(offset.x - 6, offset.y + 3)
  this.lineTo(offset.x + 3, offset.y - 6)
  this.lineTo(offset.x - 6, offset.y - 6)
}
