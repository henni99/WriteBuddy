package com.henni.writebuddy.ui.tool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.writebuddy.model.ToolMode
import com.henni.writebuddy.tool.laser.animateLaserAlphaFloatAsState
import com.henni.writebuddy.tool.laser.rememberLaserPointerController
import com.henni.writebuddy.tool.laser.useLaserPointerMode
import com.henni.writebuddy.tool.tape.rememberTapeController
import com.henni.writebuddy.tool.tape.useTapeMode
import com.henni.writebuddy.ui.extensions.ColorPicker
import com.henni.writebuddy.ui.extensions.PaletteIconButtonWithToolTip
import com.henni.writebuddy.ui.extensions.Slider
import com.henni.writebuddy.ui.extensions.SliderRange
import com.henni.writebuddy.ui.extensions.VerticalSpacer
import writebuddy.app.generated.resources.Res
import writebuddy.app.generated.resources.ic_laser_pointer
import writebuddy.app.generated.resources.ic_tape

@Composable
fun ToolSample(
  modifier: Modifier = Modifier,
) {
  val laserPointerController = rememberLaserPointerController()

  val progress = animateLaserAlphaFloatAsState(laserPointerController) {
    laserPointerController.clearLaserPaths()
  }

  val tapeController = rememberTapeController()

  var selectedMode by remember { mutableStateOf(ToolMode.LineLaserMode) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    content = { innerPadding ->

      Box(
        modifier = modifier
          .padding(innerPadding)
          .fillMaxSize()
          .useLaserPointerMode(
            laserPointerController,
            progress,
            selectedMode == ToolMode.LineLaserMode,
          )
          .useTapeMode(tapeController, selectedMode == ToolMode.TapeMode),
      )
    },
    bottomBar = {
      Box {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color.White)
            .padding(
              horizontal = 20.dp,
            ),
          horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally,
          ),
        ) {
          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            drawableResource = Res.drawable.ic_laser_pointer,
            toolMode = ToolMode.LineLaserMode,
            onClickIcon = {
              selectedMode = ToolMode.LineLaserMode
            },
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                Text(
                  modifier = Modifier.fillMaxWidth(),
                  fontSize = 18.sp,
                  maxLines = 1,
                  color = Color.Black,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  overflow = TextOverflow.Ellipsis,
                  text = "Laser",
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  selectedColor = laserPointerController.paint.color,
                  onItemClick = {
                    laserPointerController.updateLaserColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "StrokeWidth",
                  value = laserPointerController.paint.strokeWidth,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    laserPointerController.updateLaserStrokeWidth(it)
                  },
                )
              }
            },
          )

          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            drawableResource = Res.drawable.ic_tape,
            toolMode = ToolMode.TapeMode,
            onClickIcon = {
              selectedMode = ToolMode.TapeMode
            },
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                Text(
                  modifier = Modifier.fillMaxWidth(),
                  fontSize = 18.sp,
                  maxLines = 1,
                  color = Color.Black,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  overflow = TextOverflow.Ellipsis,
                  text = "Tape",
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  selectedColor = tapeController.paint.color,
                  onItemClick = {
                    tapeController.updateTapeColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Width",
                  value = tapeController.paint.strokeWidth,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    tapeController.updateTapeWidth(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Image Width",
                  value = tapeController.imagePaint.strokeWidth,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    tapeController.updateTapeImageWidth(it)
                  },
                )
              }
            },
          )
        }
      }
    },
  )
}
