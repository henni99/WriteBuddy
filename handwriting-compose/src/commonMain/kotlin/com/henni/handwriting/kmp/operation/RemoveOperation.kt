package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData

class RemoveOperation constructor(
    private val controller: HandwritingController,
    private val data: HandwritingData
) : Operation {
    override fun doOperation(): Boolean {
        controller.removeHandWritingData(data)
        controller.updateOperationState()
        return true
    }

    override fun undo() {
        controller.addHandWritingData(data)
        controller.updateOperationState()
    }

    override fun redo() {
        controller.removeHandWritingData(data)
        controller.updateOperationState()
    }
}