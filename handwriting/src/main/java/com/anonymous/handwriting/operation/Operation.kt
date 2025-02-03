package com.anonymous.handwriting.operation

import com.anonymous.handwriting.HandWritingElement

interface Operation {

    fun doOperation(): Boolean

    fun undo()

    fun redo()
}