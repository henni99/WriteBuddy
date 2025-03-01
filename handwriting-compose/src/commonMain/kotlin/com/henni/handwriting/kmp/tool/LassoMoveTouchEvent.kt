package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.extension.updateTick

class LassoMoveTouchEvent constructor(
    private val controller: HandwritingController
) : ToolTouchEvent {

    private var canSelectBoxMoved by mutableStateOf(false)

    private var firstOffset by mutableStateOf(Offset.Zero)

    private var offset by mutableStateOf(Offset.Zero)

    private var transformMatrix by mutableStateOf(Matrix())

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        println("LassoMoveTouchEvent onTouchStart: ${controller.lassoBoundBox} ${controller.lassoBoundBox.contains(offset)}")
        canSelectBoxMoved = true
        firstOffset = offset
        this.offset = offset

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
        if (canSelectBoxMoved) {

            transformMatrix.reset()
            transformMatrix.translate(
                currentOffset.x - previousOffset.x,
                currentOffset.y - previousOffset.y
            )

            offset = currentOffset

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

        controller.translateHandWritingDataSet(
            translateOffset = Offset(
                x = offset.x - firstOffset.x,
                y = offset.y - firstOffset.y
            )
        )

        firstOffset = Offset.Zero
        offset = Offset.Zero
        controller.refreshTick.updateTick()
    }

    override fun onTouchCancel() {
        canSelectBoxMoved = false
        firstOffset = Offset.Zero
        offset =  Offset.Zero
        transformMatrix = Matrix()
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {
        if(controller.lassoBoundBox.center != Offset.Zero) {
            canvas.drawRect(
                controller.lassoBoundBox,
                controller.lassoBoundBoxPaint
            )
        }

        controller.selectedDataSet.forEach { data ->
            canvas.drawPath(data.renderedPath, data.paint)
        }
    }
}