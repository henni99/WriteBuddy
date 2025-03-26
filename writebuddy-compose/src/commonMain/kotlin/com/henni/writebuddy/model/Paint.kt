package com.henni.writebuddy.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin

/** Returns a default pen [Paint]. */
internal fun defaultLaserPaint(): Paint = Paint().apply {
    color = Color.Black
    strokeWidth = 6f
    isAntiAlias = true
    style = PaintingStyle.Stroke
    strokeJoin = StrokeJoin.Round
    strokeCap = StrokeCap.Round
}

/** Returns a default pen [Paint]. */
internal fun defaultTapePaint(): Paint = Paint().apply {
    color = Color.Cyan
    strokeWidth = 100f
    isAntiAlias = true
    style = PaintingStyle.Stroke
}

/** Returns a default [Paint]. */
internal fun defaultBorderBoxPaint(): Paint = Paint().apply {
    color = Color.Black
    strokeWidth = 6f
    isAntiAlias = true
    style = PaintingStyle.Stroke
    strokeJoin = StrokeJoin.Round
    strokeCap = StrokeCap.Round
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 40f), 10f)
}

internal fun Paint.copy(from: Paint): Paint = apply {
    alpha = from.alpha
    isAntiAlias = from.isAntiAlias
    color = from.color
    blendMode = from.blendMode
    style = from.style
    strokeWidth = from.strokeWidth
    strokeCap = from.strokeCap
    strokeJoin = from.strokeJoin
    strokeMiterLimit = from.strokeMiterLimit
    filterQuality = from.filterQuality
    shader = from.shader
    colorFilter = from.colorFilter
    pathEffect = from.pathEffect
}
