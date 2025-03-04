package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.henni.handwriting.HandWritingNote
import com.henni.handwriting.model.Padding
import com.henni.handwriting.rememberHandwritingController

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
        .padding(innerPadding),

    ) {
      item {
        HandWritingNote(
          modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(Color.LightGray)
            .padding(innerPadding),
          controller = controller,
          contentWidth = 300.dp,
          contentHeight = 300.dp,
        )

        ToolPicker(controller)
        EnhancedPicker(controller)
        ColorPicker(controller)
      }
    }
  }
}
