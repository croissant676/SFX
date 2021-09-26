package io.sfx

import java.awt.LayoutManager
import javax.swing.JComponent
import javax.swing.JPanel

operator fun JComponent.plusAssign(other: JComponent) {
    add(other)
}

fun JComponent.panel(layout: Layout<*>): JPanel {
    val panel = JPanel()
    val manager: LayoutManager = if(layout is AttachingLayout) {
        layout.create(panel)
    } else {
        layout.create(null)
    }!!
    panel.layout = manager
    this += panel
    return panel
}