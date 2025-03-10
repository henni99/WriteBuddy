package com.henni.handwriting.extension

import com.henni.handwriting.model.HandwritingPath

internal fun MutableSet<HandwritingPath>.findId(id: String): Boolean = (this.find { it.id == id } != null)
