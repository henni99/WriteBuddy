package com.henni.writebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.henni.writebuddy.ui.App
import com.henni.writebuddy.ui.theme.HandWritingTheme

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
