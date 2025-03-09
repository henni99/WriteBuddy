package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.ui.extensions.VerticalSpacer

@Composable
fun HandWritingColorPicker(
    modifier: Modifier = Modifier,
    list: List<Color> = defaultColorList(),
    selectedColor: Color,
    onItemClick: (Color) -> Unit = { },
) {
//    var currentColor by remember { mutableStateOf(Color.Black) }
//
//    LaunchedEffect(Unit) {
//        currentColor = selectedColor
//
//    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        fontSize = 14.sp,
        maxLines = 1,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        text = "Color"
    )

    VerticalSpacer(8.dp)

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(list) { color ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable {
//                        currentColor = color
                        onItemClick(color)
                    },
            ) {

                if (color == selectedColor) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

fun defaultColorList(): List<Color> = listOf(
    Color(0xFFff6b6b), // Soft red
    Color(0xFFff4f4f), // Intense red
    Color(0xFFc0392b), // Dark red
    Color(0xFFff914d), // Warm orange
    Color(0xFFf3e76e), // Light yellow
    Color(0xFFf1c40f), // Bright yellow
    Color(0xFFf39c12), // Medium yellow
    Color(0xFFd35400), // Deep orange
    Color(0xFF2ecc71), // Fresh green
    Color(0xFF27ae60), // Dark green
    Color(0xFF1abc9c), // Teal
    Color(0xFF16a085), // Dark teal
    Color(0xFF3498db), // Light blue
    Color(0xFF2980b9), // Dark blue
    Color(0xFF8e44ad), // Purple
    Color(0xFF9b59b6), // Lavender
    Color(0xFFdcdcdc), // Light gray
    Color(0xFF95a5a6), // Medium gray
    Color(0xFF7f8c8d), // Dark gray
    Color(0xFF34495e), // Dark teal
    Color(0xFF2c3e50), // Deep blue,
    Color(0xFF000000), // Deep blue
)
