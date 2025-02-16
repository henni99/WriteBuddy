package com.henni.handwriting.kmp.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class Padding(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    companion object {

        @Stable
        val Zero = Padding(0, 0, 0, 0)
    }
}
