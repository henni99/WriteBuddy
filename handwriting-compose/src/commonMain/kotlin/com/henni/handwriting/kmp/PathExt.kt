package com.henni.handwriting.kmp

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

fun overlaps(path1: Path, path2: Path): Boolean {
    return Path().apply {
        this.op(path1, path2, PathOperation.Intersect)
    }.isEmpty.not()
}