package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.dp
import com.henni.handwriting.kmp.HandWritingNote
import com.henni.handwriting.kmp.model.Padding
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.rememberHandwritingController

@Composable
fun HandWritingSample() {

    val controller = rememberHandwritingController {
        isZoomable = true
        isEraserPointShowed = true
        eraserPointRadius = 20f
        lassoBoundBoxPadding = Padding(20, 20, 20, 20)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            item {
                HandWritingNote(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .background(Color.Gray)
                        .padding(innerPadding),
                    controller = controller,
                    contentWidth = 300.dp,
                    contentHeight = 300.dp
                )

                ToolPicker(controller)
                EnhancedPicker(controller)
                ColorPicker(controller)
            }
        }
    }

}