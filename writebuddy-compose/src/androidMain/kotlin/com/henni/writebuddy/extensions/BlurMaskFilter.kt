package com.henni.writebuddy.extensions

import android.graphics.BlurMaskFilter
import androidx.compose.ui.graphics.NativePaint

internal actual fun NativePaint.setMaskFilter(blurRadius: Float) {
  this.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
}
