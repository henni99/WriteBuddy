package com.henni.writebuddy.sticky.text.textbox

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.henni.writebuddy.sticky.Sticky
import com.henni.writebuddy.sticky.text.common.StickyTextItem

/**
 * Composable function that displays a sticky text box with editable text.
 * It allows for dragging, zooming, and text editing with state updates.
 * The sticky text box's properties such as background color, text style, padding, and border styling are customizable.
 *
 * @param modifier The modifier to be applied to the sticky text box.
 * @param property The properties of the sticky text box, including its size, background color, text style, etc.
 * @param textItem The sticky text item that contains the text and other properties.
 * @param onStickyMoved Lambda to handle the movement of the sticky text box.
 * @param onTextChange Lambda to handle changes in the text inside the sticky text box.
 * @param onZoomChanged Lambda to handle zoom changes on the sticky text box.
 * @param onDeleteSticky Lambda to handle the deletion of the sticky text box.
 * @param onFocusChange Lambda to handle focus changes on the sticky text box.
 */

@Composable
fun StickyTextBox(
    modifier: Modifier = Modifier,
    property: TextBoxProperty,
    textItem: StickyTextItem,
    onStickyMoved: (Offset) -> Unit,
    onTextChange: (String) -> Unit,
    onZoomChanged: (String, Float, Offset) -> Unit,
    onDeleteSticky: (StickyTextItem) -> Unit,
    onFocusChange: (String, Boolean) -> Unit
) {
    var text by remember { mutableStateOf(textItem.text) }

    val interactionSource = remember { MutableInteractionSource() }
    val focusedState = interactionSource.collectIsFocusedAsState()

    LaunchedEffect(focusedState.value) {
        onFocusChange(textItem.id, focusedState.value)
    }

    Sticky(
        modifier = modifier,
        attachable = textItem,
        stickySize = property.size,
        onStickyMoved = { offset ->
            onStickyMoved(offset)
        },
        onZoomChanged = { scale, offset ->
            onZoomChanged(textItem.id, scale, offset)
        }
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = Color.Transparent,
                backgroundColor = property.selectionBackgroundColor
            )
        ) {
            BasicTextField(
                modifier = Modifier

                    .background(property.backgroundColor)
                    .padding(property.padding)
                    .drawBehind {
                        drawIntoCanvas { canvas ->

                            if (focusedState.value) {
                                canvas.drawRect(
                                    left = size.center.x - size.width / 2 - property.borderBoxPadding.toPx(),
                                    top = size.center.y - size.height / 2 - property.borderBoxPadding.toPx(),
                                    right = size.center.x + size.width / 2 + property.borderBoxPadding.toPx(),
                                    bottom = size.center.y + size.height / 2 + property.borderBoxPadding.toPx(),
                                    paint = property.borderBoxPaint
                                )
                            }
                        }
                    },
                interactionSource = interactionSource,
                value = text,
                textStyle = property.textStyle,
                onValueChange = {
                    text = it
                    onTextChange(it)
                }
            )
        }
    }
}
