package com.henni.handwriting.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a single handwriting stroke with associated metadata.
 *
 * This class encapsulates the stroke path [Path], transformation data [Matrix], and rendering properties [Paint].
 */

@Immutable
data class HandwritingPath @OptIn(ExperimentalUuidApi::class) constructor(

    /**
     * Unique identifier for the handwriting stroke.
     * Defaults to a randomly generated UUID.
     */
    val id: String = Uuid.random().toString(),

    /**
     * The actual drawn path representing the handwriting stroke.
     */
    val renderedPath: Path,

    /**
     * A secondary path used for hit-testing selection areas (e.g., for lasso tool selection).
     * This helps determine if the handwriting stroke is selected.
     *
     * If you want to compare the target that was hit, you should use `hitAreaPath` instead of `path`.
     */
    val hitAreaPath: Path,

    /**
     * Paint properties defining the appearance of the handwriting stroke.
     */
    val paint: Paint,

    /**
     * Transformation matrix applied to the handwriting stroke.
     * Defaults to an identity matrix.
     *
     * This is used to apply transformations when loading saved [HandwritingPath],
     * allowing the final transformed state to be reconstructed using `initialPoints`.
     */
    val matrix: Matrix = Matrix(),

    /**
     * The original points of the handwriting stroke before transformations.
     * These points are used in combination with the transformation matrix to reconstruct
     * the final displayed stroke when loading saved [HandwritingPath].
     */
    val initialPoints: List<Offset> = emptyList()
)
