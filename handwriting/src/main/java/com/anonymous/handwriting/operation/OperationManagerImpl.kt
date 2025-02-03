package com.anonymous.handwriting.operation

import android.util.Log
import androidx.compose.runtime.Stable
import com.anonymous.handwriting.HandWritingElement

@Stable
class OperationManagerImpl: OperationManager {

    override val undoOperationStack: ArrayDeque<Operation> = ArrayDeque()

    override val redoOperationStack: ArrayDeque<Operation> = ArrayDeque()

    override fun executeOperation(operation: Operation) {

        if (operation.doOperation()) {
            undoOperationStack.add(operation)
            redoOperationStack.clear()
        }
    }


    override fun undo() {
        if (undoOperationStack.isNotEmpty()) {
            val operation = undoOperationStack.removeLast()
            operation.undo()
            redoOperationStack.add(operation)
            Log.d("operationStack", "undoOperationStack: ${undoOperationStack.size} redoOperationStack: ${redoOperationStack.size}".toString())

        }
    }

    override fun redo() {
        if (redoOperationStack.isNotEmpty()) {
            val operation = redoOperationStack.removeLast()
            operation.redo()
            undoOperationStack.add(operation)
            Log.d("operationStack", "undoOperationStack: ${undoOperationStack.size} redoOperationStack: ${redoOperationStack.size}".toString())
        }
    }
}