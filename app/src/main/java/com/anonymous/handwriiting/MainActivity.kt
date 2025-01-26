package com.anonymous.handwriiting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anonymous.handwriiting.ui.theme.HandwritingTheme
import com.anonymous.handwriting.HandWritingMode
import com.anonymous.handwriting.HandWritingNote
import com.anonymous.handwriting.rememberHandwritingState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val state= rememberHandwritingState()
            HandwritingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)

                    ) {

                        HandWritingNote(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .background(Color.Gray)
                                .padding(innerPadding),
                            controller = state
                        )

                        Row {

                            Button(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    state.setCurrentMode(HandWritingMode.PEN)
                                }
                            ) {
                                Text("펜")
                            }

                            Button(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    state.setCurrentMode(HandWritingMode.ERASER)
                                }
                            ) {
                                Text("지")
                            }



                            Button(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    state.undo()
                                }
                            ) {
                                Text("U")
                            }

                            Button(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    state.redo()
                                }
                            ) {
                                Text("R")
                            }

                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HandwritingTheme {
        Greeting("Android")
    }
}