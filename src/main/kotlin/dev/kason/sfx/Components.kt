@file:Suppress("unused", "SpellCheckingInspection", "UNCHECKED_CAST")

package dev.kason.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.Component
import java.awt.FlowLayout
import java.awt.PopupMenu
import java.awt.event.ActionEvent
import java.io.Serializable
import java.net.URL
import java.util.*
import javax.swing.*
import javax.swing.text.Document
import javax.swing.text.StyledDocument

operator fun JComponent.plusAssign(component: JComponent) {
    if (currentLocation == null) {
        add(component)
    } else {
        add(component, currentLocation)
    }
}

internal inline fun <T> JComponent.opcr(component: T, block: T.() -> Unit): T where T : JComponent {
    block(component)
    this += component
    return component
}

// Basic Controls

fun JComponent.button(text: String? = null, icon: Icon? = null, block: JButton.() -> Unit = {}): JButton =
    opcr(JButton(text, icon), block)

fun JComponent.checkbox(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JCheckBox.() -> Unit = {}): JCheckBox =
    opcr(JCheckBox(text, icon, selected), block)

fun <T> JComponent.combobox(block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox<T>(), block)
fun <T> JComponent.combobox(array: Array<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox<T>(array), block)
fun <T> JComponent.combobox(vector: Vector<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox(vector), block)
fun <T> JComponent.combobox(comboBoxModel: ComboBoxModel<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox(comboBoxModel), block)
fun <T> JComponent.combobox(list: List<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> =
    opcr(JComboBox(ListComboBoxModel(if (list is MutableList<*>) list as MutableList<T> else ArrayList(list))), block)

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

fun JComponent.radiobutton(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JRadioButton.() -> Unit = {}): JRadioButton =
    opcr(JRadioButton(text, icon, selected), block)

fun group(vararg radioButton: JRadioButton, block: ButtonGroup.() -> Unit = {}): ButtonGroup {
    val buttonGroup = ButtonGroup()
    radioButton.forEach {
        buttonGroup.add(it)
    }
    block(buttonGroup)
    return buttonGroup
}

fun JComponent.textfield(text: String? = null, columns: Int = 0, document: Document? = null, block: JTextField.() -> Unit = {}): JTextField =
    opcr(JTextField(document, text, columns), block)

fun JComponent.passwordfield(
    text: String? = null,
    columns: Int = 0,
    document: Document? = null,
    block: JPasswordField.() -> Unit = {}
): JPasswordField = opcr(JPasswordField(document, text, columns), block)

fun JComponent.textarea(
    text: String? = null,
    row: Int = 0,
    columns: Int = 0,
    document: Document? = null,
    block: JTextArea.() -> Unit = {}
): JTextArea =
    opcr(JTextArea(document, text, row, columns), block)

fun JComponent.spinner(model: SpinnerModel = numberModel(), block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(model), block)

fun JComponent.datespinner(
    value: Date = Date(),
    start: Comparable<Date>? = null,
    end: Comparable<Date>? = null,
    calendarField: Int = Calendar.DAY_OF_MONTH,
    block: JSpinner.() -> Unit = {}
): JSpinner = opcr(JSpinner(SpinnerDateModel(value, start, end, calendarField)), block)

fun JComponent.numberspinner(
    initialValue: Number = 0,
    minimumValue: Comparable<Number>? = null,
    maximumValue: Comparable<Number>? = null,
    stepValue: Number = 1,
    block: JSpinner.() -> Unit = {}
): JSpinner = opcr(JSpinner(SpinnerNumberModel(initialValue, minimumValue, maximumValue, stepValue)), block)

fun JComponent.listspinner(list: List<*>, block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(SpinnerListModel(list)), block)
fun JComponent.listspinner(array: Array<*>, block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(SpinnerListModel(array)), block)

fun dateModel(
    value: Date = Date(),
    start: Comparable<Date>? = null,
    end: Comparable<Date>? = null,
    calendarField: Int = Calendar.DAY_OF_MONTH
): SpinnerDateModel = SpinnerDateModel(value, start, end, calendarField)

fun numberModel(
    initialValue: Number = 0,
    minimumValue: Comparable<Number>? = null,
    maximumValue: Comparable<Number>? = null,
    stepValue: Number = 1
): SpinnerNumberModel =
    SpinnerNumberModel(initialValue, minimumValue, maximumValue, stepValue)

fun listModel(list: List<*>): SpinnerListModel = SpinnerListModel(list)
fun listModel(array: Array<*> = arrayOf<Any>()): SpinnerListModel = SpinnerListModel(array)

fun JSpinner.enableScroll() {
    addMouseWheelListener {
        if (it.wheelRotation == 0) return@addMouseWheelListener
        value = if (it.wheelRotation > 0) {
            model.nextValue
        } else {
            model.previousValue
        }
    }
}

fun JComponent.slider(
    @MagicConstant(valuesFromClass = SwingConstants::class) alignment: Int = SwingConstants.HORIZONTAL,
    minimumValue: Int = 0,
    maximumValue: Int = 100,
    initialValue: Int = 50,
    block: JSlider.() -> Unit = {}
): JSlider = opcr(JSlider(alignment, minimumValue, maximumValue, initialValue), block)

// Menus

fun JFrame.menubar(block: JMenuBar.() -> Unit = {}): JMenuBar {
    val menuBar = JMenuBar()
    block(menuBar)
    jMenuBar = menuBar
    return menuBar
}

fun JMenuBar.menu(text: String? = null, block: JMenu.() -> Unit = {}): JMenu = opcr(JMenu(text), block)
fun JMenuBar.menu(action: Action? = null, block: JMenu.() -> Unit = {}): JMenu = opcr(JMenu(action), block)
fun JMenu.menu(text: String? = null, block: JMenu.() -> Unit = {}): JMenu = opcr(JMenu(text), block)
fun JMenu.menu(action: Action? = null, block: JMenu.() -> Unit = {}): JMenu = opcr(JMenu(action), block)
fun JMenu.menuitem(text: String? = null, icon: Icon? = null, block: JMenuItem.() -> Unit = {}): JMenuItem = opcr(JMenuItem(text, icon), block)
fun JMenu.menuitem(action: Action? = null, block: JMenuItem.() -> Unit = {}): JMenuItem = opcr(JMenuItem(action), block)

fun JMenu.radiobutton(
    text: String? = null,
    icon: Icon? = null,
    selected: Boolean = false,
    block: JRadioButtonMenuItem.() -> Unit = {}
): JRadioButtonMenuItem = opcr(JRadioButtonMenuItem(text, icon, selected), block)

fun JMenu.checkbox(
    text: String? = null,
    icon: Icon? = null,
    selected: Boolean = false,
    block: JCheckBoxMenuItem.() -> Unit = {}
): JCheckBoxMenuItem = opcr(JCheckBoxMenuItem(text, icon, selected), block)

fun group(vararg radioButton: JRadioButtonMenuItem, block: ButtonGroup.() -> Unit = {}): ButtonGroup {
    val buttonGroup = ButtonGroup()
    radioButton.forEach {
        buttonGroup.add(it)
    }
    block(buttonGroup)
    return buttonGroup
}

// Other

inline fun JButton.action(crossinline block: (ActionEvent) -> Unit) {
    addActionListener {
        block(it)
    }
}

private val componentMap = hashMapOf<Any, JComponent>()
val nodes = object : Map<Any, JComponent> {
    override val entries: Set<Map.Entry<Any, JComponent>> = componentMap.entries
    override val keys: Set<Any> = componentMap.keys
    override val size: Int = componentMap.size
    override val values: Collection<JComponent> = componentMap.values
    override fun containsKey(key: Any): Boolean = componentMap.containsKey(key)
    override fun containsValue(value: JComponent): Boolean = componentMap.containsValue(value)
    override operator fun get(key: Any): JComponent? = componentMap[key]
    override fun isEmpty(): Boolean = componentMap.isEmpty()
}

fun <T> nodes(key: Any): T? where T : JComponent = nodes[key] as? T

var JComponent.link: Any?
    get() {
        for (entry in componentMap) {
            if (entry.value == this) return entry.key
        }
        return null
    }
    set(value) {
        if (value != null && this !in componentMap.values) {
            componentMap[value] = this
        }
    }

private val jcomponent = object : JComponent() {
    override fun add(popup: PopupMenu?) = Unit
    override fun add(comp: Component?): Component? = comp
    override fun add(comp: Component, constraints: Any?) = Unit
    override fun add(comp: Component?, index: Int): Component? = comp
    override fun add(comp: Component?, constraints: Any?, index: Int) = Unit
    override fun add(name: String?, comp: Component?): Component? = comp
    override fun toString(): String = "JComponent{...}"
    override fun hashCode(): Int = 0
    override fun equals(other: Any?): Boolean = other != null && other === this
}

fun define(block: JComponent.() -> Unit) {
    block(jcomponent)
}

// Panes

fun JComponent.panel(layout: Layout<*>? = null, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    panel.layout = layout?.createLayout(panel) ?: FlowLayout()
    return opcr(panel, block)
}

fun JComponent.tabbedpane(
    @MagicConstant(valuesFromClass = JTabbedPane::class)
    placement: Int = JTabbedPane.TOP,
    @MagicConstant(valuesFromClass = JTabbedPane::class)
    layoutPolicy: Int = JTabbedPane.WRAP_TAB_LAYOUT,
    block: JTabbedPane.() -> Unit = {}
): JTabbedPane = opcr(JTabbedPane(placement, layoutPolicy), block)

fun JComponent.scrollpane(
    @MagicConstant(valuesFromClass = ScrollPaneConstants::class)
    verticalScrollBarPolicy: Int = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    @MagicConstant(valuesFromClass = ScrollPaneConstants::class)
    horizontalScrollBarPolicy: Int = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,
    component: JComponent? = null,
    block: JScrollPane.() -> Unit = {}
): JScrollPane = opcr(JScrollPane(component, verticalScrollBarPolicy, horizontalScrollBarPolicy), block)

fun JComponent.splitpane(
    leftComponent: JComponent? = null,
    rightComponent: JComponent? = null,
    @MagicConstant(valuesFromClass = JSplitPane::class)
    orientation: Int = JSplitPane.HORIZONTAL_SPLIT,
    continuouslyResize: Boolean = UIManager.getBoolean("SplitPane.continuousLayout"),
    block: JSplitPane.() -> Unit = {}
): JSplitPane =
    opcr(JSplitPane(orientation, continuouslyResize, leftComponent, rightComponent), block)


fun JComponent.toolbar(
    name: String? = null,
    @MagicConstant(valuesFromClass = SwingConstants::class)
    orientation: Int = SwingConstants.HORIZONTAL,
    block: JToolBar.() -> Unit = {}
): JToolBar =
    opcr(JToolBar(name, orientation), block)

fun JComponent.editorpane(
    url: String? = null,
    type: String? = null,
    text: String? = null,
    block: JEditorPane.() -> Unit = {}
): JEditorPane = opcr(JEditorPane(), block).apply {
    if (url != null) page = URL(url)
    if (type != null) contentType = type
    if (text != null) setText(text)
}

fun JComponent.textpane(document: StyledDocument? = null, block: JTextPane.() -> Unit): JTextPane = opcr(JTextPane(document), block)

// Utils

@Suppress("UNCHECKED_CAST")
class ListComboBoxModel<T>(private val list: MutableList<T>) : AbstractListModel<T>(), MutableComboBoxModel<T>, Serializable {
    private var currentSelectedObject: T? = if (list.isNotEmpty()) list[0] else null

    override fun setSelectedItem(item: Any?) {
        if (item == null) return
        if (currentSelectedObject == null || currentSelectedObject != item) {
            currentSelectedObject = item as T
            fireContentsChanged(this, 0, size)
        }
    }

    override fun getSelectedItem() = currentSelectedObject!!
    override fun getSize(): Int = list.size
    override fun getElementAt(index: Int): T? = if (index >= 0 && index < list.size) list.elementAt(index) else null
    fun getIndexOf(anObject: Any?): Int = list.indexOf(anObject as T)
    override fun addElement(element: T) {
        list += element
        fireIntervalAdded(this, list.lastIndex, list.lastIndex)
        if (list.size == 1 && currentSelectedObject == null && element != null) {
            selectedItem = element
        }
    }

    override fun insertElementAt(element: T, index: Int) {
        list.add(index, element)
        fireIntervalAdded(this, index, index)
    }

    override fun removeElementAt(index: Int) {
        if (list[index] == currentSelectedObject) {
            currentSelectedObject = if (index == 0) if (size == 1) null else list[index + 1]
            else list[index - 1]
        }
        list.removeAt(index)
        fireIntervalRemoved(this, index, index)
    }

    override fun removeElement(element: Any) {
        list.remove(element as T)
    }

    fun removeAllElements() {
        if (list.isNotEmpty()) {
            list.clear()
            currentSelectedObject = null
            fireIntervalRemoved(this, 0, list.lastIndex)
        } else {
            currentSelectedObject = null
        }
    }

    fun addAll(collection: Collection<T>) {
        list.addAll(collection)
        fireIntervalAdded(this, 0, list.lastIndex)
    }

    fun addAll(index: Int, collection: Collection<T>) {
        list.addAll(index, collection)
        fireIntervalAdded(this, index, index + collection.size - 1)
    }
}