package com.henni.handwriting.kmp.tool

import com.henni.handwriting.kmp.model.ToolMode

fun isLassoUsed(toolMode: ToolMode): Boolean {
    return toolMode == ToolMode.LassoSelectMode || toolMode == ToolMode.LassoMoveMode
}