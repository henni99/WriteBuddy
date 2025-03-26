package com.henni.writebuddy.ui.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.ui.sticky.StickySample
import com.henni.writebuddy.ui.tool.ToolSample
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WriteBuddySample() {
  var selectedIndex by remember { mutableStateOf(0) }

  val options = listOf("Sticky", "Tool")

  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    if (selectedIndex == 0) {
      StickySample()
    } else {
      ToolSample()
    }

    SingleChoiceSegmentedButtonRow(
      modifier = Modifier
        .width(180.dp)
        .offset {
          IntOffset(
            x = 0,
            y = 40.dp.toPx().toInt(),
          )
        }
        .align(Alignment.TopCenter),
    ) {
      options.forEachIndexed { index, label ->
        SegmentedButton(
          shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
          onClick = { selectedIndex = index },
          selected = index == selectedIndex,
        ) {
          Text(label)
        }
      }
    }
  }
}
