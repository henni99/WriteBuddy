package com.anonymous.handwriting

import android.graphics.Point
import android.graphics.Rect
import android.graphics.Region
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.core.graphics.toRect

fun createRegionFromPath(path: Path): Region {

    val boundRect = path.getBounds().toAndroidRectF().toRect()
    val boundRegion = Region()
    boundRegion.set(boundRect)

    val pathRegion = Region()
    pathRegion.setPath(path.asAndroidPath(), Region(boundRect))

    return pathRegion
}

/**
 * Path의 모든 좌표를 일정 간격으로 추출하는 함수
 *
 * @param path 대상 Path
 * @param interval 좌표를 추출할 간격 (픽셀 단위)
 * @return Path 상의 모든 좌표 리스트
 */
fun getPathCoordinates(path: Path, interval: Int = 1): Set<Rect> {
    val pathMeasure = android.graphics.PathMeasure(path.asAndroidPath(), false)
    val pathLength = pathMeasure.length
    val coordinates = mutableSetOf<Rect>()

    var distance = 0f
    val pos = FloatArray(2)

    while (distance <= pathLength) {
        if (pathMeasure.getPosTan(distance, pos, null)) {
            coordinates.add(
                Rect(
                    pos[0].toInt() - 5,
                    pos[1].toInt() - 5,
                    pos[0].toInt() + 5,
                    pos[1].toInt() + 5
                )
            )
        }
        distance += interval
    }

    return coordinates
}

fun getPathCoordinates2(path: Path, interval: Int = 1): Set<Point> {
    val pathMeasure = android.graphics.PathMeasure(path.asAndroidPath(), false)
    val pathLength = pathMeasure.length
    val coordinates = mutableSetOf<Point>()

    var distance = 0f
    val pos = FloatArray(2)

    while (distance <= pathLength) {
        if (pathMeasure.getPosTan(distance, pos, null)) {
            coordinates.add(
//                Rect(
//                    pos[0].toInt() - 1,
//                    pos[1].toInt() - 1,
//                    pos[0].toInt() + 1,
//                    pos[1].toInt() + 1
//                )
                Point(
                    pos[0].toInt(),
                    pos[1].toInt()
                )
            )
        }
        distance += interval
    }

    return coordinates
}
