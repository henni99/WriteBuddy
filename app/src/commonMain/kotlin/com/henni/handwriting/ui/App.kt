package com.henni.handwriting.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App() {
  Box(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    HandWritingSample()
  }
}
