package com.anonymous.handwriting.operation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import com.anonymous.handwriting.HandWritingElement
import com.anonymous.handwriting.HandwritingState

class TranslateOperation(
    private val handwritingState: HandwritingState,
    private val elements: Set<HandWritingElement>,
    private val translateOffset: Offset
) : Operation {
    override fun doOperation(): Boolean {
        elements.forEach { element ->
            element.matrix.translate(
                translateOffset.x,
                translateOffset.y
            )
        }

        return true
    }

    override fun undo() {

        elements.forEach { element ->

            element.matrix.translate(
                - translateOffset.x,
                - translateOffset.y
            )
            element.path.translate(translateOffset.unaryMinus())

            Log.d("TransformOperation", "undo: originalMatrix: ${element.matrix}")

        }
        handwritingState.updateOperationStack()
    }

    override fun redo() {
        elements.forEach { element ->
            element.matrix.translate(
                translateOffset.x,
                translateOffset.y
            )
            element.path.translate(translateOffset)
        }
        handwritingState.updateOperationStack()

        Log.d("TransformOperation", "redo")

    }

}