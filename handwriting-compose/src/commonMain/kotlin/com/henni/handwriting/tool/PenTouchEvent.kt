package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.extension.addDeformationPoint

/**
 * A class representing the touch event handling for a pen tool. This class handles the touch interactions,
 *
 * @param controller The handwriting controller used to manage the drawing paths.
 */

internal class PenTouchEvent internal constructor(
    private val controller: HandwritingController
) : ToolTouchEvent {

    // Stores the path that will be rendered on the canvas
    private var renderedPenPath by mutableStateOf(Path())

    // Stores the hit area path for touch detection purposes
    private var hitAreaPath by mutableStateOf(Path())

    // List of touch offsets to track the movement during drawing
    private val offsets = mutableListOf<Offset>()

    override fun onTouchInitialize() {
        renderedPenPath = Path()
        hitAreaPath = Path()

        offsets.clear()
    }

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        renderedPenPath = Path()
        renderedPenPath.moveTo(offset.x, offset.y)

        hitAreaPath = Path()
        hitAreaPath.moveTo(offset.x, offset.y)

        offsets.clear()
        offsets.add(offset)
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        renderedPenPath.quadraticTo(
            previousOffset.x,
            previousOffset.y,
            (currentOffset.x + previousOffset.x) / 2,
            (currentOffset.y + previousOffset.y) / 2
        )
        hitAreaPath.quadraticTo(
            previousOffset.x,
            previousOffset.y,
            (currentOffset.x + previousOffset.x) / 2,
            (currentOffset.y + previousOffset.y) / 2
        )
        hitAreaPath.addDeformationPoint(currentOffset)

        offsets.add(currentOffset)
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {
        controller.addHandWritingPath(renderedPenPath, hitAreaPath, offsets)
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {
        if(!isMultiTouch) {
            canvas.drawPath(renderedPenPath, paint)
        }
    }
}