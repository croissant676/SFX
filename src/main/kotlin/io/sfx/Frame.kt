package io.sfx

import java.awt.Dimension
import javax.swing.JFrame

fun frame(name: String, size: Dimension = dim(500, 500), visible: Boolean = true, block: JFrame.() -> Unit = {}): JFrame {
    val frame = JFrame(name)
    frame.size = size
    frame.isVisible = visible
    block(frame)
    return frame
}

