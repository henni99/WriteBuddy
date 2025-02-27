package com.henni.handwriting.kmp.operation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow

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

    fun updateOperationState()
}