package com.henni.handwriting.kmp.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

fun overlaps(path1: Path, path2: Path): Boolean {
    return Path().apply {
        this.op(path1, path2, PathOperation.Intersect)
    }.isEmpty.not()
}

fun Path.addDeformationPoint(offset: Offset) {
    this.lineTo(offset.x + 3, offset.y + 3)
    this.lineTo(offset.x - 6, offset.y + 3)
    this.lineTo(offset.x + 3, offset.y - 6)
    this.lineTo(offset.x - 6, offset.y - 6)
}