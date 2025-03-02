package com.henni.handwriting.model

import androidx.compose.runtime.Immutable

/**
 * Represents the result of an eraser hit test on a [HandwritingPath] object.
 *
 * This class determines whether an eraser has interacted with [HandwritingPath]
 * and provides the associated data if a hit occurs.
 */

@Immutable
internal data class HitResult(

    /**
     * Indicates whether the eraser has hit the [HandwritingPath].
     * `true` if a hit is detected, `false` otherwise.
     */
    val isHit: Boolean,

    /**
     * The handwriting data that was hit by the eraser.
     * Null if no hit occurred.
     */
    val path: HandwritingPath?
)