package com.anonymous.handwriting.operation

import com.anonymous.handwriting.HandWritingElement

class InsertOperation constructor(
    private val element: HandWritingElement
) : Operation {

    override fun doOperation(operationEvent: (HandWritingElement) -> Unit): Boolean {
        operationEvent(element)
        return true
    }

    override fun undo(reOperationEvent: (HandWritingElement) -> Unit) {
        reOperationEvent(element)
    }

    override fun redo(operationEvent: (HandWritingElement) -> Unit) {
        operationEvent(element)
    }

}