package com.henni.handwriting.kmp.tool

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path

interface ToolTouchEvent {

    fun onTouchStart(
        canvas: Canvas? = null,
        offset: Offset = Offset.Zero,
        paint: Paint = Paint(),
    )

    fun onTouchMove(
        canvas: Canvas? = null,
        previousOffset: Offset = Offset.Zero,
        currentOffset: Offset = Offset.Zero,
        paint: Paint = Paint(),
    )

    fun onTouchEnd(
        canvas: Canvas? = null,
        paint: Paint = Paint(),
    )

    fun onTouchTap(
        offset: Offset,
        radius: Float = 20f,
    ) { }

    fun onTouchCancel(

    )

    fun onDrawIntoCanvas(canvas: Canvas, paint: Paint = Paint())
}