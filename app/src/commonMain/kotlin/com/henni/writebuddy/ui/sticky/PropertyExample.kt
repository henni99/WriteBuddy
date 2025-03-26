package com.henni.writebuddy.ui.sticky

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.sticky.image.imageVector.VectorImageProperty
import com.henni.writebuddy.sticky.image.painter.PainterImageProperty
import org.jetbrains.compose.resources.painterResource
import writebuddy.app.generated.resources.Res
import writebuddy.app.generated.resources.ic_launcher

@Composable
fun PainterImagePropertyExample(): PainterImageProperty {
    return PainterImageProperty.DEFAULT.copy(
        painter = painterResource(Res.drawable.ic_launcher)
    )
}

@Composable
fun VectorImagePropertyExample(): VectorImageProperty {
    val density = LocalDensity.current.density

    return VectorImageProperty.DEFAULT.copy(
        imageVector = ImageVector.Builder(
            name = "CustomIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 200 * density,
            viewportHeight = 200 * density
        ).apply {
            addPath(
                pathData = PathBuilder().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, 200f * density)
                    lineTo(200f * density, 200f * density)
                    lineTo(200f * density, 0f)
                    close()
                }.nodes,
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 0.0f,
                strokeLineWidth = 0.0f,
                strokeLineMiter = 0.0f,
                pathFillType = PathFillType.NonZero
            )
        }.build()
    )
}