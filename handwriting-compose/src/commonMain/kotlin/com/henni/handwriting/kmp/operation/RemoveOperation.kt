package com.henni.handwriting.kmp.operation

import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData

class RemoveOperation internal constructor(
    private val controller: HandwritingController,
    private val data: HandwritingData
) : Operation {
    override fun doOperation(): Boolean {
        controller.removeHandWritingData(data)
        return true
    }

    override fun undo() = with(controller) {
        addHandWritingData(data)
    }

    override fun redo() = with(controller) {
        removeHandWritingData(data)
    }
}