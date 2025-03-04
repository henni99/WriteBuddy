package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.HandwritingController
import com.henni.handwriting.model.ToolMode

@Composable
fun ToolPicker(
  controller: HandwritingController,
  modifier: Modifier = Modifier,
  list: List<ToolMode> = defaultToolList(),
) {
  Text(
    modifier = Modifier
      .padding(start = 8.dp),
    text = "Tool",
    fontSize = 18.sp,
  )

  LazyRow(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    items(list) { tool ->
      Box(
        modifier = Modifier
          .padding(8.dp)
          .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
          .background(
            color = Color.White,
            shape = RoundedCornerShape(8.dp),
          )
          .clickable { controller.setToolMode(tool) }
          .padding(8.dp),
      ) {
        when (tool) {
          ToolMode.PenMode -> {
            Text("Pen")
          }

          ToolMode.EraserMode -> {
            Text("Eraser")
          }

          ToolMode.LassoSelectMode -> {
            Text("Lasso")
          }

          else -> {}
        }
      }
    }
  }
}

fun defaultToolList(): List<ToolMode> {
  return listOf(
    ToolMode.PenMode,
    ToolMode.EraserMode,
    ToolMode.LassoSelectMode,
  )
}
