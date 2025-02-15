package com.henni.handwriting.kmp

import androidx.compose.ui.geometry.Rect

fun Rect.contains(other: Rect): Boolean {
    return (this.left >= other.left) &&
            (this.right >= other.right) &&
            (this.top >= other.top) &&
            (this.bottom >= other.bottom)
}

fun Rect.unions(
    other: Rect,
    padding: Int
): Rect {
    if(this == Rect.Zero && other != Rect.Zero) {
        return Rect(
            left = other.left - padding,
            top = other.top - padding,
            right = other.right + padding,
            bottom = other.bottom + padding
        )
    }

    if(this != Rect.Zero && other == Rect.Zero) {
        return Rect(
            left = this.left - padding,
            top = this.top - padding,
            right = this.right + padding,
            bottom = this.bottom + padding
        )
    }

    if(this == Rect.Zero && other == Rect.Zero) {
        return Rect.Zero
    }

    return Rect(
        left = minOf(this.left - padding, other.left - padding),
        top = minOf(this.top - padding, other.top - padding),
        right = maxOf(this.right + padding, other.right + padding),
        bottom = maxOf(this.bottom + padding, other.bottom + padding)
    )
}