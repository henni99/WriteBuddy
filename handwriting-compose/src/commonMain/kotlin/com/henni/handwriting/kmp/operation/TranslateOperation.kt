package com.henni.handwriting.kmp.operation

import androidx.compose.ui.geometry.Offset
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingData


class TranslateOperation internal constructor(
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

    override fun undo() = with(controller) {

        println("undo: ${dataSet}")
        println("undo: ${offset}")

        dataSet.forEach { element ->

            element.matrix.translate(
                -offset.x,
                -offset.y
            )

            element.path.translate(offset.unaryMinus())
            element.deformationPath.translate(offset.unaryMinus())
        }

        selectedBoundBox = selectedBoundBox.translate(offset.unaryMinus())
    }

    override fun redo() = with(controller) {
        dataSet.forEach { element ->
            element.matrix.translate(
                offset.x,
                offset.y
            )
            element.path.translate(offset)
            element.deformationPath.translate(offset)
        }
        selectedBoundBox = selectedBoundBox.translate(offset)
    }

}