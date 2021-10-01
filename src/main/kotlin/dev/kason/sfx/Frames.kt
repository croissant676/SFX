@file:Suppress("unused", "SpellCheckingInspection", "UNCHECKED_CAST")

package dev.kason.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GraphicsConfiguration
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.io.File
import java.net.URL
import java.util.*
import javax.swing.*
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
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaMethod


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

operator fun JFrame.plusAssign(component: JComponent) {
    if (currentLocation == null) {
        add(component)
    } else {
        add(component, currentLocation)
    }
}

inline fun <T> JFrame.opcr(component: T, block: T.() -> Unit = {}): T where T : JComponent {
    block(component)
    this += component
    return component
}

// Basic Controls

fun JFrame.button(text: String? = null, icon: Icon? = null, block: JButton.() -> Unit = {}): JButton =
    opcr(JButton(text, icon), block)

fun JFrame.checkbox(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JCheckBox.() -> Unit = {}): JCheckBox =
    opcr(JCheckBox(text, icon, selected), block)

fun <T> JFrame.combobox(block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox<T>(), block)
fun <T> JFrame.combobox(array: Array<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox<T>(array), block)
fun <T> JFrame.combobox(vector: Vector<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox(vector), block)
fun <T> JFrame.combobox(comboBoxModel: ComboBoxModel<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> = opcr(JComboBox(comboBoxModel), block)
fun <T> JFrame.combobox(list: List<T>, block: JComboBox<T>.() -> Unit): JComboBox<T> =
    opcr(JComboBox(ListComboBoxModel(if (list is MutableList<*>) list as MutableList<T> else ArrayList(list))), block)

fun <T> JFrame.list(block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(), block)
fun <T> JFrame.list(array: Array<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(array), block)
fun <T> JFrame.list(vector: Vector<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList<T>(vector), block)
fun <T> JFrame.list(listModel: ListModel<T>, block: JList<T>.() -> Unit = {}): JList<T> = opcr(JList(listModel), block)
fun <T> JFrame.list(list: List<T>, block: JList<T>.() -> Unit = {}): JList<T> {
    val listModel = object : AbstractListModel<T>() {
        override fun getSize(): Int = list.size
        override fun getElementAt(index: Int): T = list[index]
    }
    return opcr(JList<T>(listModel), block)
}

fun JFrame.radiobutton(text: String? = null, icon: Icon? = null, selected: Boolean = false, block: JRadioButton.() -> Unit = {}): JRadioButton =
    opcr(JRadioButton(text, icon, selected), block)

fun JFrame.textfield(text: String? = null, columns: Int = 0, document: Document? = null, block: JTextField.() -> Unit = {}): JTextField =
    opcr(JTextField(document, text, columns), block)

fun JFrame.passwordfield(
    text: String? = null,
    columns: Int = 0,
    document: Document? = null,
    block: JPasswordField.() -> Unit = {}
): JPasswordField = opcr(JPasswordField(document, text, columns), block)

fun JFrame.textarea(
    text: String? = null,
    row: Int = 0,
    columns: Int = 0,
    document: Document? = null,
    block: JTextArea.() -> Unit = {}
): JTextArea =
    opcr(JTextArea(document, text, row, columns), block)

fun JFrame.spinner(model: SpinnerModel = numberModel(), block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(model), block)

fun JFrame.datespinner(
    value: Date = Date(),
    start: Comparable<Date>? = null,
    end: Comparable<Date>? = null,
    calendarField: Int = Calendar.DAY_OF_MONTH,
    block: JSpinner.() -> Unit = {}
): JSpinner = opcr(JSpinner(SpinnerDateModel(value, start, end, calendarField)), block)

fun JFrame.numberspinner(
    initialValue: Number = 0,
    minimumValue: Comparable<Number>? = null,
    maximumValue: Comparable<Number>? = null,
    stepValue: Number = 1,
    block: JSpinner.() -> Unit = {}
): JSpinner = opcr(JSpinner(SpinnerNumberModel(initialValue, minimumValue, maximumValue, stepValue)), block)

fun JFrame.listspinner(list: List<*>, block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(SpinnerListModel(list)), block)
fun JFrame.listspinner(array: Array<*>, block: JSpinner.() -> Unit = {}): JSpinner = opcr(JSpinner(SpinnerListModel(array)), block)

fun JFrame.slider(
    @MagicConstant(
        intValues = [JSlider.HORIZONTAL.toLong(), JSlider.VERTICAL.toLong()],
        valuesFromClass = JSlider::class
    ) alignment: Int = JSlider.HORIZONTAL,
    minimumValue: Int = 0,
    maximumValue: Int = 100,
    initialValue: Int = 50,
    block: JSlider.() -> Unit = {}
): JSlider = opcr(JSlider(alignment, minimumValue, maximumValue, initialValue), block)

fun JFrame.label(
    text: String? = null, icon: Icon? = null,
    @MagicConstant(valuesFromClass = JLabel::class) alignment: Int = JLabel.LEADING, block: JLabel.() -> Unit = {}
): JLabel = opcr(JLabel(text, icon, alignment), block)

fun JFrame.progressbar(orient: Int = JProgressBar.HORIZONTAL, min: Int = 0, max: Int = 100, block: JProgressBar.() -> Unit = {}): JProgressBar =
    opcr(JProgressBar(orient, min, max), block)

fun JFrame.progressbar(boundedRangeModel: BoundedRangeModel, block: JProgressBar.() -> Unit = {}): JProgressBar =
    opcr(JProgressBar(boundedRangeModel), block)

fun JFrame.sep(orientation: Int = JSeparator.HORIZONTAL, block: JSeparator.() -> Unit = {}): JSeparator = opcr(JSeparator(orientation), block)

fun JFrame.tooltip(block: JToolTip.() -> Unit = {}): JToolTip = opcr(JToolTip(), block)

fun JFrame.colorchooser(initialColor: Color = Color.WHITE, block: JColorChooser.() -> Unit = {}): JColorChooser =
    opcr(JColorChooser(initialColor), block)

fun JFrame.colorchooser(colorSelectionModel: ColorSelectionModel, block: JColorChooser.() -> Unit = {}): JColorChooser =
    opcr(JColorChooser(colorSelectionModel), block)

fun JFrame.filechooser(initialFile: File? = null, fileSystemView: FileSystemView? = null, block: JFileChooser.() -> Unit = {}): JFileChooser =
    opcr(JFileChooser(initialFile, fileSystemView), block)

fun JFrame.filechooser(initialFile: String? = null, fileSystemView: FileSystemView? = null, block: JFileChooser.() -> Unit = {}): JFileChooser =
    opcr(JFileChooser(initialFile, fileSystemView), block)

fun JFrame.table(
    tableModel: TableModel? = null,
    tableColumnModel: TableColumnModel? = null,
    listSelectionModel: ListSelectionModel? = null,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(tableModel, tableColumnModel, listSelectionModel), block)

fun JFrame.table(
    rows: Int = 0,
    cols: Int = 0,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(rows, cols), block)

fun JFrame.table(
    data: Vector<out Vector<*>>,
    columnNames: Vector<*>,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(data, columnNames), block)

fun JFrame.table(
    data: Array<out Array<*>>,
    columnNames: Array<*>,
    block: JTable.() -> Unit = {}
): JTable = opcr(JTable(data, columnNames), block)

inline fun <reified T> JFrame.table(
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

fun JFrame.tree(values: Array<*>, block: JTree.() -> Unit = {}): JTree = opcr(JTree(values), block)
fun JFrame.tree(values: Collection<*>, block: JTree.() -> Unit = {}): JTree = opcr(JTree(values.toTypedArray()), block)
fun JFrame.tree(values: Map<*, *>, block: JTree.() -> Unit = {}): JTree {
    return if (values is Hashtable) {
        opcr(JTree(values), block)
    } else {
        opcr(JTree(Hashtable(values)), block)
    }
}

fun JFrame.tree(treeModel: TreeModel? = null, block: JTree.() -> Unit = {}): JTree =
    opcr(if (treeModel == null) JTree() else JTree(treeModel), block)

fun JFrame.tree(root: TreeNode, childLeaves: Boolean = false, block: JTree.() -> Unit = {}): JTree =
    opcr(JTree(root, childLeaves), block)

fun JFrame.tree(value: Any? = null, allowsChildren: Boolean = true, block: DefaultMutableTreeNode.() -> Unit = {}): JTree =
    opcr(JTree(createNode(value, allowsChildren, block)))

// Panes

fun JFrame.panel(layout: Layout<*>? = null, block: JPanel.() -> Unit = {}): JPanel {
    val panel = JPanel()
    panel.layout = layout?.createLayout(panel) ?: FlowLayout()
    return opcr(panel, block)
}

fun JFrame.tabbedpane(
    @MagicConstant(valuesFromClass = JTabbedPane::class)
    placement: Int = JTabbedPane.TOP,
    @MagicConstant(valuesFromClass = JTabbedPane::class)
    layoutPolicy: Int = JTabbedPane.WRAP_TAB_LAYOUT,
    block: JTabbedPane.() -> Unit = {}
): JTabbedPane = opcr(JTabbedPane(placement, layoutPolicy), block)

fun JFrame.scrollpane(
    @MagicConstant(valuesFromClass = ScrollPaneConstants::class)
    verticalScrollBarPolicy: Int = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    @MagicConstant(valuesFromClass = ScrollPaneConstants::class)
    horizontalScrollBarPolicy: Int = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,
    component: JFrame? = null,
    block: JScrollPane.() -> Unit = {}
): JScrollPane = opcr(JScrollPane(component, verticalScrollBarPolicy, horizontalScrollBarPolicy), block)

fun JFrame.splitpane(
    leftComponent: JComponent? = null,
    rightComponent: JComponent? = null,
    @MagicConstant(valuesFromClass = JSplitPane::class)
    orientation: Int = JSplitPane.HORIZONTAL_SPLIT,
    continuouslyResize: Boolean = UIManager.getBoolean("SplitPane.continuousLayout"),
    block: JSplitPane.() -> Unit = {}
): JSplitPane =
    opcr(JSplitPane(orientation, continuouslyResize, leftComponent, rightComponent), block)


fun JFrame.toolbar(
    name: String? = null,
    @MagicConstant(valuesFromClass = SwingConstants::class)
    orientation: Int = SwingConstants.HORIZONTAL,
    block: JToolBar.() -> Unit = {}
): JToolBar =
    opcr(JToolBar(name, orientation), block)

fun JFrame.editorpane(
    url: String? = null,
    type: String? = null,
    text: String? = null,
    block: JEditorPane.() -> Unit = {}
): JEditorPane = opcr(JEditorPane(), block).apply {
    if (url != null) page = URL(url)
    if (type != null) contentType = type
    if (text != null) setText(text)
}

fun JFrame.textpane(document: StyledDocument? = null, block: JTextPane.() -> Unit): JTextPane = opcr(JTextPane(document), block)

@Suppress("SpellCheckingInspection")
fun JFrame.createInternalFrame(
    title: String = "", resizable: Boolean = false, closable: Boolean = false,
    maximizable: Boolean = false, iconifiable: Boolean = false, block: JInternalFrame.() -> Unit = {}
): JInternalFrame = opcr(JInternalFrame(title, resizable, closable, maximizable, iconifiable), block)

@Suppress("SpellCheckingInspection")
fun JComponent.createInternalFrame(
    title: String = "", resizable: Boolean = false, closable: Boolean = false,
    maximizable: Boolean = false, iconifiable: Boolean = false, block: JInternalFrame.() -> Unit = {}
): JInternalFrame = opcr(JInternalFrame(title, resizable, closable, maximizable, iconifiable), block)