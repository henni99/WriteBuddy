package com.anonymous.handwriting

import android.graphics.PointF
import android.view.MotionEvent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path


interface HandWritingMotionEvent {

    fun actionDown()

    fun actionMove()

    fun actionUp()
}


inline fun penMotionEvent(
    event: MotionEvent,
    penPath: Path,
    currentPoint: PointF?,
    canvas: Canvas?,
    paint: Paint,
    addHandWritingPath: (Path) -> Unit
) {
    val motionTouchEventX = event.x
    val motionTouchEventY = event.y

    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            penPath.reset()
            penPath.moveTo(motionTouchEventX, motionTouchEventY)
            currentPoint?.set(motionTouchEventX, motionTouchEventY)
        }

        MotionEvent.ACTION_MOVE -> {
            penPath.lineTo(
                motionTouchEventX,
                motionTouchEventY,
            )

            canvas?.drawPath(penPath, paint)
        }

        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            addHandWritingPath(penPath)
            currentPoint?.set(motionTouchEventX, motionTouchEventY)
        }
    }
}

inline fun eraserMotionEvent(
    event: MotionEvent,
    currentPoint: PointF,
    eraserPath: Path,
    removeHandWritingPath: (Path, Int, Int) -> Unit
) {
    val motionTouchEventX = event.x
    val motionTouchEventY = event.y

    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            currentPoint.set(motionTouchEventX, motionTouchEventY)
        }

        MotionEvent.ACTION_MOVE -> {
            currentPoint.set(motionTouchEventX, motionTouchEventY)
            eraserPath.addOval(
                Rect(
                    motionTouchEventX - 10,
                    motionTouchEventY - 10,
                    motionTouchEventX + 10,
                    motionTouchEventY + 10
                )
            )

            removeHandWritingPath(
                eraserPath, motionTouchEventX.toInt(), motionTouchEventY.toInt()
            )
        }

        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            currentPoint.set(-40f, -40f)
        }
    }
}

inline fun lassoSelectionMotionEvent(
    event: MotionEvent,
    lassoPath: Path,
    currentPoint: PointF,
    canvas: Canvas?,
    paint: Paint,
    selectHandWritingPath: (Path) -> Unit
) {
    val motionTouchEventX = event.x
    val motionTouchEventY = event.y

    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            lassoPath.moveTo(motionTouchEventX, motionTouchEventY)
            currentPoint.set(motionTouchEventX, motionTouchEventY)
        }

        MotionEvent.ACTION_MOVE -> {
            lassoPath.lineTo(
                motionTouchEventX,
                motionTouchEventY,
            )

            canvas?.drawPath(lassoPath, paint)
        }

        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            selectHandWritingPath(lassoPath)
            lassoPath.reset()
        }
    }

    currentPoint.set(0f, 0f)
}