package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData
import com.henni.handwriting.kmp.model.defaultPaint
import com.henni.handwriting.kmp.updateTick

class LassoSelectTouchEvent constructor(
    private val controller: HandwritingController
): ToolTouchEvent {

    private var lassoPath by mutableStateOf(Path())

    private var isTap = true

    private var firstPoint by mutableStateOf(Offset.Zero)

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        println("LassoSelectTouchEvent onTouchStart ${offset}")
        isTap = true
        lassoPath = Path()
        lassoPath.moveTo(offset.x, offset.y)
        firstPoint = offset
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        firstPoint = Offset.Zero
        isTap = false
        if(lassoPath.isEmpty) {
            lassoPath.moveTo(currentOffset.x, currentOffset.y )
        }

        println("LassoSelectTouchEvent onTouchMove ${previousOffset} ${currentOffset}")
        lassoPath.quadraticBezierTo(
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
        println("LassoSelectTouchEvent onTouchEnd ")
        if(isTap) {
            lassoPath.addOval(
                Rect(
                    center = firstPoint,
                    radius = 10f
                )
            )
        }

        controller.selectHandWritingData(
            path = lassoPath,
        )
    }

    override fun onTouchCancel() {
        lassoPath = Path()
        isTap = true
        firstPoint = Offset.Zero
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint) {

        println("onDrawIntoCanvas: ${lassoPath.isEmpty}")
        if(!lassoPath.isEmpty) {
            canvas.drawPath(lassoPath, paint)
        }

        if(!controller.selectedBoundBox.isEmpty) {
            canvas.drawRect(
                controller.selectedBoundBox,
                controller.selectedBoundBoxPaint
            )
        }

        controller.selectedDataSet.forEach { data ->
            canvas.drawPath(data.path, data.paint)
        }

//        controller.handwritingDataCollection.forEach { data ->
//
//            val dataPath = Path().apply {
//                addPath(data.path)
//            }
//
//            val pathWithOp = Path().apply {
//                this.op(dataPath, lassoPath, PathOperation.Intersect)
//            }
//
//            if (controller.isSelectedDataHighlight) {
//                canvas.drawPath(pathWithOp, defaultPaint().apply {
//                    this.color = controller.selectedDataHighlightColor
//                })
//            }
//        }

    }
}