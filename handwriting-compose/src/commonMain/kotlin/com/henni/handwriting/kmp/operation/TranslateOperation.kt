package com.henni.handwriting.kmp.operation

import androidx.compose.ui.geometry.Offset
import com.henni.handwriting.kmp.HandwritingController
import com.henni.handwriting.kmp.model.HandwritingPath


class TranslateOperation internal constructor(
    private val controller: HandwritingController,
    private val dataSet: MutableSet<HandwritingPath>,
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

            element.renderedPath.translate(offset.unaryMinus())
            element.hitAreaPath.translate(offset.unaryMinus())
        }

        selectedBoundBox = selectedBoundBox.translate(offset.unaryMinus())
    }

    override fun redo() = with(controller) {
        dataSet.forEach { element ->
            element.matrix.translate(
                offset.x,
                offset.y
            )
            element.renderedPath.translate(offset)
            element.hitAreaPath.translate(offset)
        }
        selectedBoundBox = selectedBoundBox.translate(offset)
    }

}