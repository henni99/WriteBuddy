package com.henni.handwriting.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin

/** Returns a default pen [Paint]. */
internal fun defaultPenPaint(): Paint {
    return Paint().apply {
        color = Color.Black
        strokeWidth = 14f
        isAntiAlias = true
        style = PaintingStyle.Stroke
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
    }
}

/** Returns a default stroke eraser [Paint]. */
internal fun defaultStrokeEraserPaint(): Paint {
    return Paint().apply {
        color = Color.LightGray
        isAntiAlias = true
        style = PaintingStyle.Fill
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
    }
}


/** Returns a default [Paint]. */
internal fun lassoDefaultPaint(): Paint {
    return Paint().apply {
        color = Color.Black
        strokeWidth = 12f
        isAntiAlias = true
        style = PaintingStyle.Stroke
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 40f), 10f)
    }
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