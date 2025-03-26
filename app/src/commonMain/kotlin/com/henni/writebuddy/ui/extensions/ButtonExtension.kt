package com.henni.writebuddy.ui.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PaletteIconButton(
  modifier: Modifier = Modifier,
  iconColor: Color = Color.Gray,
  drawableResource: DrawableResource,
  onClickIcon: () -> Unit = {},
) {
  Icon(
    modifier = modifier
      .padding(4.dp)
      .clickable {
        onClickIcon()
      }
      .padding(4.dp)
      .size(28.dp),
    painter = painterResource(drawableResource),
    contentDescription = null,
    tint = iconColor,
  )
}
