package com.henni.handwriting.kmp.tool

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path

fun onEraserDragStart(
    currentOffset: Offset,
    offset: Offset,
) {
    currentOffset.copy(offset.x, offset.y)
}

fun onEraserDrag(
    eraserPath: Path,
    eraserPointRadius: Int,
    currentOffset: Offset,
    onPathRemoved: (Path) -> Unit
) {
    eraserPath.addOval(
        Rect(
            currentOffset.x - eraserPointRadius,
            currentOffset.y - eraserPointRadius,
            currentOffset.x + eraserPointRadius,
            currentOffset.y + eraserPointRadius
        )
    )
    onPathRemoved(eraserPath)
}

fun onEraserDragEnd(
    eraserPath: Path,
    onDragFinished: () -> Unit
) {
    eraserPath.reset()
    onDragFinished()
}