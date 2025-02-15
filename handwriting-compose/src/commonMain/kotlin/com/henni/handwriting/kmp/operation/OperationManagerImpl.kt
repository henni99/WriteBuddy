package com.henni.handwriting.kmp.operation

class OperationManagerImpl : OperationManager {

    override val undoStack: ArrayDeque<Operation> = ArrayDeque()

    override val redoStack: ArrayDeque<Operation> = ArrayDeque()

    override fun isUndoEmpty(): Boolean {
        return undoStack.isEmpty()
    }

    override fun isUndoNotEmpty(): Boolean {
        return undoStack.isNotEmpty()
    }

    override fun isRedoEmpty(): Boolean {
        return redoStack.isEmpty()
    }

    override fun isRedoNotEmpty(): Boolean {
        return redoStack.isNotEmpty()
    }

    override fun executeOperation(operation: Operation) {

        if (operation.doOperation()) {
            undoStack.add(operation)
            redoStack.clear()
        }
    }


    override fun undo() {
        if (undoStack.isNotEmpty()) {
            val operation = undoStack.removeLast()
            operation.undo()
            redoStack.add(operation)
        }
    }

    override fun redo() {
        if (redoStack.isNotEmpty()) {
            val operation = redoStack.removeLast()
            operation.redo()
            undoStack.add(operation)
        }
    }
}