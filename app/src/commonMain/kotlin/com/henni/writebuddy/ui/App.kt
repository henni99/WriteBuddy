package com.henni.writebuddy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.henni.writebuddy.ui.root.WriteBuddySample

@Composable
fun App() {
  Box(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    WriteBuddySample()
  }
}
