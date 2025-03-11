package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.HandWritingNote
import com.henni.handwriting.animateLaserAlphaFloatAsState
import com.henni.handwriting.model.ToolMode
import com.henni.handwriting.rememberHandwritingController
import com.henni.handwriting.ui.extensions.HandWritingColorPicker
import com.henni.handwriting.ui.extensions.HandWritingSlider
import com.henni.handwriting.ui.extensions.HandWritingSwitch
import com.henni.handwriting.ui.extensions.PaletteIconButton
import com.henni.handwriting.ui.extensions.PaletteIconButtonWithToolTip
import com.henni.handwriting.ui.extensions.SliderRange
import com.henni.handwriting.ui.extensions.VerticalSpacer
import handwriting.app.generated.resources.Res
import handwriting.app.generated.resources.ic_eraser
import handwriting.app.generated.resources.ic_laser_pointer
import handwriting.app.generated.resources.ic_lasso
import handwriting.app.generated.resources.ic_pen
import handwriting.app.generated.resources.ic_redo
import handwriting.app.generated.resources.ic_undo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HandWritingSample() {
  val controller = rememberHandwritingController()

  val laserState = animateLaserAlphaFloatAsState(controller)

  LaunchedEffect(Unit) {
    val imageBitmap = Res.readBytes("drawable/img_lined_paper.png").decodeToImageBitmap()
    controller.updateContentBackgroundImageBitmap(imageBitmap, ContentScale.None)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    content = { innerPadding ->
      Column(
        modifier = Modifier.fillMaxSize(),
      ) {
        HandWritingNote(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
          controller = controller,
          laserState = laserState,
          containerBackgroundColor = Color.LightGray,
          contentWidthRatio = 0.9f,
          contentHeightRatio = 0.9f,
        )
      }
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
            iconColor = controller.penColor,
            toolMode = ToolMode.PenMode,
            drawableResource = Res.drawable.ic_pen,
            onClickIcon = controller::setToolMode,
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                HandWritingSlider(
                  title = "StrokeWidth",
                  value = controller.penStrokeWidth,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = controller::updatePenStrokeWidth,
                )

                VerticalSpacer(12.dp)

                HandWritingColorPicker(
                  selectedColor = controller.penColor,
                  onItemClick = {
                    controller.updatePenColor(it)
                  },
                )
              }
            },
          )

          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            drawableResource = Res.drawable.ic_eraser,
            toolMode = ToolMode.EraserMode,
            onClickIcon = controller::setToolMode,
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                HandWritingSlider(
                  title = "Eraser Radius",
                  value = controller.eraserPointRadius,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    controller.updateEraserPointRadius(it)
                  },
                )

                VerticalSpacer(12.dp)

                HandWritingSwitch(
                  checked = controller.isEraserPointShowed,
                  title = "Eraser Pointer Visibility",
                  onCheckedChange = {
                    controller.updateIsEraserPointShowed(it)
                  },
                )

                VerticalSpacer(12.dp)

                HandWritingColorPicker(
                  title = "Eraser Point Color",
                  selectedColor = controller.eraserPointColor,
                  onItemClick = {
                    controller.updateEraserPointColor(it)
                  },
                )

                VerticalSpacer(12.dp)

                Button(
                  colors = ButtonDefaults.buttonColors(Color.LightGray),
                  shape = RoundedCornerShape(6.dp),
                  modifier = Modifier
                    .fillMaxWidth(),
                  onClick = {
                    controller.clearAllHandWritingPaths()
                  },
                ) {
                  Text(
                    modifier = Modifier
                      .fillMaxWidth(),
                    fontSize = 14.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    text = "Clear All",
                  )
                }
              }
            },
          )

          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            iconColor = controller.lassoColor,
            drawableResource = Res.drawable.ic_lasso,
            toolMode = ToolMode.LassoSelectMode,
            onClickIcon = controller::setToolMode,
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                HandWritingColorPicker(
                  title = "Lasso Color",
                  selectedColor = controller.lassoColor,
                  onItemClick = {
                    controller.updateLassoColor(it)
                  },
                )

                VerticalSpacer(12.dp)

                HandWritingColorPicker(
                  title = "Lasso BoundBox Color",
                  selectedColor = controller.lassoBoundBoxColor,
                  onItemClick = {
                    controller.updateLassoBoundBoxColor(it)
                  },
                )
              }
            },
          )

          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            iconColor = controller.laserColor,
            drawableResource = Res.drawable.ic_laser_pointer,
            toolMode = ToolMode.LineLaserMode,
            onClickIcon = controller::setToolMode,
            tooltipContent = {
              Column(
                modifier = Modifier
                  .padding(16.dp),
              ) {
                HandWritingColorPicker(
                  selectedColor = controller.laserColor,
                  onItemClick = controller::updateLaserColor,
                )
              }
            },
          )

          PaletteIconButton(
            drawableResource = Res.drawable.ic_undo,
            onClickIcon = {
              controller.undo()
            },
            iconColor = when (controller.canUndo) {
              true -> Color.Red
              false -> Color.Gray
            },
          )

          PaletteIconButton(
            drawableResource = Res.drawable.ic_redo,
            onClickIcon = {
              controller.redo()
            },
            iconColor = when (controller.canRedo) {
              true -> Color.Red
              false -> Color.Gray
            },
          )
        }
      }
    },
  )
}

// ToolPicker(controller)
//        EnhancedPicker(controller)
//        ColorPicker(controller)
