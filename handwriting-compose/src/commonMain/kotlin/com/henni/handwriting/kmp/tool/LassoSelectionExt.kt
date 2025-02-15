package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path

fun onLassoSelectDragStart(
    canvas: Canvas?,
    offset: Offset,
    lassoPathOffsets: SnapshotStateList<Offset>,
    paint: Paint,
    lassoPath: Path
) {
    lassoPath.reset()
    lassoPath.moveTo(offset.x, offset.y)
    lassoPath.lineTo(offset.x, offset.y)
    lassoPathOffsets.add(offset)
    canvas?.drawPath(lassoPath, paint)
}

fun onLassoSelectDrag(
    onDragPrevious: (Path) -> Unit,
    canvas: Canvas?,
    previousOffset: Offset,
    currentOffset: Offset,
    lassoPathOffsets: SnapshotStateList<Offset>,
    paint: Paint,
    lassoPath: Path
) {
//    penPath.lineTo(currentOffset.x, currentOffset.y)
    onDragPrevious(lassoPath)
    lassoPath.quadraticBezierTo(
        previousOffset.x,
        previousOffset.y,
        (currentOffset.x + previousOffset.x) / 2,
        (currentOffset.y + previousOffset.y) / 2
    )
    lassoPathOffsets.add(currentOffset)
    canvas?.drawPath(lassoPath, paint)
}

fun onLassoTap(
    currentOffset: Offset,
    lassoTapRadius: Float = 20f,
    onTapEnd: (Path) -> Unit
) {
    val tapPath = Path()
    tapPath.addOval(
        Rect(
            center = currentOffset,
            radius = lassoTapRadius
        )
    )
    onTapEnd(tapPath)
}

fun onLassoSelectDragEnd(
    lassoPathOffsets: SnapshotStateList<Offset>,
    lassoPath: Path,
    onDragFinished: (Path, SnapshotStateList<Offset>) -> Unit
) {
    lassoPathOffsets.clear()
    onDragFinished(lassoPath, lassoPathOffsets)
}

fun onLassoSelectDragCancel(
    lassoPath: Path,
    lassoPathOffsets: SnapshotStateList<Offset>,
) {
    lassoPath.reset()
    lassoPathOffsets.clear()
}