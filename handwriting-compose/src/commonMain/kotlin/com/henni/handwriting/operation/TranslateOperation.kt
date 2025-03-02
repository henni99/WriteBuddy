package com.henni.handwriting.operation

import androidx.compose.ui.geometry.Offset
import com.henni.handwriting.HandwritingController
import com.henni.handwriting.model.HandwritingPath

/**
 * [TranslateOperation] represents the operation of translating (moving) a set of handwriting paths by a specified offset.
 *
 * @property controller The handwriting controller object that manages handwriting paths and related data.
 * @property paths A set of handwriting paths to be translated.
 * @property offset The offset by which the paths should be translated (moved).
 */

internal class TranslateOperation internal constructor(
    private val controller: HandwritingController,
    private val paths: MutableSet<HandwritingPath>,
    private val offset: Offset
) : Operation {

    /**
     * Performs the translation operation by applying the offset to each handwriting path.
     */
    override fun doOperation(): Boolean {
        paths.forEach { path ->
            path.matrix.translate(
                offset.x,
                offset.y
            )
        }
        return true
    }

    /**
     * Undoes the translation operation by reversing the offset on each handwriting path.
     * Also reverses the translation for the rendered and hit area paths, and adjusts the lasso boundary box.
     */
    override fun undo() = with(controller) {
        paths.forEach { path ->

            path.matrix.translate(
                -offset.x,
                -offset.y
            )

            path.renderedPath.translate(offset.unaryMinus())
            path.hitAreaPath.translate(offset.unaryMinus())
        }

        lassoBoundBox = lassoBoundBox.translate(offset.unaryMinus())
    }

    /**
     * Redoes the translation operation by applying the offset to each handwriting path again.
     * Also re-applies the translation for the rendered and hit area paths, and adjusts the lasso boundary box.
     */
    override fun redo() = with(controller) {
        paths.forEach { path ->
            path.matrix.translate(
                offset.x,
                offset.y
            )
            path.renderedPath.translate(offset)
            path.hitAreaPath.translate(offset)
        }
        lassoBoundBox = lassoBoundBox.translate(offset)
    }

}