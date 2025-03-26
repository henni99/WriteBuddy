package com.henni.writebuddy.ui.sticky

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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.henni.writebuddy.sticky.StickyNote
import com.henni.writebuddy.sticky.StickyType
import com.henni.writebuddy.sticky.rememberStickyItemController
import com.henni.writebuddy.ui.extensions.ColorPicker
import com.henni.writebuddy.ui.extensions.PaletteIconButtonWithToolTip
import com.henni.writebuddy.ui.extensions.Slider
import com.henni.writebuddy.ui.extensions.SliderRange
import com.henni.writebuddy.ui.extensions.VerticalSpacer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import writebuddy.app.generated.resources.Res
import writebuddy.app.generated.resources.ic_image
import writebuddy.app.generated.resources.ic_post_it
import writebuddy.app.generated.resources.ic_text_box

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StickySample(
  modifier: Modifier = Modifier,
) {
  val controller = rememberStickyItemController(
    painterImageProperty = PainterImagePropertyExample(),
    vectorImageProperty = VectorImagePropertyExample(),
  )

  LaunchedEffect(Unit) {
    controller.updateBitmapImageImageBitmap(
      Res.readBytes("drawable/ic_launcher.webp").decodeToImageBitmap(),
    )
  }

  var selectedIndex by remember { mutableStateOf(-1) }
  val options = listOf("Painter", "Vector", "ImageBitmap")

  Scaffold(
    modifier = modifier.fillMaxSize(),
    content = { innerPadding ->

      StickyNote(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize(),
        controller = controller,
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
            drawableResource = Res.drawable.ic_post_it,
            type = StickyType.POST_IT,
            onClickIcon = controller::updateStickyType,
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
                  text = "PostIt",
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  title = "Background Color",
                  selectedColor = controller.postItProperty.backgroundColor,
                  onItemClick = {
                    controller.updatePostItBackgroundColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Width",
                  value = controller.postItProperty.size.width.value,
                  sliderRange = SliderRange.ONE_TO_THOUSANDS,
                  onValueChangeFinished = {
                    controller.updatePostItWidth(it.dp)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Height",
                  value = controller.postItProperty.size.height.value,
                  sliderRange = SliderRange.ONE_TO_THOUSANDS,
                  onValueChangeFinished = {
                    controller.updatePostItHeight(it.dp)
                  },
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  title = "Text Color",
                  selectedColor = controller.postItProperty.textStyle.color,
                  onItemClick = {
                    controller.updatePostItTextColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Font Size",
                  value = controller.postItProperty.textStyle.fontSize.value,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    controller.updatePostItFontSize(it)
                  },
                )
              }
            },
          )

          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            drawableResource = Res.drawable.ic_text_box,
            type = StickyType.TEXT_BOX,
            onClickIcon = controller::updateStickyType,
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
                  text = "TextBox",
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  title = "Background Color",
                  selectedColor = controller.textBoxProperty.backgroundColor,
                  onItemClick = {
                    controller.updateTextBoxBackgroundColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                ColorPicker(
                  title = "Text Color",
                  selectedColor = controller.textBoxProperty.textStyle.color,
                  onItemClick = {
                    controller.updateTextBoxTextColor(it)
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Font Size",
                  value = controller.textBoxProperty.textStyle.fontSize.value,
                  sliderRange = SliderRange.ONE_TO_HUNDRED,
                  onValueChangeFinished = {
                    controller.updateTextBoxFontSize(it)
                  },
                )
              }
            },
          )
          PaletteIconButtonWithToolTip(
            modifier = Modifier,
            drawableResource = Res.drawable.ic_image,
            type = StickyType.PAINTER_IMAGE,
            onClickIcon = { },
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
                  text = "Image",
                )

                VerticalSpacer(16.dp)

                SingleChoiceSegmentedButtonRow(
                  modifier = Modifier
                    .fillMaxWidth(),
                ) {
                  options.forEachIndexed { index, label ->
                    SegmentedButton(
                      shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size,
                      ),
                      onClick = {
                        selectedIndex = index
                        when (selectedIndex) {
                          0 -> controller.updateStickyType(StickyType.PAINTER_IMAGE)
                          1 -> controller.updateStickyType(StickyType.VECTOR_IMAGE)
                          2 -> controller.updateStickyType(StickyType.BITMAP_IMAGE)
                        }
                      },
                      selected = index == selectedIndex,
                    ) {
                      Text(label)
                    }
                  }
                }

                VerticalSpacer(16.dp)

                Slider(
                  title = "Width",
                  value = when (selectedIndex) {
                    0 -> controller.postItProperty.size.width.value
                    1 -> controller.vectorImageProperty.size.width.value
                    2 -> controller.bitmapImageProperty.size.width.value
                    else -> 0f
                  },
                  sliderRange = SliderRange.ONE_TO_THOUSANDS,
                  onValueChangeFinished = {
                    when (selectedIndex) {
                      0 -> controller.updatePainterImageWidth(it.dp)
                      1 -> controller.updateVectorImageWidth(it.dp)
                      2 -> controller.updateBitmapImageWidth(it.dp)
                      else -> {}
                    }
                  },
                )

                VerticalSpacer(16.dp)

                Slider(
                  title = "Height",
                  value = when (selectedIndex) {
                    0 -> {
                      println("selectedIndex: $selectedIndex ${controller.postItProperty.size.height.value}")
                      controller.postItProperty.size.height.value
                    }

                    1 -> controller.vectorImageProperty.size.height.value
                    2 -> controller.bitmapImageProperty.size.height.value
                    else -> 0f
                  },
                  sliderRange = SliderRange.ONE_TO_THOUSANDS,
                  onValueChangeFinished = {
                    when (selectedIndex) {
                      0 -> controller.updatePainterImageHeight(it.dp)
                      1 -> controller.updateVectorImageHeight(it.dp)
                      2 -> controller.updateBitmapImageHeight(it.dp)
                      else -> {}
                    }
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
