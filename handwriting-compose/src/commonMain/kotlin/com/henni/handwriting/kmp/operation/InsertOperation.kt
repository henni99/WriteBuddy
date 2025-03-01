package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingPath


class InsertOperation internal constructor(
    private val controller: HandwritingController,
    private val data: HandwritingPath,
) : Operation {

    override fun doOperation(): Boolean {
        controller.addHandWritingData(data)
        return true
    }

    override fun undo() = with(controller) {
        removeHandWritingData(data)
    }

    override fun redo() = with(controller) {
        addHandWritingData(data)
    }

}