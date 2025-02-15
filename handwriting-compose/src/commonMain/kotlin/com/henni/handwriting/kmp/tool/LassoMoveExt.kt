package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path


fun onLassoMoveDragStart(
    offset: Offset,
    lassoPath: Path,
    selectedBounds: Rect,
    isMoveAllowed: (Path) -> Unit,
    isMoveNotAllowed: () -> Unit
) {
    lassoPath.reset()
    lassoPath.moveTo(offset.x, offset.y)
    if(selectedBounds.contains(offset)) {
        isMoveAllowed(lassoPath)
    } else {
        isMoveNotAllowed()
    }

}


fun onLassoMoveDragEnd(
    lassoPathOffsets: SnapshotStateList<Offset>,
    lassoPath: Path,
    onDragFinished: (Path, SnapshotStateList<Offset>) -> Unit
) {
    lassoPathOffsets.clear()
    onDragFinished(lassoPath, lassoPathOffsets)
}

fun onLassoMoveDragCancel(
    lassoPath: Path,
    lassoPathOffsets: SnapshotStateList<Offset>,
) {
    lassoPath.reset()
    lassoPathOffsets.clear()
}