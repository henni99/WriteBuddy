package com.henni.handwriting.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import com.henni.handwriting.HandwritingController

/**
 * A class representing the touch event handling for the lasso selection tool.
 * This class tracks touch interactions for selecting paths on the canvas by drawing a lasso path
 * and allowing the user to select handwriting data enclosed by the lasso.
 *
 * @param controller The handwriting controller responsible for managing selection data.
 */

internal class LassoSelectTouchEvent internal constructor(
    private val controller: HandwritingController
) : ToolTouchEvent {

    // Stores the path of the lasso selection
    private var lassoPath by mutableStateOf(Path())

    // Flag to indicate whether the user performed a tap or a lasso selection
    private var isTap = true

    // Stores the first offset (starting point of the selection)
    private var firstOffset by mutableStateOf(Offset.Zero)

    override fun onTouchInitialize() {
        lassoPath = Path()
        isTap = true
        firstOffset = Offset.Zero
    }

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        isTap = true
        lassoPath = Path()
        lassoPath.moveTo(offset.x, offset.y)
        firstOffset = offset
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        firstOffset = Offset.Zero
        isTap = false

        if (lassoPath.isEmpty) {
            lassoPath.moveTo(currentOffset.x, currentOffset.y)
        }

        lassoPath.quadraticTo(
            previousOffset.x,
            previousOffset.y,
            (currentOffset.x + previousOffset.x) / 2,
            (currentOffset.y + previousOffset.y) / 2
        )
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {
        if (isTap) {
            lassoPath.addOval(
                Rect(
                    center = firstOffset,
                    radius = 10f
                )
            )
        }

        controller.selectHandWritingPath(
            path = lassoPath,
        )
    }


    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {

        if (!lassoPath.isEmpty && !isMultiTouch) {
            canvas.drawPath(lassoPath, paint)
        }

        val lassoBoundBox = controller.lassoBoundBox
        val lassoBoundBoxPaint = controller.lassoBoundBoxPaint

        if (lassoBoundBox.center != Offset.Zero && !lassoBoundBox.isEmpty) {
            canvas.drawRect(
                lassoBoundBox,
                lassoBoundBoxPaint
            )
        }

        controller.selectedHandwritingPaths.forEach { path ->
            canvas.drawPath(path.renderedPath, path.paint)
        }


    }
}