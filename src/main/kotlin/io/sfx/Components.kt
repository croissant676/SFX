package io.sfx

import java.awt.LayoutManager
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

operator fun JComponent.plusAssign(other: JComponent) {
    add(other)
}

@Suppress("SpellCheckingInspection")
private fun <T: JComponent> JComponent.opcr(component: T, block: T.() -> Unit) : T {
    block(component)
    this += component
    return component
}

fun JComponent.panel(layout: Layout<*>, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    val manager: LayoutManager = if(layout is AttachingLayout) {
        layout.create(panel)
    } else {
        layout.create(null)
    }!!
    panel.layout = manager
    return opcr(panel, block)
}

fun JComponent.button(text: String, block: JButton.() -> Unit = {}): JButton {
    val button = JButton(text)
    return opcr(button, block)
}

fun JButton.action(block: (ActionEvent) -> Unit) {
    addActionListener {
        block(it)
    }
}