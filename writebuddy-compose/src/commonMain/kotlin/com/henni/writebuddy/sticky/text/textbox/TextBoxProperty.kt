package com.henni.writebuddy.sticky.text.textbox

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.writebuddy.model.defaultBorderBoxPaint
import com.henni.writebuddy.sticky.Property

/**
 * Represents the properties of a sticky text box, including size, background color, text style, padding, and border properties.
 * Used to customize the appearance and behavior of a sticky text box in the UI.
 *
 * @param size The size of the text box.
 * @param backgroundColor The background color of the text box.
 * @param textStyle The text style (font size, color, etc.) to be applied to the text inside the text box.
 * @param selectionBackgroundColor The background color when the text is selected.
 * @param padding The padding inside the text box, applied around the text.
 * @param borderBoxPaint Paint object used to draw a border around the text box.
 * @param borderBoxPadding Padding for the border box around the text box when focused.
 */

@Immutable
data class TextBoxProperty(
    val size: DpSize,
    val backgroundColor: Color,
    val textStyle: TextStyle,
    val selectionBackgroundColor: Color,
    val padding: Dp,
    val borderBoxPaint: Paint,
    val borderBoxPadding: Dp
) : Property {
    companion object {

        /**
         * Default property values for an image bitmap.
         */
        @Stable
        val DEFAULT = TextBoxProperty(
            size = DpSize(200.dp, 40.dp),
            backgroundColor = Color.Transparent,
            selectionBackgroundColor = Color.LightGray,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            padding = 8.dp,
            borderBoxPaint = defaultBorderBoxPaint(),
            borderBoxPadding = 16.dp
        )

    }
}