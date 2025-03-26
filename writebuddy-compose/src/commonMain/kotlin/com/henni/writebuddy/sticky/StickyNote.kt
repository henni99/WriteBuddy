package com.henni.writebuddy.sticky

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.henni.writebuddy.sticky.image.StickyImage
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapItem
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapProperty
import com.henni.writebuddy.sticky.image.imageVector.VectorImageItem
import com.henni.writebuddy.sticky.image.imageVector.VectorImageProperty
import com.henni.writebuddy.sticky.image.painter.PainterImageItem
import com.henni.writebuddy.sticky.image.painter.PainterImageProperty
import com.henni.writebuddy.sticky.text.common.StickyTextItem
import com.henni.writebuddy.sticky.text.post_it.PostItProperty
import com.henni.writebuddy.sticky.text.post_it.StickyPostIt
import com.henni.writebuddy.sticky.text.textbox.StickyTextBox
import com.henni.writebuddy.sticky.text.textbox.TextBoxProperty
import kotlinx.coroutines.launch

/**
 * A composable function that displays a collection of sticky notes (such as text, images, etc.)
 * and allows for interactions like moving, zooming, and editing them.
 *
 * @param modifier Modifier to apply additional styling or behavior to the composable.
 * @param controller The controller responsible for managing the state and logic of the sticky items.
 */

@Composable
fun StickyNote(
    modifier: Modifier = Modifier,
    controller: StickyItemController
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            controller.invalidateTick.collect {
                focusManager.clearFocus()
            }
        }
    }

    Box(
        modifier = modifier
            .useStickyMode(controller)
    ) {
        controller.stickyItemList.forEach { item ->
            when (item.type) {
                StickyType.POST_IT -> {
                    StickyPostIt(
                        property = item.property as PostItProperty,
                        textItem = item as StickyTextItem,
                        onStickyMoved = { offset ->
                            controller.updateTranslate(item.id, offset)
                        },
                        onTextChange = { text ->
                            controller.updateText(item.id, text)
                        },
                        onDeleteSticky = { id ->
                            controller.removeStickyItem(id)
                        },
                        onFocusChange = { id, isFocus ->
                            controller.updateFocus(id, isFocus)
                        },
                        onZoomChanged = { id, scale, offset ->
                            controller.updateZoomState(id, scale, offset)
                        }
                    )
                }

                StickyType.TEXT_BOX -> {
                    StickyTextBox(
                        property = item.property as TextBoxProperty,
                        textItem = item as StickyTextItem,
                        onStickyMoved = { offset ->
                            controller.updateTranslate(item.id, offset)
                        },
                        onTextChange = { text ->
                            controller.updateText(item.id, text)
                        },
                        onDeleteSticky = { id ->
                            controller.removeStickyItem(id)
                        },
                        onZoomChanged = { id, scale, offset ->
                            controller.updateZoomState(id, scale, offset)
                        },
                        onFocusChange = { id, isFocus ->
                            controller.updateFocus(id, isFocus)
                        },
                    )
                }

                StickyType.PAINTER_IMAGE -> {
                    StickyImage(
                        property = item.property as PainterImageProperty,
                        image = item as PainterImageItem,
                        onStickyMoved = { offset ->
                            controller.updateTranslate(item.id, offset)
                        },
                        onZoomChanged = { id, scale, offset ->
                            controller.updateZoomState(id, scale, offset)
                        },
                        onDeleteSticky = { sticky ->
                            controller.removeStickyItem(sticky)
                        }
                    )
                }

                StickyType.VECTOR_IMAGE -> {
                    StickyImage(
                        property = item.property as VectorImageProperty,
                        image = item as VectorImageItem,
                        onStickyMoved = { offset ->
                            controller.updateTranslate(item.id, offset)
                        },
                        onZoomChanged = { id, scale, offset ->
                            controller.updateZoomState(id, scale, offset)
                        },
                        onDeleteSticky = { sticky ->
                            controller.removeStickyItem(sticky)
                        }
                    )
                }

                StickyType.BITMAP_IMAGE -> {
                    StickyImage(
                        property = item.property as ImageBitmapProperty,
                        image = item as ImageBitmapItem,
                        onStickyMoved = { offset ->
                            controller.updateTranslate(item.id, offset)
                        },
                        onZoomChanged = { id, scale, offset ->
                            controller.updateZoomState(id, scale, offset)
                        },
                        onDeleteSticky = { sticky ->
                            controller.removeStickyItem(sticky)
                        }
                    )
                }

            }
        }
    }
}