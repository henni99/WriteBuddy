package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingPath

/**
 * [RemoveOperation] represents the operation of removing a handwriting path.
 *
 * @property controller The handwriting controller object that manages handwriting paths.
 * @property path The handwriting path to be removed.
 */

internal class RemoveOperation internal constructor(
    private val controller: HandwritingController,
    private val path: HandwritingPath
) : Operation {

    /**
     * Performs the remove operation by deleting the specified handwriting path.
     */
    override fun doOperation(): Boolean {
        controller.removeHandWritingPath(path)
        return true
    }

    /**
     * Undo the remove operation by re-adding the removed handwriting path.
     */
    override fun undo() = with(controller) {
        addHandWritingPath(path)
    }

    /**
     * Redo the remove operation by removing the handwriting path again.
     */
    override fun redo() = with(controller) {
        removeHandWritingPath(path)
    }
}