package dev.kason.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.FlowLayout
import java.awt.GraphicsConfiguration
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.*


fun frame(
    frameName: String? = null,
    visible: Boolean = true,
    graphicsConfiguration: GraphicsConfiguration? = null,
    component: JComponent? = null,
    block: JFrame.() -> Unit
): JFrame {
    val frame = if (graphicsConfiguration == null) JFrame(frameName) else JFrame(frameName, graphicsConfiguration)
    if (component != null) frame.add(component)
    block(frame)
    frame.isVisible = visible
    return frame
}

var JFrame.closeOperation: Int
    get() = defaultCloseOperation
    set(@MagicConstant(valuesFromClass = WindowConstants::class) value) {
        defaultCloseOperation = value
    }

fun JFrame.onClose(block: JFrame.() -> Unit) = addComponentListener(object : ComponentAdapter() {
    override fun componentHidden(e: ComponentEvent) = block()
})

fun JFrame.panel(layout: Layout<*>? = null, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    panel.layout = layout?.createLayout(panel) ?: FlowLayout()
    block(panel)
    add(panel)
    return panel
}

@Suppress("SpellCheckingInspection")
fun JFrame.createInternalFrame(
    title: String = "", resizable: Boolean = false, closable: Boolean = false,
    maximizable: Boolean = false, iconifiable: Boolean = false, block: JInternalFrame.() -> Unit = {}
): JInternalFrame {
    val frame = JInternalFrame(title, resizable, closable, maximizable, iconifiable)
    block(frame)
    add(frame)
    return frame
}

@Suppress("SpellCheckingInspection")
fun JComponent.createInternalFrame(
    title: String = "", resizable: Boolean = false, closable: Boolean = false,
    maximizable: Boolean = false, iconifiable: Boolean = false, block: JInternalFrame.() -> Unit = {}
): JInternalFrame = opcr(JInternalFrame(title, resizable, closable, maximizable, iconifiable), block)