package com.henni.handwriting.operation

import com.henni.handwriting.HandwritingController
import com.henni.handwriting.model.HandwritingPath

/**
 * [InsertOperation] represents the operation of inserting a handwriting path.
 *
 * @property controller The handwriting controller object that manages handwriting paths.
 * @property path The handwriting path to be inserted.
 */

internal class InsertOperation internal constructor(
  private val controller: HandwritingController,
  private val path: HandwritingPath,
) : Operation {

  /**
   * Performs the operation of adding the handwriting path.
   *
   */
  override fun doOperation(): Boolean {
    controller.addHandWritingPath(path)
    return true
  }

  /**
   * Undo the operation by removing the last handwriting path.
   */
  override fun undo() = with(controller) {
    removeHandWritingPath(path)
  }

  /**
   * Redo the operation by adding the handwriting path again.
   */
  override fun redo() = with(controller) {
    addHandWritingPath(path)
  }
}
