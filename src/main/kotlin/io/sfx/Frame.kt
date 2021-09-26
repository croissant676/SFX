package io.sfx

import java.awt.Dimension
import java.awt.LayoutManager
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel

fun frame(name: String, size: Dimension = dim(500, 500), visible: Boolean = true, block: JFrame.() -> Unit = {}): JFrame {
    val frame = JFrame(name)
    frame.size = size
    block(frame)
    frame.isVisible = visible
    return frame
}

operator fun JFrame.plusAssign(other: JComponent) {
    add(other)
}

fun JFrame.panel(layout: Layout<*> = flowLayout, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    val manager: LayoutManager = if(layout is AttachingLayout) {
        layout.create(panel)
    } else {
        layout.create(null)
    }!!
    panel.layout = manager
    block(panel)
    this += panel
    return panel
}

// TODO add jframe functionality: currently stuck at += panel(<layout>) {...}