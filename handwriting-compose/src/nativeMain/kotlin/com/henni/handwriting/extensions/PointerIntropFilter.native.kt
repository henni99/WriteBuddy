package com.henni.handwriting.extensions

import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.*
import platform.CoreGraphics.*

actual fun Modifier.pointerInteropFilter(onTouchEvent: (MotionEvent) -> Boolean): Modifier =
    this.pointerInteropFilterCommon { event -> onTouchEvent.invoke(event) }

