package com.henni.writebuddy.ui.extensions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.model.ToolMode
import com.henni.writebuddy.sticky.StickyType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class SliderRange {
  ONE_TO_THOUSANDS,
  ONE_TO_HUNDRED,
  ZERO_TO_ONE,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PaletteIconButtonWithToolTip(
  modifier: Modifier = Modifier,
  iconColor: Color = Color.Gray,
  type: StickyType,
  drawableResource: DrawableResource,
  onClickIcon: (StickyType) -> Unit = {},
  tooltipContent: @Composable () -> Unit,
) {
  val state = rememberTooltipState(isPersistent = true)
  val scope = rememberCoroutineScope()

  LaunchedEffect(state.isVisible) {
    if (!state.isVisible) {
      state.onDispose()
      state.dismiss()
    }
  }

  TooltipBox(
    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
    tooltip = {
      PlainTooltip(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
      ) {
        Box(
          modifier.fillMaxWidth()
            .background(
              color = Color.White,
              shape = RoundedCornerShape(5.dp),
            )
            .shadow(
              elevation = 3.dp,
              shape = RoundedCornerShape(5.dp),
            )
            .wrapContentHeight()
            .background(Color.White),
        ) {
          tooltipContent()
        }
      }
    },
    state = state,
  ) {
    Icon(
      modifier = Modifier
        .padding(4.dp)
        .combinedClickable(
          onClick = { onClickIcon(type) },
          onLongClick = {
            scope.launch {
              println("onDoubleClick: ${state.isVisible}")
              onClickIcon(type)
              state.show()
              state.onDispose()
            }
          },
        )
        .padding(4.dp)
        .size(28.dp),
      painter = painterResource(drawableResource),
      contentDescription = null,
      tint = iconColor,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PaletteIconButtonWithToolTip(
  modifier: Modifier = Modifier,
  iconColor: Color = Color.Gray,
  toolMode: ToolMode,
  drawableResource: DrawableResource,
  onClickIcon: (ToolMode) -> Unit = {},
  tooltipContent: @Composable () -> Unit,
) {
  val state = rememberTooltipState(isPersistent = true)
  val scope = rememberCoroutineScope()

  LaunchedEffect(state.isVisible) {
    if (!state.isVisible) {
      state.onDispose()
      state.dismiss()
    }
  }

  TooltipBox(
    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
    tooltip = {
      PlainTooltip(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
      ) {
        Box(
          modifier.fillMaxWidth()
            .background(
              color = Color.White,
              shape = RoundedCornerShape(5.dp),
            )
            .shadow(
              elevation = 3.dp,
              shape = RoundedCornerShape(5.dp),
            )
            .wrapContentHeight()
            .background(Color.White),
        ) {
          tooltipContent()
        }
      }
    },
    state = state,
  ) {
    Icon(
      modifier = Modifier
        .padding(4.dp)
        .combinedClickable(
          onClick = { onClickIcon(toolMode) },
          onLongClick = {
            scope.launch {
              println("onDoubleClick: ${state.isVisible}")
              onClickIcon(toolMode)
              state.show()
              state.onDispose()
            }
          },
        )
        .padding(4.dp)
        .size(28.dp),
      painter = painterResource(drawableResource),
      contentDescription = null,
      tint = iconColor,
    )
  }
}
