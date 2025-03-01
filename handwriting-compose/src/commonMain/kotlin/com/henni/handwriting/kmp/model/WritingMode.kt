package com.henni.handwriting.kmp.model

/**
 * Enum class representing different writing modes.
 * It defines whether the user is writing with a finger, stylus pen, or an unknown method.
 */

enum class WritingMode {
    /**
     * Mode for writing with a finger
     */
    Hand,
    /**
     * Mode for writing with a stylus pen
     */
    StylusPen,
    /**
     * Unknown input mode (e.g., when running on an unsupported device)
     */
    Unknown
}