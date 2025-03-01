package com.henni.handwriting.kmp.extension

import com.henni.handwriting.kmp.model.HandwritingPath

internal inline fun MutableSet<HandwritingPath>.findId(id: String): Boolean {
    return (this.find { it.id == id } != null)
}