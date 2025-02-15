package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.util.fastForEachReversed

fun onPenDragStart(
    canvas: Canvas?,
    offset: Offset,
    penPathOffsets: SnapshotStateList<Offset>,
    paint: Paint,
    penPath: Path
) {
    penPath.reset()
    penPath.moveTo(offset.x, offset.y)
    penPath.lineTo(offset.x, offset.y)
    penPathOffsets.add(offset)
    canvas?.drawPath(penPath, paint)
}

fun onPenDrag(
    canvas: Canvas?,
    previousOffset: Offset,
    currentOffset: Offset,
    penPathOffsets: SnapshotStateList<Offset>,
    paint: Paint,
    penPath: Path
) {
//    penPath.lineTo(currentOffset.x, currentOffset.y)
    penPath.quadraticBezierTo(
        previousOffset.x,
        previousOffset.y,
        (currentOffset.x + previousOffset.x) / 2,
        (currentOffset.y + previousOffset.y) / 2
    )
    penPathOffsets.add(currentOffset)
    canvas?.drawPath(penPath, paint)
}

fun onPenDragEnd(
    canvas: Canvas?,
    paint: Paint,
    penPathOffsets: SnapshotStateList<Offset>,
    penPath: Path,
    onDragFinished: (Path,  SnapshotStateList<Offset>) -> Unit
) {
    penPathOffsets.fastForEachReversed {
        penPath.lineTo(it.x, it.y)
    }

    canvas?.drawPath(penPath, paint)
    onDragFinished(penPath, penPathOffsets)
//    controller.addHandWritingPath(penPath, penPathOffsets)

    penPathOffsets.clear()
}

fun onPenDragCancel(
    penPath: Path,
    penPathOffsets: SnapshotStateList<Offset>,
) {
    penPath.reset()
    penPathOffsets.clear()
}