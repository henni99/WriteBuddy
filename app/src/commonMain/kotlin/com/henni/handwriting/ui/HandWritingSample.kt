package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.HandWritingNote
import com.henni.handwriting.model.Padding
import com.henni.handwriting.model.ToolMode
import com.henni.handwriting.rememberHandwritingController
import com.henni.handwriting.ui.extensions.HandWritingSlider
import com.henni.handwriting.ui.extensions.VerticalSpacer
import handwriting.app.generated.resources.Res
import handwriting.app.generated.resources.ic_eraser
import handwriting.app.generated.resources.ic_lasso
import handwriting.app.generated.resources.ic_pen


@Composable
fun HandWritingSample() {
    val controller = rememberHandwritingController {
        isZoomable = true
        isEraserPointShowed = true
        eraserPointRadius = 20f
        lassoBoundBoxPadding = Padding(20, 20, 20, 20)
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
                        .background(Color.LightGray)
                        .padding(innerPadding),
                    controller = controller,
                    contentWidthRatio = 0.9f,
                    contentHeightRatio = 0.9f
                )
            }
        },
        bottomBar = {

            Box {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .background(Color.White)
                        .padding(
                            horizontal = 20.dp,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        PaletteIconButtonWithToolTip(
                            modifier = Modifier.align(Alignment.Center),
                            iconColor = controller.penPaint.color,
                            toolMode = ToolMode.PenMode,
                            drawableResource = Res.drawable.ic_pen,
                            onClickIcon = controller::setToolMode,
                            tooltipContent = {

                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {

                                    HandWritingSlider(
                                        title = "Alpha",
                                        value = controller.currentPaint.alpha,
                                        sliderRange = SliderRange.ZERO_TO_ONE,
                                        onValueChangeFinished = controller::setPenAlpha
                                    )

                                    VerticalSpacer(12.dp)

                                    HandWritingSlider(
                                        title = "StrokeWidth",
                                        value = controller.currentPaint.strokeWidth,
                                        sliderRange = SliderRange.ONE_TO_HUNDRED,
                                        onValueChangeFinished = controller::setPenStrokeWidth
                                    )

                                    VerticalSpacer(12.dp)

                                    HandWritingColorPicker(
                                        selectedColor = controller.penPaint.color,
                                        onItemClick = controller::setPenColor
                                    )
                                }
                            }
                        )
                    }

                    item {

                        PaletteIconButtonWithToolTip(
                            modifier = Modifier.align(Alignment.Center),
                            drawableResource = Res.drawable.ic_eraser,
                            toolMode = ToolMode.EraserMode,
                            onClickIcon = controller::setToolMode,
                            tooltipContent = {

                                Column(
                                    modifier = Modifier
                                        .padding(
                                            top = 16.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                ) {


                                    HandWritingSlider(
                                        title = "Eraser Radius",
                                        value = controller.eraserPointRadius,
                                        sliderRange = SliderRange.ONE_TO_HUNDRED,
                                        onValueChangeFinished = {
                                            controller.eraserPointRadius = it
                                        }
                                    )

                                    VerticalSpacer(12.dp)

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = Color.Black
                                    )

                                    Button(
                                        colors = ButtonDefaults.buttonColors().copy(
                                            containerColor = Color.White,

                                            ),
                                        shape = RectangleShape,
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
                                            text = "Clear All"
                                        )
                                    }

                                }
                            }
                        )
                    }


                    item {
                        PaletteIconButtonWithToolTip(
                            modifier = Modifier.align(Alignment.Center),
                            drawableResource = Res.drawable.ic_lasso,
                            toolMode = ToolMode.LassoSelectMode,
                            onClickIcon = controller::setToolMode,
                            tooltipContent = {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .align(Alignment.Center)
                                        .height(200.dp),
                                    text = "lasso"
                                )
                            }
                        )
                    }

                    item {
                        PaletteIconButtonWithToolTip(
                            modifier = Modifier.align(Alignment.Center),
                            drawableResource = Res.drawable.ic_lasso,
                            toolMode = ToolMode.LineLaserMode,
                            onClickIcon = controller::setToolMode,
                            tooltipContent = {


                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .align(Alignment.Center)
                                        .height(200.dp),
                                    text = "laser pointer"
                                )
                            }
                        )
                    }

                    item {
                        PaletteIconButton(
                            drawableResource = Res.drawable.ic_lasso,
                            onClickIcon = {
                                controller.undo()
                            },
                            iconColor = when(controller.canUndo) {
                                true -> Color.Red
                                false -> Color.Gray
                            }
                        )
                    }

                    item {
                        PaletteIconButton(
                            drawableResource = Res.drawable.ic_lasso,
                            onClickIcon = {
                                controller.redo()
                            },
                            iconColor = when(controller.canRedo) {
                                true -> Color.Red
                                false -> Color.Gray
                            }
                        )
                    }

                }
            }
        }
    )


}


//ToolPicker(controller)
//        EnhancedPicker(controller)
//        ColorPicker(controller)