package com.henni.handwriting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.henni.handwriting.ui.App
import com.henni.handwriting.ui.theme.HandWritingTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      HandWritingTheme(false) {
        App()
      }
    }
  }
}
