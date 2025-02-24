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

    var lassoPath by mutableStateOf(Path())

    var offsets = mutableListOf<Offset>()


    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        println("LassoSelectTouchEvent onTouchStart ${offset}")

        offsets.clear()

        lassoPath.moveTo(offset.x, offset.y)
        lassoPath.lineTo(offset.x, offset.y)
        offsets.add(offset)
        controller.refreshTick.updateTick()
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
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
        offsets.add(currentOffset)
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {
        controller.selectHandWritingData(
            path = lassoPath,
        )

        offsets.clear()
    }

    override fun onTouchCancel() {
        TODO("Not yet implemented")
    }

    override fun onTouchTap(
        offset: Offset,
        radius: Float,
    ) {
        val tapPath = Path()
        tapPath.addOval(
            Rect(
                center = offset,
                radius = radius
            )
        )
        controller.initializeSelection()
        controller.selectHandWritingData(tapPath)
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint) {
        canvas.drawPath(lassoPath, paint)

        canvas.drawRect(
            controller.selectedBoundBox,
            controller.selectedBoundBoxPaint
        )

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