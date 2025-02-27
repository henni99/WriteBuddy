package com.henni.handwriting.kmp

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import com.henni.handwriting.kmp.model.Padding

fun Rect.contains(other: Rect): Boolean {
    return (this.left <= other.left) &&
            (this.right >= other.right) &&
            (this.top <= other.top) &&
            (this.bottom >= other.bottom)
}

fun Rect.unions(
    other: Rect,
): Rect {
    if(this == Rect.Zero && other != Rect.Zero) {
        return Rect(
            left = other.left,
            top = other.top,
            right = other.right,
            bottom = other.bottom
        )
    }

    if(this != Rect.Zero && other == Rect.Zero) {
        return Rect(
            left = other.left,
            top = other.top,
            right = other.right,
            bottom = other.bottom
        )
    }

    if(this == Rect.Zero && other == Rect.Zero) {
        return Rect.Zero
    }

    return Rect(
        left = minOf(this.left, other.left),
        top = minOf(this.top, other.top),
        right = maxOf(this.right, other.right),
        bottom = maxOf(this.bottom, other.bottom )
    )
}

fun Rect.addPadding(padding: Padding): Rect {
    return Rect(
        this.left - padding.left,
        this.top - padding.top,
        this.right + padding.right,
        this.bottom + padding.bottom
    )
}

fun Rect.translate(matrix: Matrix): Rect {
    return this.translate(
        matrix.values[12],
        matrix.values[13]
    )
}