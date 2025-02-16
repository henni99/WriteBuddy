package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData


class InsertOperation constructor(
    private val controller: HandwritingController,
    private val data: HandwritingData,
) : Operation {

    override fun doOperation(): Boolean {
        controller.addHandWritingData(data)
        return true
    }

    override fun undo() {
        controller.removeHandWritingData(data)
    }

    override fun redo() {
        controller.addHandWritingData(data)
    }

}