package com.henni.handwriting.extensions

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter

actual fun Modifier.pointerInteropFilter(onTouchEvent: (MotionEvent) -> Boolean): Modifier =
    this.pointerInteropFilterCommon { event -> onTouchEvent.invoke(event) }
