package com.henni.handwriting.kmp.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Immutable
data class HandwritingData @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val path: Path,
    val deformationPath: Path,
    val paint: Paint,
    val matrix: Matrix = Matrix(),
    val originalOffsets: List<Offset> = emptyList()
)
