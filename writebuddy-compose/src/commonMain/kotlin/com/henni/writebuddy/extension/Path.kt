package com.henni.writebuddy.extension

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

internal fun overlaps(path1: Path, path2: Path): Boolean = Path().apply {
  this.op(path1, path2, PathOperation.Intersect)
}.isEmpty.not()
