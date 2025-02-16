package com.henni.handwriting.kmp.operation

import androidx.compose.ui.geometry.Offset
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData


class TranslateOperation(
    private val controller: HandwritingController,
    private val dataSet: MutableSet<HandwritingData>,
    private val offset: Offset
) : Operation {
    override fun doOperation(): Boolean {
        dataSet.forEach { data ->
            data.matrix.translate(
                offset.x,
                offset.y
            )
        }

        return true
    }

    override fun undo() {

        println("undo: ${dataSet}")
        println("undo: ${offset}")

        dataSet.forEach { element ->

            element.matrix.translate(
                -offset.x,
                -offset.y
            )

            element.path.translate(offset.unaryMinus())
        }

        controller.selectedBoundBox.value = controller.selectedBoundBox.value.translate(
            offset.unaryMinus()
        )
    }

    override fun redo() {
        dataSet.forEach { element ->
            element.matrix.translate(
                offset.x,
                offset.y
            )
            element.path.translate(offset)
        }
        controller.selectedBoundBox.value = controller.selectedBoundBox.value.translate(
            offset
        )
    }

}