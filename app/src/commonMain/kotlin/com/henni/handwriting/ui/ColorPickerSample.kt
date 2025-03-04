package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun ColorPicker(
  controller: HandwritingController,
  modifier: Modifier = Modifier,
  list: List<Color> = defaultColorList(),
) {
  Text(
    modifier = Modifier
      .padding(start = 8.dp),
    text = "Color",
    fontSize = 18.sp,
  )

  LazyRow(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    items(list) { color ->
      Box(
        modifier = Modifier
          .padding(8.dp)
          .size(48.dp)
          .background(
            color = color,
            shape = RoundedCornerShape(8.dp),
          )
          .clickable { controller.setPenColor(color) },
      )
    }
  }
}

fun defaultColorList(): List<Color> = listOf(
  Color(0xFFff6b6b), // Soft red
  Color(0xFFff4f4f), // Intense red
  Color(0xFFc0392b), // Dark red
  Color(0xFFff914d), // Warm orange
  Color(0xFFf3e76e), // Light yellow
  Color(0xFFf1c40f), // Bright yellow
  Color(0xFFf39c12), // Medium yellow
  Color(0xFFd35400), // Deep orange
  Color(0xFF2ecc71), // Fresh green
  Color(0xFF27ae60), // Dark green
  Color(0xFF1abc9c), // Teal
  Color(0xFF16a085), // Dark teal
  Color(0xFF3498db), // Light blue
  Color(0xFF2980b9), // Dark blue
  Color(0xFF8e44ad), // Purple
  Color(0xFF9b59b6), // Lavender
  Color(0xFFdcdcdc), // Light gray
  Color(0xFF95a5a6), // Medium gray
  Color(0xFF7f8c8d), // Dark gray
  Color(0xFF34495e), // Dark teal
  Color(0xFF2c3e50), // Deep blue
)
