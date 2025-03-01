package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.extension.updateTick
import com.henni.handwriting.kmp.model.ToolMode

/**
 * A class representing the touch event handling for moving a lasso-bound selection.
 * This allows the user to drag and move the lasso bounding box and the selected handwriting data within it.
 * The controller is responsible for managing the state of the handwriting data and the lasso selection.
 *
 * @param controller The handwriting controller responsible for managing selection and transformation of data.
 */

internal class LassoMoveTouchEvent internal constructor(
    private val controller: HandwritingController
) : ToolTouchEvent {

    // Flag to determine if the lasso bounding box can be moved
    private var isLassoBoundBoxMovable by mutableStateOf(false)

    // Stores the initial touch position
    private var firstOffset by mutableStateOf(Offset.Zero)

    // Tracks the current offset during the touch interaction
    private var currentOffset by mutableStateOf(Offset.Zero)

    // Transformation matrix used to apply translations to the bounding box and selected data
    private var transformMatrix by mutableStateOf(Matrix())

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        isLassoBoundBoxMovable = true
        firstOffset = offset
        this.currentOffset = offset

        if (controller.lassoBoundBox.contains(offset)) {
            controller.refreshTick.updateTick()
        } else {
            controller.setToolMode(ToolMode.LassoSelectMode)
        }
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        if (isLassoBoundBoxMovable) {

            transformMatrix.reset()
            transformMatrix.translate(
                currentOffset.x - previousOffset.x,
                currentOffset.y - previousOffset.y
            )

            this.currentOffset = currentOffset

            controller.transformlassoBoundBox(transformMatrix)
            controller.selectedDataSet.forEach { data ->
                data.renderedPath.transform(transformMatrix)
                data.hitAreaPath.transform(transformMatrix)
            }
        }
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {

        val translateX = currentOffset.x - firstOffset.x
        val translateY = currentOffset.y - firstOffset.y

        controller.translateHandWritingDataSet(
            translateOffset = Offset(
                x = translateX,
                y = translateY
            )
        )

        firstOffset = Offset.Zero
        currentOffset = Offset.Zero
        controller.refreshTick.updateTick()
    }

    override fun onTouchInitialize() {
        isLassoBoundBoxMovable = false
        firstOffset = Offset.Zero
        currentOffset =  Offset.Zero
        transformMatrix = Matrix()
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {

        val lassoBoundBox = controller.lassoBoundBox
        val lassoBoundBoxPaint = controller.lassoBoundBoxPaint

        if(lassoBoundBox.center != Offset.Zero) {
            canvas.drawRect(
                lassoBoundBox,
                lassoBoundBoxPaint
            )
        }

        controller.selectedDataSet.forEach { data ->
            canvas.drawPath(data.renderedPath, data.paint)
        }
    }
}