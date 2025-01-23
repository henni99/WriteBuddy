package com.anonymous.handwriting.operation

import androidx.compose.runtime.Stable

@Stable
interface OperationManager {

    val undoOperationStack: ArrayDeque<Operation>

    val redoOperationStack: ArrayDeque<Operation>

    fun executeOperation(operation: Operation)

    fun undo()

    fun redo()
}