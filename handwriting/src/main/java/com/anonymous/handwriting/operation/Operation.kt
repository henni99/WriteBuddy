package com.anonymous.handwriting.operation

import com.anonymous.handwriting.HandWritingElement

interface Operation {

    fun doOperation(operationEvent: (HandWritingElement) -> Unit): Boolean

    fun undo(reOperationEvent: (HandWritingElement) -> Unit)

    fun redo(operationEvent: (HandWritingElement) -> Unit)
}