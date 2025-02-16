package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.kmp.HandwritingController

@Composable
fun EnhancedPicker(
    controller: HandwritingController,
    modifier: Modifier = Modifier,
    list: List<Float> = defaultStrokeWidthList()
) {

    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Text(
        modifier = Modifier
            .padding(start = 8.dp),
        text = "Enhanced",
        fontSize = 18.sp
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .clickable { controller.undo() }
            ) {

                Text(
                    text = "Undo",
                    color = if (controller.canUndo.value) Color.Black else Color.Red
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .clickable { controller.redo() }
            ) {
                Text(
                    text = "Redo",
                    color = if (controller.canRedo.value) Color.Black else Color.Red
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .clickable { isDropDownMenuExpanded = true }
            ) {
                Text(
                    text = "ChangeStroke",
                    color = Color.Black
                )

                if (isDropDownMenuExpanded) {
                    DropdownMenu(
                        modifier = Modifier
                            .wrapContentSize(),
                        expanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false }
                    ) {

                        list.forEach {
                            DropdownMenuItem(
                                text = { Text("${it}") },
                                onClick = {
                                    controller.setPenStrokeWidth(it)
                                    isDropDownMenuExpanded = false
                                })
                        }
                    }
                }
            }
        }

    }



}

fun defaultEnhancedFunctionList(): List<String> {
    return listOf(
        "Undo",
        "Redo",
        "Clear",
        "StrokeChange"
    )
}

fun defaultStrokeWidthList(): List<Float> {
    return listOf(
        5f,
        10f,
        15f,
        20f,
        25f
    )
}