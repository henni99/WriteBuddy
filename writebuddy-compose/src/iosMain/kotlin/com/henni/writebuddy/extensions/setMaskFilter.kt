package com.henni.writebuddy.extensions

import androidx.compose.ui.graphics.NativePaint
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

internal actual fun NativePaint.setMaskFilter(blurRadius: Float) {
  this.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blurRadius / 2, true)
}
