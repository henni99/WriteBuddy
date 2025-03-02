package com.henni.handwriting.operation

import androidx.compose.runtime.Stable

/**
 * [Operation] defines the contract for operations that can be performed, undone, and redone.
 * Any class that implements this interface should provide specific implementations for these actions.
 */

@Stable
interface Operation {

    /**
     * Performs the operation.
     *
     * @return Returns true if the operation is successfully performed, false otherwise.
     */
    fun doOperation(): Boolean

    /**
     * Undo the operation, reverting any changes made by the `doOperation` method.
     */
    fun undo()

    /**
     * Redo the operation, reapplying any changes that were undone by the `undo` method.
     */
    fun redo()

}