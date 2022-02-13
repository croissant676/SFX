@file:Suppress("unused", "SpellCheckingInspection", "UNCHECKED_CAST", "DuplicatedCode")

package dev.kason.sfx

import java.awt.Color
import java.awt.FlowLayout
import java.io.File
import java.io.Serializable
import java.net.URL
import java.util.Calendar
import java.util.Date
import java.util.Hashtable
import java.util.Locale
import java.util.Vector
import javax.swing.AbstractListModel
import javax.swing.Action
import javax.swing.BoundedRangeModel
import javax.swing.ButtonGroup
import javax.swing.ComboBoxModel
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JCheckBoxMenuItem
import javax.swing.JColorChooser
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JProgressBar
import javax.swing.JRadioButton
import javax.swing.JRadioButtonMenuItem
import javax.swing.JScrollPane
import javax.swing.JSeparator
import javax.swing.JSlider
import javax.swing.JSpinner
import javax.swing.JSplitPane
import javax.swing.JTabbedPane
import javax.swing.JTable
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.JToolBar
import javax.swing.JToolTip
import javax.swing.JTree
import javax.swing.ListModel
import javax.swing.ListSelectionModel
import javax.swing.MutableComboBoxModel
import javax.swing.ScrollPaneConstants
import javax.swing.SpinnerDateModel
import javax.swing.SpinnerListModel
import javax.swing.SpinnerModel
import javax.swing.SpinnerNumberModel
import javax.swing.SwingConstants
import javax.swing.UIManager
import javax.swing.colorchooser.ColorSelectionModel
import javax.swing.filechooser.FileSystemView
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableColumnModel
import javax.swing.table.TableModel
import javax.swing.text.Document
import javax.swing.text.StyledDocument
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeModel
import javax.swing.tree.TreeNode
import org.intellij.lang.annotations.MagicConstant
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaMethod

operator fun JComponent.plusAssign(component: JComponent) {
    if (currentLocation == null) {
        add(component)
    } else {
        add(component, currentLocation)
    }
}

inline fun <T> JComponent.opcr(component: T, block: T.() -> Unit = {}): T where T : JComponent {
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
    @MagicConstant(
        intValues = [JSlider.HORIZONTAL.toLong(), JSlider.VERTICAL.toLong()],
        valuesFromClass = JSlider::class
    ) alignment: Int = JSlider.HORIZONTAL,
    minimumValue: Int = 0,
    maximumValue: Int = 100,
    initialValue: Int = 50,
    block: JSlider.() -> Unit = {}
): JSlider = opcr(JSlider(alignment, minimumValue, maximumValue, initialValue), block)

fun JComponent.label(
    text: String? = null, icon: Icon? = null,
    @MagicConstant(valuesFromClass = JLabel::class) alignment: Int = JLabel.LEADING, block: JLabel.() -> Unit = {}
): JLabel = opcr(JLabel(text, icon, alignment), block)

fun JComponent.progressbar(orient: Int = JProgressBar.HORIZONTAL, min: Int = 0, max: Int = 100, block: JProgressBar.() -> Unit = {}): JProgressBar =
    opcr(JProgressBar(orient, min, max), block)

fun JComponent.progressbar(boundedRangeModel: BoundedRangeModel, block: JProgressBar.() -> Unit = {}): JProgressBar =
    opcr(JProgressBar(boundedRangeModel), block)

fun JComponent.sep(orientation: Int = JSeparator.HORIZONTAL, block: JSeparator.() -> Unit = {}): JSeparator = opcr(JSeparator(orientation), block)

fun JComponent.tooltip(block: JToolTip.() -> Unit = {}): JToolTip = opcr(JToolTip(), block)

fun JComponent.colorchooser(initialColor: Color = Color.WHITE, block: JColorChooser.() -> Unit = {}): JColorChooser =
    opcr(JColorChooser(initialColor), block)

fun JComponent.colorchooser(colorSelectionModel: ColorSelectionModel, block: JColorChooser.() -> Unit = {}): JColorChooser =
    opcr(JColorChooser(colorSelectionModel), block)

fun JComponent.filechooser(initialFile: File? = null, fileSystemView: FileSystemView? = null, block: JFileChooser.() -> Unit = {}): JFileChooser =
    opcr(JFileChooser(initialFile, fileSystemView), block)

fun JComponent.filechooser(initialFile: String? = null, fileSystemView: FileSystemView? = null, block: JFileChooser.() -> Unit = {}): JFileChooser =
    opcr(JFileChooser(initialFile, fileSystemView), block)

fun JComponent.table(
    tableModel: TableModel? = null,
    tableColumnModel: TableColumnModel? = null,
    listSelectionModel: ListSelectionModel? = null,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(tableModel, tableColumnModel, listSelectionModel), block)

fun JComponent.table(
    rows: Int = 0,
    cols: Int = 0,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(rows, cols), block)

fun JComponent.table(
    data: Vector<out Vector<*>>,
    columnNames: Vector<*>,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(data, columnNames), block)

fun JComponent.table(
    data: Array<out Array<*>>,
    columnNames: Array<*>,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(data, columnNames), block)

inline fun <reified T> JComponent.table(
    list: List<T>,
    vararg properties: KProperty<*>,
    block: JTable.() -> Unit = {}
): JTable {
    if (!properties.all { it.getter.javaMethod!!.declaringClass == T::class.java }) {
        throw IllegalArgumentException("Not all properties are from ${T::class.qualifiedName}")
    }
    val model = DefaultTableModel()
    val table = JTable(model)
    for (property in properties) {
        val vector = Vector(list.map {
            try {
                property.getter.call(it) ?: "null"
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot access property ${property.name} inside of class ${T::class.qualifiedName} for object $it")
            }
        })
        vector.add(0, property.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
        model.addColumn(property.name, vector)
    }
    return opcr(table, block)
}

fun JComponent.tree(values: Array<*>, block: JTree.() -> Unit = {}): JTree = opcr(JTree(values), block)
fun JComponent.tree(values: Collection<*>, block: JTree.() -> Unit = {}): JTree = opcr(JTree(values.toTypedArray()), block)
fun JComponent.tree(values: Map<*, *>, block: JTree.() -> Unit = {}): JTree {
    return if (values is Hashtable) {
        opcr(JTree(values), block)
    } else {
        opcr(JTree(Hashtable(values)), block)
    }
}

fun JComponent.tree(treeModel: TreeModel? = null, block: JTree.() -> Unit = {}): JTree =
    opcr(if (treeModel == null) JTree() else JTree(treeModel), block)

fun JComponent.tree(root: TreeNode, childLeaves: Boolean = false, block: JTree.() -> Unit = {}): JTree =
    opcr(JTree(root, childLeaves), block)

fun JComponent.tree(value: Any? = null, allowsChildren: Boolean = true, block: DefaultMutableTreeNode.() -> Unit = {}): JTree =
    opcr(JTree(createNode(value, allowsChildren, block)))

fun createNode(value: Any? = null, allowsChildren: Boolean = true, block: DefaultMutableTreeNode.() -> Unit = {}): DefaultMutableTreeNode {
    val root = DefaultMutableTreeNode(value, allowsChildren)
    block(root)
    return root
}

fun DefaultMutableTreeNode.node(value: Any? = null, allowsChildren: Boolean = true, block: TreeNode.() -> Unit = {}): DefaultMutableTreeNode {
    val node = DefaultMutableTreeNode(value, allowsChildren)
    block(node)
    add(node)
    return node
}

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