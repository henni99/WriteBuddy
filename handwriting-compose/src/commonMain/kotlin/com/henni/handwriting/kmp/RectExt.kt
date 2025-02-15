package com.henni.handwriting.kmp

import androidx.compose.ui.geometry.Rect

fun Rect.contains(other: Rect): Boolean {
    return (this.left >= other.left) &&
            (this.right >= other.right) &&
            (this.top >= other.top) &&
            (this.bottom >= other.bottom)
}

fun Rect.unions(other: Rect): Rect {
    if(this == Rect.Zero && other != Rect.Zero) {
        return other
    }

    if(this != Rect.Zero && other == Rect.Zero) {
        return this
    }

    if(this == Rect.Zero && other == Rect.Zero) {
        return Rect.Zero
    }

    return Rect(
        left = minOf(this.left, other.left),
        top = minOf(this.top, other.top),
        right = maxOf(this.right, other.right),
        bottom = maxOf(this.bottom, other.bottom)
    )
}