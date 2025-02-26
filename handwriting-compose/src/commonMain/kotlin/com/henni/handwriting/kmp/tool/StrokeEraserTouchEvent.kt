package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import com.henni.handwriting.kmp.HandwritingController

class StrokeEraserTouchEvent constructor(
    private val controller: HandwritingController

): ToolTouchEvent {

    private var eraserPath by mutableStateOf(Path())

    private var eraserOffset by mutableStateOf(Offset.Zero)

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        eraserPath = Path()
        eraserOffset = offset
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        eraserOffset = currentOffset
        eraserPath.addOval(
            Rect(
                eraserOffset.x - controller.eraserPointRadius,
                eraserOffset.y - controller.eraserPointRadius,
                eraserOffset.x + controller.eraserPointRadius,
                eraserOffset.y + controller.eraserPointRadius
            )
        )
        controller.removeHandWritingPath(eraserPath)
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {
        eraserPath.reset()
        eraserOffset = Offset.Zero
    }

    override fun onTouchCancel() {
        eraserPath = Path()
        eraserOffset = Offset.Zero
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint) {

        if (eraserOffset != Offset.Zero && controller.isEraserPointShowed) {
            canvas.drawCircle(
                eraserOffset,
                controller.eraserPointRadius,
                paint
            )
        }
    }
}