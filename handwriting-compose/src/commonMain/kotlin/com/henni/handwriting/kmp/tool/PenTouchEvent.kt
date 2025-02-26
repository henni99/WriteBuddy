package com.henni.handwriting.kmp.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.addDeformationPoint
import com.henni.handwriting.kmp.updateTick

class PenTouchEvent constructor(
    private val controller: HandwritingController
) : ToolTouchEvent {

    private var penPath by mutableStateOf(Path())

    private var deformationPenPath by mutableStateOf(Path())

    private val offsets = mutableListOf<Offset>()

    override fun onTouchStart(
        canvas: Canvas?,
        offset: Offset,
        paint: Paint,
    ) {
        penPath = Path()
        deformationPenPath = Path()

        offsets.clear()

        offsets.add(offset)
        penPath.moveTo(offset.x, offset.y)
        deformationPenPath.moveTo(offset.x, offset.y)
    }

    override fun onTouchMove(
        canvas: Canvas?,
        previousOffset: Offset,
        currentOffset: Offset,
        paint: Paint,
    ) {
        penPath.quadraticBezierTo(
            previousOffset.x,
            previousOffset.y,
            (currentOffset.x + previousOffset.x) / 2,
            (currentOffset.y + previousOffset.y) / 2
        )
        deformationPenPath.quadraticBezierTo(
            previousOffset.x,
            previousOffset.y,
            (currentOffset.x + previousOffset.x) / 2,
            (currentOffset.y + previousOffset.y) / 2
        )
        deformationPenPath.addDeformationPoint(currentOffset)

        offsets.add(currentOffset)
    }

    override fun onTouchEnd(
        canvas: Canvas?,
        paint: Paint,
    ) {

        controller.addHandWritingPath(penPath, deformationPenPath, offsets)
        controller.refreshTick.updateTick()
    }

    override fun onTouchCancel() {
        penPath = Path()
        deformationPenPath = Path()

        offsets.clear()
    }

    override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint) {
        canvas.drawPath(penPath, paint)
    }

}