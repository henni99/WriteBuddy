package com.henni.writebuddy.sticky.text.post_it

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.writebuddy.sticky.Property

/**
 * Represents the properties of a Post-It sticky note, including its size, background color,
 * text style, selection color, elevation, corner radius, and padding.
 *
 * @param size The size of the Post-It sticky note.
 * @param backgroundColor The background color of the sticky note.
 * @param textStyle The text style for the text within the sticky note, such as font size and color.
 * @param selectionBackgroundColor The background color when the sticky note is selected.
 * @param elevation The elevation (shadow) applied to the sticky note.
 * @param corner The corner radius for rounded corners of the sticky note.
 * @param padding The padding inside the sticky note to control the spacing between content and borders.
 */

@Immutable
data class PostItProperty(
    val size: DpSize,
    val backgroundColor: Color,
    val textStyle: TextStyle,
    val selectionBackgroundColor: Color,
    val elevation: Dp,
    val corner: Dp,
    val padding: Dp
): Property {

    companion object {

        @Stable
        val DEFAULT = PostItProperty(
            size = DpSize(200.dp, 100.dp),
            backgroundColor = Color.Yellow,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            selectionBackgroundColor = Color.LightGray,
            elevation = 10.dp,
            corner = 8.dp,
            padding = 8.dp
        )

    }
}