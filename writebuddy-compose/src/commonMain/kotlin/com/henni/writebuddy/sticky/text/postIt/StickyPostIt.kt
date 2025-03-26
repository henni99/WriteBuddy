package com.henni.writebuddy.sticky.text.postIt

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.henni.writebuddy.sticky.Sticky
import com.henni.writebuddy.sticky.text.common.StickyTextItem

/**
 * Composable function that displays a sticky note (Post-It) with editable text.
 * It allows for dragging, zooming, and text editing with state updates.
 * The sticky note's properties like background color, text style, size, and padding are customizable.
 *
 * @param modifier The modifier to be applied to the sticky note.
 * @param property The properties of the sticky note, including its size, background color, text style, etc.
 * @param textItem The sticky text item that contains the text and other properties.
 * @param onStickyMoved Lambda to handle the movement of the sticky note.
 * @param onTextChange Lambda to handle changes in the text inside the sticky note.
 * @param onDeleteSticky Lambda to handle the deletion of the sticky note.
 * @param onFocusChange Lambda to handle focus changes on the sticky note.
 * @param onZoomChanged Lambda to handle zoom changes on the sticky note.
 */

@Composable
fun StickyPostIt(
  modifier: Modifier = Modifier,
  property: PostItProperty,
  textItem: StickyTextItem,
  onStickyMoved: (Offset) -> Unit,
  onTextChange: (String) -> Unit,
  onDeleteSticky: (StickyTextItem) -> Unit,
  onFocusChange: (String, Boolean) -> Unit,
  onZoomChanged: (String, Float, Offset) -> Unit,
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
    },
  ) {
    CompositionLocalProvider(
      LocalTextSelectionColors provides TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = property.selectionBackgroundColor,
      ),
    ) {
      BasicTextField(
        modifier = Modifier
          .shadow(property.elevation)
          .size(property.size)
          .background(
            color = property.backgroundColor,
            shape = RoundedCornerShape(property.corner),
          )
          .padding(property.padding),
        interactionSource = interactionSource,
        value = text,
        textStyle = property.textStyle,
        onValueChange = {
          text = it
          onTextChange(it)
        },
      )
    }
  }
}
