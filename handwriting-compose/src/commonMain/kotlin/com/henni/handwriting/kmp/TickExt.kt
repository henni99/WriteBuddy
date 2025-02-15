package com.henni.handwriting.kmp

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun MutableStateFlow<Int>.updateTick() {
    update {
        (it + 1) % Int.MAX_VALUE
    }
}

fun MutableState<Int>.updateTick() {
    this.value = (this.value + 1) % Int.MAX_VALUE
}