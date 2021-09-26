package io.sfx

import java.awt.Dimension


fun dim(width: Number, height: Number): Dimension {
    return Dimension(width.toInt(), height.toInt())
}