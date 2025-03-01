package com.henni.handwriting.kmp.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Represents padding values with individual left, top, right, and bottom properties.
 *
 * This class is used to define padding around a certain area or component.
 */

@Immutable
data class Padding(
    /**
     * Padding value for the left side.
     */
    val left: Int,

    /**
     * Padding value for the top side.
     */
    val top: Int,

    /**
     * Padding value for the right side.
     */
    val right: Int,

    /**
     * Padding value for the bottom side.
     */
    val bottom: Int
) {
    companion object {

        /**
         * A predefined instance of Padding with all values set to zero.
         * Can be used as a default or empty padding.
         */

        @Stable
        val Zero = Padding(0, 0, 0, 0)
    }
}
