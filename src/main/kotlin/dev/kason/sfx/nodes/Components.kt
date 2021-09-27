package dev.kason.sfx.nodes

import org.intellij.lang.annotations.MagicConstant
import java.awt.FlowLayout
import java.util.*
import javax.swing.*
import javax.swing.text.Document

operator fun JComponent.plusAssign(component: JComponent) {
    add(component)
}

@Suppress("SpellCheckingInspection")
internal inline fun <T> JComponent.opcr(component: T, block: T.() -> Unit): T where T : JComponent {
    block(component)
    this += component
    return component
}

fun JComponent.panel(layout: Layout<*>? = null, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    panel.layout = layout?.createLayout(panel) ?: FlowLayout()
    return opcr(panel, block)
}

fun JComponent.button(text: String? = null, icon: Icon? = null, block: JButton.() -> Unit = {}): JButton =
    opcr(JButton(text, icon), block)

fun JComponent.checkbox(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JCheckBox.() -> Unit = {}): JCheckBox =
    opcr(JCheckBox(text, icon, selected), block)

fun JComponent.radiobutton(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JRadioButton.() -> Unit = {}): JRadioButton =
    opcr(JRadioButton(text, icon, selected), block)

fun <T> JComponent.list(block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(), block)
fun <T> JComponent.list(array: Array<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(array), block)
fun <T> JComponent.list(vector: Vector<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(vector), block)
fun <T> JComponent.list(listModel: ListModel<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList(listModel), block)
fun <T> JComponent.list(list: List<T>, block: JList<T>.() -> Unit = {}): JList<T> {
    val listModel = object : AbstractListModel<T>() {
        override fun getSize(): Int = list.size
        override fun getElementAt(index: Int): T = list[index]
    }
    return opcr(JList<T>(listModel), block)
}

@Suppress("SpellCheckingInspection")
fun JComponent.textfield(text: String? = null, columns: Int = 0, document: Document? = null, block: JTextField.() -> Unit = {}): JTextField =
    opcr(JTextField(document, text, columns), block)

fun JComponent.textarea(
    text: String? = null,
    row: Int = 0,
    columns: Int = 0,
    document: Document? = null,
    block: JTextArea.() -> Unit = {}
): JTextArea =
    opcr(JTextArea(document, text, row, columns), block)

@Suppress("SpellCheckingInspection")
fun JComponent.tabbedpane(
    @MagicConstant(valuesFromClass = JTabbedPane::class, intValues = [0, 1, 2, 3, 4])
    placement: Int = JTabbedPane.TOP,
    @MagicConstant(valuesFromClass = JTabbedPane::class, intValues = [0, 1])
    layoutPolicy: Int = JTabbedPane.WRAP_TAB_LAYOUT,
    block: JTabbedPane.() -> Unit = {}
): JTabbedPane = opcr(JTabbedPane(placement, layoutPolicy), block)

@Suppress("SpellCheckingInspection")
fun JComponent.tabbedpane(
    placement: Constants = Constants.TOP,
    layoutPolicy: TabLayout = TabLayout.WRAP,
    block: JTabbedPane.() -> Unit = {}
): JTabbedPane = opcr(JTabbedPane(placement.value, layoutPolicy.value), block)

enum class TabLayout(val value: Int) {
    WRAP(JTabbedPane.WRAP_TAB_LAYOUT),
    SCROLL(JTabbedPane.SCROLL_TAB_LAYOUT);

    override fun toString(): String = "TabLayout{name=$name, ordinal=$ordinal, value=$value}"
}

enum class Constants(val value: Int) {
    CENTER(SwingConstants.CENTER),
    TOP(SwingConstants.TOP),
    LEFT(SwingConstants.LEFT),
    BOTTOM(SwingConstants.BOTTOM),
    RIGHT(SwingConstants.RIGHT),
    NORTH(SwingConstants.NORTH),
    NORTH_EAST(SwingConstants.NORTH_EAST),
    EAST(SwingConstants.EAST),
    SOUTH_EAST(SwingConstants.SOUTH_EAST),
    SOUTH(SwingConstants.SOUTH),
    SOUTH_WEST(SwingConstants.SOUTH_WEST),
    WEST(SwingConstants.WEST),
    NORTH_WEST(SwingConstants.NORTH_WEST),
    HORIZONTAL(SwingConstants.HORIZONTAL),
    VERTICAL(SwingConstants.VERTICAL),
    LEADING(SwingConstants.LEADING),
    TRAILING(SwingConstants.TRAILING),
    NEXT(SwingConstants.NEXT),
    PREVIOUS(SwingConstants.PREVIOUS);

    override fun toString(): String = "Constant{name=$name, ordinal=$ordinal, value=$value}"
}