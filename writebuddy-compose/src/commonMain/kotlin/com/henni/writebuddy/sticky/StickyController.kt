package com.henni.writebuddy.sticky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.sp
import com.henni.writebuddy.Attachable
import com.henni.writebuddy.extension.updateTick
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapProperty
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapTouchEvent
import com.henni.writebuddy.sticky.image.imageVector.VectorImageProperty
import com.henni.writebuddy.sticky.image.imageVector.VectorImageTouchEvent
import com.henni.writebuddy.sticky.image.painter.PainterImageProperty
import com.henni.writebuddy.sticky.image.painter.PainterImageTouchEvent
import com.henni.writebuddy.sticky.text.common.StickyTextItem
import com.henni.writebuddy.sticky.text.postIt.PostItProperty
import com.henni.writebuddy.sticky.text.postIt.PostItTouchEvent
import com.henni.writebuddy.sticky.text.textbox.TextBoxProperty
import com.henni.writebuddy.sticky.text.textbox.TextBoxTouchEvent
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A Composable function to create and remember an instance of [StickyItemController].
 * This controller manages various properties for sticky items, such as Post-its, images, and text boxes.
 * It provides the ability to update the properties and manage the state of each sticky item.
 *
 * @param postItProperty The initial properties of a Post-it note.
 * @param painterImageProperty The initial properties of a painter image.
 * @param vectorImageProperty The initial properties of a vector image.
 * @param bitmapImageProperty The initial properties of a bitmap image.
 * @param textBoxProperty The initial properties of a text box.
 * @return An instance of [StickyItemController] that manages sticky items.
 */

@Composable
fun rememberStickyItemController(
    postItProperty: PostItProperty = PostItProperty.DEFAULT,
    painterImageProperty: PainterImageProperty = PainterImageProperty.DEFAULT,
    vectorImageProperty: VectorImageProperty = VectorImageProperty.DEFAULT,
    bitmapImageProperty: ImageBitmapProperty = ImageBitmapProperty.DEFAULT,
    textBoxProperty: TextBoxProperty = TextBoxProperty.DEFAULT
): StickyItemController {
    return remember {
        StickyItemController(
            defaultPostItProperty = postItProperty,
            defaultPainterImageProperty = painterImageProperty,
            defaultVectorImageProperty = vectorImageProperty,
            defaultBitmapImageProperty = bitmapImageProperty,
            defaultTextBoxProperty = textBoxProperty
        )
    }
}

/**
 * A stable class that manages the state and properties of various sticky items (e.g., Post-it notes, images, text boxes).
 * It allows updating properties such as size, background color, text style, and more.
 *
 * @param defaultPostItProperty The default properties for a Post-it note.
 * @param defaultPainterImageProperty The default properties for a painter image.
 * @param defaultVectorImageProperty The default properties for a vector image.
 * @param defaultBitmapImageProperty The default properties for a bitmap image.
 * @param defaultTextBoxProperty The default properties for a text box.
 */

@Stable
class StickyItemController internal constructor(
    defaultPostItProperty: PostItProperty,
    defaultPainterImageProperty: PainterImageProperty,
    defaultVectorImageProperty: VectorImageProperty,
    defaultBitmapImageProperty: ImageBitmapProperty,
    defaultTextBoxProperty: TextBoxProperty
) {

    /**
     * Tracks the current touch event for the sticky item, defaults to PostItTouchEvent.
     */
    internal var touchEvent: StickyTouchEvent by mutableStateOf(
        PostItTouchEvent(this)
    )

    /**
     * Defines the type of sticky item (e.g., Post-It, Image, Text Box), defaults to POST_IT.
     */
    internal var type: StickyType by mutableStateOf(
        StickyType.POST_IT
    )

    /**
     * Private mutable state for Post-It properties.
     */
    private var _postItProperty = mutableStateOf(defaultPostItProperty)

    /**
     * Public read-only access to Post-It properties.
     */
    val postItProperty: PostItProperty
        get() = _postItProperty.value

    /**
     * Updates the width of the Post-It.
     * @param value The new width value.
     */
    fun updatePostItWidth(value: Dp) {
        val size = _postItProperty.value.size

        _postItProperty.value = _postItProperty.value.copy(
            size = size.copy(
                width = value
            )
        )
    }

    /**
     * Updates the height of the Post-It.
     * @param value The new height value.
     */
    fun updatePostItHeight(value: Dp) {
        val size = _postItProperty.value.size

        _postItProperty.value = _postItProperty.value.copy(
            size = size.copy(
                height = value
            )
        )
    }

    /**
     * Updates the size (width and height) of the Post-It.
     * @param value The new size value.
     */
    fun updatePostItSize(value: DpSize) {
        _postItProperty.value = _postItProperty.value.copy(
            size = value
        )
    }

    /**
     * Updates the text style of the Post-It.
     * @param value The new text style.
     */
    fun updatePostItTextStyle(value: TextStyle) {
        _postItProperty.value = _postItProperty.value.copy(
            textStyle = value
        )
    }

    /**
     * Updates the background color of the Post-It selection.
     * @param value The new background color.
     */
    fun updatePostSelectionBackgroundColor(value: Color) {
        _postItProperty.value = _postItProperty.value.copy(
            selectionBackgroundColor = value
        )
    }

    /**
     * Updates the elevation (shadow) of the Post-It.
     * @param value The new elevation value.
     */
    fun updatePostElevation(value: Dp) {
        _postItProperty.value = _postItProperty.value.copy(
            elevation = value
        )
    }

    /**
     * Updates the corner radius of the Post-It.
     * @param value The new corner radius.
     */
    fun updatePostCorner(value: Dp) {
        _postItProperty.value = _postItProperty.value.copy(
            corner = value
        )
    }

    /**
     * Updates the padding of the Post-It.
     * @param value The new padding value.
     */
    fun updatePostPadding(value: Dp) {
        _postItProperty.value = _postItProperty.value.copy(
            padding = value
        )
    }

    /**
     * Updates the background color of the Post-It.
     * @param value The new background color.
     */
    fun updatePostItBackgroundColor(value: Color) {
        _postItProperty.value = _postItProperty.value.copy(
            backgroundColor = value
        )
    }

    /**
     * Updates the text color of the Post-It.
     * @param value The new text color.
     */
    fun updatePostItTextColor(value: Color) {
        val textStyle = _postItProperty.value.textStyle

        _postItProperty.value = _postItProperty.value.copy(
            textStyle = textStyle.copy(color = value)
        )
    }

    /**
     * Updates the font size of the Post-It text.
     * @param value The new font size.
     */
    fun updatePostItFontSize(value: Float) {
        val textStyle = _postItProperty.value.textStyle

        _postItProperty.value = _postItProperty.value.copy(
            textStyle = textStyle.copy(fontSize = value.sp)
        )
    }

    /**
     * Private mutable state for TextBox properties.
     */
    private var _textBoxProperty = mutableStateOf(defaultTextBoxProperty)

    /**
     * Public read-only access to TextBox properties.
     */
    val textBoxProperty: TextBoxProperty
        get() = _textBoxProperty.value

    /**
     * Updates the text style of the TextBox.
     * @param value The new text style.
     */
    fun updateTextBoxTextStyle(value: TextStyle) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            textStyle = value
        )
    }

    /**
     * Updates the background color of the TextBox.
     * @param value The new background color.
     */
    fun updateTextBoxBackgroundColor(value: Color) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            backgroundColor = value
        )
    }

    /**
     * Updates the selection background color of the TextBox.
     * @param value The new selection background color.
     */
    fun updateTextBoxSelectionBackgroundColor(value: Color) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            selectionBackgroundColor = value
        )
    }

    /**
     * Updates the padding of the TextBox.
     * @param value The new padding value.
     */
    fun updateTextBoxPadding(value: Dp) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            padding = value
        )
    }

    /**
     * Updates the border box paint of the TextBox.
     * @param value The new border box paint.
     */
    fun updateTextBoxBorderBoxPaint(value: Paint) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            borderBoxPaint = value
        )
    }

    /**
     * Updates the border box padding of the TextBox.
     * @param value The new border box padding.
     */
    fun updateTextBoxBorderBoxPadding(value: Dp) {
        _textBoxProperty.value = _textBoxProperty.value.copy(
            borderBoxPadding = value
        )
    }

    /**
     * Updates the text color of the TextBox.
     * @param value The new text color.
     */
    fun updateTextBoxTextColor(value: Color) {
        val textStyle = _textBoxProperty.value.textStyle

        _textBoxProperty.value = _textBoxProperty.value.copy(
            textStyle = textStyle.copy(color = value)
        )
    }

    /**
     * Updates the font size of the TextBox text.
     * @param value The new font size.
     */
    fun updateTextBoxFontSize(value: Float) {
        val textStyle = _textBoxProperty.value.textStyle

        _textBoxProperty.value = _textBoxProperty.value.copy(
            textStyle = textStyle.copy(fontSize = value.sp)
        )
    }

    /**
     * Private mutable state for PainterImage properties.
     */
    private var _painterImageProperty = mutableStateOf(defaultPainterImageProperty)

    /**
     * Public read-only access to PainterImage properties.
     */
    val painterImageProperty: PainterImageProperty
        get() = _painterImageProperty.value

    /**
     * Updates the painter of the PainterImage.
     * @param value The new painter.
     */
    fun updatePainterImagePainter(value: Painter) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            painter = value
        )
    }

    /**
     * Updates the width of the PainterImage.
     * @param value The new width value.
     */
    fun updatePainterImageWidth(value: Dp) {
        val size = _painterImageProperty.value.size

        _painterImageProperty.value = _painterImageProperty.value.copy(
            size = size.copy(
                width = value
            )
        )
    }

    /**
     * Updates the height of the PainterImage.
     * @param value The new height value.
     */
    fun updatePainterImageHeight(value: Dp) {
        val size = _painterImageProperty.value.size

        _painterImageProperty.value = _painterImageProperty.value.copy(
            size = size.copy(
                height = value
            )
        )
    }

    /**
     * Updates the size (width and height) of the PainterImage.
     * @param value The new size value.
     */
    fun updatePainterImageSize(value: DpSize) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            size = value
        )
    }

    /**
     * Updates the alpha of the PainterImage.
     * @param value The new alpha value.
     */
    fun updatePainterImageAlpha(value: Float) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            alpha = value
        )
    }

    /**
     * Updates the alignment of the PainterImage.
     * @param value The new alignment value.
     */
    fun updatePainterImageAlignment(value: Alignment) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            alignment = value
        )
    }

    /**
     * Updates the content scale of the PainterImage.
     * @param value The new content scale value.
     */
    fun updatePainterImageContentScale(value: ContentScale) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            contentScale = value
        )
    }

    /**
     * Updates the color filter of the PainterImage.
     * @param value The new color filter value.
     */
    fun updatePainterImageColorFilter(value: ColorFilter) {
        _painterImageProperty.value = _painterImageProperty.value.copy(
            colorFilter = value
        )
    }

    /**
     * Private mutable state for VectorImage properties.
     */
    private var _vectorImageProperty = mutableStateOf(defaultVectorImageProperty)

    /**
     * Public read-only access to VectorImage properties.
     */
    val vectorImageProperty: VectorImageProperty
        get() = _vectorImageProperty.value

    /**
     * Updates the image vector of the VectorImage.
     * @param value The new image vector.
     */
    fun updateVectorImageImageVector(value: ImageVector) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            imageVector = value
        )
    }

    /**
     * Updates the width of the VectorImage.
     * @param value The new width value.
     */
    fun updateVectorImageWidth(value: Dp) {
        val size = _vectorImageProperty.value.size

        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            size = size.copy(
                width = value
            )
        )
    }

    /**
     * Updates the height of the VectorImage.
     * @param value The new height value.
     */
    fun updateVectorImageHeight(value: Dp) {
        val size = _vectorImageProperty.value.size

        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            size = size.copy(
                height = value
            )
        )
    }

    /**
     * Updates the size (width and height) of the VectorImage.
     * @param value The new size value.
     */
    fun updateVectorImageSize(value: DpSize) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            size = value
        )
    }

    /**
     * Updates the alpha of the VectorImage.
     * @param value The new alpha value.
     */
    fun updateVectorImageAlpha(value: Float) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            alpha = value
        )
    }

    /**
     * Updates the alignment of the VectorImage.
     * @param value The new alignment value.
     */
    fun updateVectorImageAlignment(value: Alignment) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            alignment = value
        )
    }

    /**
     * Updates the content scale of the VectorImage.
     * @param value The new content scale value.
     */
    fun updateVectorImageContentScale(value: ContentScale) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            contentScale = value
        )
    }

    /**
     * Updates the color filter of the VectorImage.
     * @param value The new color filter value.
     */
    fun updateVectorImageColorFilter(value: ColorFilter) {
        _vectorImageProperty.value = _vectorImageProperty.value.copy(
            colorFilter = value
        )
    }

    /**
     * Private mutable state for BitmapImage properties.
     */
    private var _bitmapImageProperty = mutableStateOf(defaultBitmapImageProperty)

    /**
     * Public read-only access to BitmapImage properties.
     */
    val bitmapImageProperty: ImageBitmapProperty
        get() = _bitmapImageProperty.value

    /**
     * Updates the ImageBitmap of the BitmapImage.
     * @param value The new ImageBitmap.
     */
    fun updateBitmapImageImageBitmap(value: ImageBitmap) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            imageBitmap = value
        )
    }

    /**
     * Updates the width of the BitmapImage.
     * @param value The new width value.
     */
    fun updateBitmapImageWidth(value: Dp) {
        val size = _bitmapImageProperty.value.size

        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            size = size.copy(
                width = value
            )
        )
    }

    /**
     * Updates the height of the BitmapImage.
     * @param value The new height value.
     */
    fun updateBitmapImageHeight(value: Dp) {
        val size = _bitmapImageProperty.value.size

        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            size = size.copy(
                height = value
            )
        )
    }

    /**
     * Updates the size (width and height) of the BitmapImage.
     * @param value The new size value.
     */
    fun updateBitmapImageSize(value: DpSize) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            size = value
        )
    }

    /**
     * Updates the alpha of the BitmapImage.
     * @param value The new alpha value.
     */
    fun updateBitmapImageAlpha(value: Float) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            alpha = value
        )
    }

    /**
     * Updates the alignment of the BitmapImage.
     * @param value The new alignment value.
     */
    fun updateBitmapImageAlignment(value: Alignment) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            alignment = value
        )
    }

    /**
     * Updates the content scale of the BitmapImage.
     * @param value The new content scale value.
     */
    fun updateBitmapImageContentScale(value: ContentScale) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            contentScale = value
        )
    }

    fun updateBitmapImageColorFilter(value: ColorFilter) {
        _bitmapImageProperty.value = _bitmapImageProperty.value.copy(
            colorFilter = value
        )
    }

    /**
     * Updates the color filter of the BitmapImage.
     * @param value The new color filter value.
     */
    fun updateStickyType(value: StickyType) {
        type = value
        touchEvent = when (value) {
            StickyType.POST_IT -> PostItTouchEvent(this)
            StickyType.PAINTER_IMAGE -> PainterImageTouchEvent(this)
            StickyType.VECTOR_IMAGE -> VectorImageTouchEvent(this)
            StickyType.BITMAP_IMAGE -> ImageBitmapTouchEvent(this)
            StickyType.TEXT_BOX -> TextBoxTouchEvent(this)
        }
    }

    /**
     * A private mutable list of sticky items (e.g., Post-it notes, images, text boxes).
     * Holds all the attached sticky items in the current session.
     */
    private val _stickyItemList = mutableStateListOf<Attachable>()

    /**
     * A public read-only list of sticky items.
     * Provides access to the list of sticky items, but prevents modification from outside the class.
     */
    val stickyItemList: List<Attachable>
        get() = _stickyItemList.toList()

    /**
     * A private mutable map that associates each sticky item by its ID to its index in the list.
     */
    private val _stickyItemMap = mutableStateMapOf<String, Int>()

    /**
     * Adds a sticky item to the list and updates the map with its index.
     * @param item The sticky item to be added.
     */
    fun addStickyItem(item: Attachable) {
        _stickyItemList.add(item)
        _stickyItemMap[item.id] = _stickyItemList.lastIndex
    }

    /**
     * Removes a sticky item from the list and updates the map to reflect the removal.
     * @param item The sticky item to be removed.
     */
    fun removeStickyItem(item: Attachable) {
        _stickyItemList.remove(item)
        _stickyItemMap.remove(item.id)
    }

    /**
     * Updates the text content of a sticky item, specifically for StickyTextItem.
     * @param id The ID of the sticky item to be updated.
     * @param text The new text value to set.
     */
    fun updateText(id: String, text: String) {
        val stickyItemIdx: Int = _stickyItemMap[id] ?: return
        val curStickyItem = _stickyItemList[stickyItemIdx]
        if (curStickyItem is StickyTextItem) {
            _stickyItemList[stickyItemIdx] = curStickyItem.copy(text = text)
        }
    }

    /**
     * Updates the translation (position) of a sticky item.
     * @param id The ID of the sticky item to be updated.
     * @param translate The new translation (offset) value.
     */
    fun updateTranslate(id: String, translate: Offset) {
        val stickyItemIdx: Int = _stickyItemMap[id] ?: return
        val curStickyItem = _stickyItemList[stickyItemIdx]
        _stickyItemList[stickyItemIdx] = curStickyItem.copySelf(translate = translate)
    }

    /**
     * Updates the zoom state of a sticky item (scale factor and scale offset).
     * @param id The ID of the sticky item to be updated.
     * @param scaleFactor The new scale factor to set.
     * @param scaleOffset The new scale offset to set.
     */
    fun updateZoomState(id: String, scaleFactor: Float, scaleOffset: Offset) {
        val stickyItemIdx: Int = _stickyItemMap[id] ?: return
        val curStickyItem = _stickyItemList[stickyItemIdx]
        _stickyItemList[stickyItemIdx] = curStickyItem.copySelf(
            scaleFactor = scaleFactor,
            scaleOffset = scaleOffset
        )
    }

    /**
     * Checks if any StickyTextItem is currently focused.
     * @return True if any StickyTextItem is focused, otherwise false.
     */
    fun isFocusNow(): Boolean {
        return _stickyItemList.find { item ->
            item is StickyTextItem && item.isFocus
        } != null
    }

    /**
     * Updates the focus state of a sticky item.
     * @param id The ID of the sticky item to be updated.
     * @param isFocus The new focus state to set.
     */
    fun updateFocus(id: String, isFocus: Boolean) {
        val stickyItemIdx: Int = _stickyItemMap[id] ?: return
        val curStickyItem = _stickyItemList[stickyItemIdx]
        _stickyItemList[stickyItemIdx] = curStickyItem.copySelf(isFocus = isFocus)
    }

    /**
     * Clears the focus from all sticky items by setting `isFocus` to false for each.
     */
    fun clearAllFocus() {
        for (stickyItemIdx in 0 until _stickyItemList.size) {
            val curStickyItem = _stickyItemList[stickyItemIdx]
            _stickyItemList[stickyItemIdx] = curStickyItem.copySelf(isFocus = false)
        }
    }

    /**
     * A flow that tracks the invalidation state of the sticky item list.
     * Can be used to trigger UI re-renders when the list needs to be updated.
     */
    val invalidateTick: MutableStateFlow<Int> = MutableStateFlow(0)

    /**
     * Updates the invalidate tick, signaling that the sticky item list should be re-rendered.
     */
    fun updateInvalidateTick() {
        invalidateTick.updateTick()
    }

}
