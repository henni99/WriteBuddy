package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData


class InsertOperation constructor(
    private val controller: HandwritingController,
    private val data: HandwritingData,
) : Operation {

    override fun doOperation(): Boolean {
        controller.addHandWritingData(data)
        controller.updateOperationState()
        return true
    }

    override fun undo() {
        controller.removeHandWritingData(data)
        controller.updateOperationState()
    }

    override fun redo() {
        controller.addHandWritingData(data)
        controller.updateOperationState()
    }

}