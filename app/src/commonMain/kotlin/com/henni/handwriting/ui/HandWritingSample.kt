package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.henni.handwriting.kmp.HandWritingNote
import com.henni.handwriting.kmp.model.Padding
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.rememberHandwritingController

@Composable
fun HandWritingSample() {

    val controller = rememberHandwritingController {
        setIsNoteZoomable(true)
        setIsEraserPointShowed(true)
        setEraserPointRadius(20f)
        setLassoStrokeWidth(10f)
        setLassoColor(Color.Black)
        setSelectedBoxPadding(Padding(20, 20, 20, 20))
        setSelectedBoxStrokeWidth(10f)
        setSelectedBoxColor(Color.Black)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            HandWritingNote(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.Gray)
                    .padding(innerPadding),
                controller = controller
            )

            Row {

                Button(
                    modifier = Modifier.size(50.dp),
                    onClick = {
                        controller.setToolMode(ToolMode.PenMode)
                    }
                ) {
                    Text("펜")
                }

                Button(
                    modifier = Modifier.size(50.dp),
                    onClick = {
                        controller.setToolMode(ToolMode.EraserMode)
                    }
                ) {
                    Text("지")
                }

                Button(
                    modifier = Modifier.size(50.dp),
                    onClick = {
                        controller.setToolMode(ToolMode.LassoSelectMode)
                    }
                ) {
                    Text("올")
                }



                Button(
                    modifier = Modifier.size(50.dp),
                    onClick = {
                        controller.undo()
                    }
                ) {
                    Text("U")
                }

                Button(
                    modifier = Modifier.size(50.dp),
                    onClick = {
                       controller.redo()
                    }
                ) {
                    Text("R")
                }

            }

            colorPicker(
                onColorChange = { color ->
                    controller.setPenColor(color)
                }
            )
        }
    }

}