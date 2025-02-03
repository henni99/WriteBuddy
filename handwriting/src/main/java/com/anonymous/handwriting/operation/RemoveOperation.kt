package com.anonymous.handwriting.operation

import com.anonymous.handwriting.HandWritingElement
import com.anonymous.handwriting.HandwritingState

class RemoveOperation constructor(
    private val handwritingState: HandwritingState,
    private val element: HandWritingElement
) : Operation {
    override fun doOperation(): Boolean {
        handwritingState.removeHandWritingElement(element)
        handwritingState.updateOperationStack()
        return true
    }

    override fun undo() {
        handwritingState.addHandWritingElement(element)
        handwritingState.updateOperationStack()
    }

    override fun redo() {
        handwritingState.removeHandWritingElement(element)
        handwritingState.updateOperationStack()
    }
}