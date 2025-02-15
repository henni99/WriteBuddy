package com.henni.handwriting.kmp.operation

interface Operation {

    fun doOperation(): Boolean

    fun undo()

    fun redo()

}