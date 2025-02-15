import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.henni.handwriting.ui.App

fun main() = application {
    Window(
        state = rememberWindowState(
            width = 1080.dp,
            height = 720.dp
        ),
        onCloseRequest = ::exitApplication,
        title = "Charts samples",
    ) {
        App()
    }
}