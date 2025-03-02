package com.henni.handwriting.extension

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Extension function for [MutableState] to update the tick value.
 * Increments the current value by 1 and wraps around at `Int.MAX_VALUE`.
 */

internal inline fun MutableStateFlow<Int>.updateTick() {
    update {
        (it + 1) % Int.MAX_VALUE
    }
}

/**
 * Extension function for [MutableState] to update the tick value.
 * Increments the current value by 1 and wraps around at `Int.MAX_VALUE`.
 */

internal inline fun MutableState<Int>.updateTick() {
    this.value = (this.value + 1) % Int.MAX_VALUE
}