package com.henni.handwriting.kmp.operation

import androidx.compose.runtime.Stable

interface OperationManager {

    val undoStack: ArrayDeque<Operation>

    val redoStack: ArrayDeque<Operation>

    fun isUndoEmpty(): Boolean

    fun isUndoNotEmpty(): Boolean

    fun isRedoEmpty(): Boolean

    fun isRedoNotEmpty(): Boolean

    fun executeOperation(operation: Operation)

    fun undo()

    fun redo()
}