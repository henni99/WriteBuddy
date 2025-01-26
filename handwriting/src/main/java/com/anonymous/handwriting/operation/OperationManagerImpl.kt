package com.anonymous.handwriting.operation

import android.util.Log
import androidx.compose.runtime.Stable
import com.anonymous.handwriting.HandWritingElement

@Stable
class OperationManagerImpl constructor(
    private val addElement: (HandWritingElement) -> Unit,
    private val removeElement: (HandWritingElement) -> Unit
) : OperationManager {

    override val undoOperationStack: ArrayDeque<Operation> = ArrayDeque()

    override val redoOperationStack: ArrayDeque<Operation> = ArrayDeque()

    override fun executeOperation(operation: Operation) {

        when(operation) {
            is InsertOperation -> {
                if (operation.doOperation(addElement)) {
                    Log.d("undoOperationStack", undoOperationStack.size.toString())

                    undoOperationStack.add(operation)
                    redoOperationStack.clear()
                }
            }
            is RemoveOperation -> {
                if (operation.doOperation(removeElement)) {
                    Log.d("undoOperationStack", undoOperationStack.size.toString())

                    undoOperationStack.add(operation)
                    redoOperationStack.clear()
                }
            }
        }




    }


    override fun undo() {
        if (undoOperationStack.isNotEmpty()) {
            val operation = undoOperationStack.removeLast()
            operation.undo(
                when(operation) {
                    is InsertOperation -> removeElement
                    is RemoveOperation -> addElement
                    else -> addElement
                })
            redoOperationStack.add(operation)
            Log.d("operationStack", "undoOperationStack: ${undoOperationStack.size} redoOperationStack: ${redoOperationStack.size}".toString())

        }
    }

    override fun redo() {
        if (redoOperationStack.isNotEmpty()) {
            val operation = redoOperationStack.removeLast()
            operation.redo(
                when(operation) {
                    is InsertOperation -> addElement
                    is RemoveOperation -> removeElement
                    else -> removeElement
                })
            undoOperationStack.add(operation)
            Log.d("operationStack", "undoOperationStack: ${undoOperationStack.size} redoOperationStack: ${redoOperationStack.size}".toString())
        }
    }


//    override fun undo() {
//        if (undoStack.isNotEmpty()) {
//            val operation = undoStack.removeLast()
//            operation.undo()
//            redoStack.add(operation)
//        }
//    }
//
//    override fun redo() {
//        if (redoStack.isNotEmpty()) {
//            val operation = redoStack.removeLast()
//            operation.redo()
//            undoStack.add(operation)
//        }
//    }


}