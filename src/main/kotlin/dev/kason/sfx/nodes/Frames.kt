package dev.kason.sfx.nodes

import org.intellij.lang.annotations.MagicConstant
import java.awt.*
import java.awt.dnd.DropTarget
import java.awt.event.*
import java.awt.im.InputContext
import java.awt.im.InputMethodRequests
import java.awt.image.ColorModel
import java.awt.image.ImageObserver
import java.awt.image.ImageProducer
import java.awt.image.VolatileImage
import java.beans.PropertyChangeListener
import java.io.PrintStream
import java.io.PrintWriter
import java.util.*
import javax.accessibility.AccessibleContext
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
    override fun componentHidden(e: ComponentEvent) = if (closeOperation == WindowConstants.HIDE_ON_CLOSE) block() else Unit
})

val JFrame.wrapped: JComponent get() = FrameWrapper(this)

// Wrap functions
fun JFrame.panel(layout: Layout<*>? = null, block: JPanel.() -> Unit = {}) = wrapped.panel(layout, block)

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

class FrameWrapper(private val frame: JFrame) : JComponent() {
    override fun update(g: Graphics) = frame.update(g)
    override fun paint(g: Graphics) = frame.paint(g)
    override fun printAll(g: Graphics) = frame.printAll(g)
    override fun print(g: Graphics) = frame.print(g)
    override fun requestFocus() = frame.requestFocus()
    override fun requestFocusInWindow(): Boolean = frame.requestFocusInWindow()
    override fun getFontMetrics(font: Font): FontMetrics = frame.getFontMetrics(font)
    override fun setPreferredSize(preferredSize: Dimension) = run { frame.preferredSize = preferredSize }
    override fun getPreferredSize(): Dimension = frame.preferredSize
    override fun setMaximumSize(maximumSize: Dimension) = run { frame.maximumSize = maximumSize }
    override fun getMaximumSize(): Dimension = frame.maximumSize
    override fun setMinimumSize(minimumSize: Dimension) = run { frame.minimumSize = minimumSize }
    override fun getMinimumSize(): Dimension = frame.minimumSize
    override fun contains(x: Int, y: Int): Boolean = frame.contains(x, y)
    override fun getInsets(): Insets = frame.insets
    override fun getAlignmentY(): Float = frame.alignmentY
    override fun getAlignmentX(): Float = frame.alignmentX
    override fun getGraphics(): Graphics = frame.graphics
    override fun getBaseline(width: Int, height: Int): Int = frame.getBaseline(width, height)
    override fun getBaselineResizeBehavior(): BaselineResizeBehavior = frame.baselineResizeBehavior
    override fun setVisible(aFlag: Boolean) = run { frame.isVisible = aFlag }
    override fun setEnabled(enabled: Boolean) = run { frame.isEnabled = enabled }
    override fun setForeground(fg: Color) = run { frame.foreground = fg }
    override fun setBackground(bg: Color) = run { frame.background = bg }
    override fun setFont(font: Font) = run { frame.font = font }
    override fun setTransferHandler(newHandler: TransferHandler) = run { frame.transferHandler = newHandler }
    override fun getTransferHandler(): TransferHandler = frame.transferHandler
    override fun setFocusTraversalKeys(id: Int, keystrokes: Set<AWTKeyStroke>) = frame.setFocusTraversalKeys(id, keystrokes)
    override fun getBounds(rv: Rectangle): Rectangle = frame.getBounds(rv)
    override fun getSize(rv: Dimension): Dimension = frame.getSize(rv)
    override fun getLocation(rv: Point): Point = frame.getLocation(rv)
    override fun getX(): Int = frame.x
    override fun getY(): Int = frame.y
    override fun getWidth(): Int = frame.width
    override fun getHeight(): Int = frame.height
    override fun isOpaque(): Boolean = frame.isOpaque
    override fun firePropertyChange(propertyName: String, oldValue: Int, newValue: Int) =
        frame.firePropertyChange(propertyName, oldValue.toLong(), newValue.toLong())

    override fun firePropertyChange(propertyName: String, oldValue: Char, newValue: Char) = frame.firePropertyChange(propertyName, oldValue, newValue)
    override fun <T : EventListener?> getListeners(listenerType: Class<T>): Array<T> = frame.getListeners(listenerType)
    override fun addNotify() = frame.addNotify()
    override fun removeNotify() = frame.removeNotify()
    override fun repaint(tm: Long, x: Int, y: Int, width: Int, height: Int) = frame.repaint(tm, x, y, width, height)
    override fun revalidate() = frame.revalidate()
    override fun isValidateRoot(): Boolean = frame.isValidateRoot
    override fun isDoubleBuffered(): Boolean = frame.isDoubleBuffered
    override fun getRootPane(): JRootPane = frame.rootPane
    override fun getComponentCount(): Int = frame.componentCount
    override fun getComponent(n: Int): Component = frame.getComponent(n)
    override fun getComponents(): Array<Component> = frame.components
    override fun add(comp: Component): Component = frame.add(comp)
    override fun add(name: String, comp: Component): Component = frame.add(name, comp)
    override fun add(comp: Component, index: Int): Component = frame.add(comp, index)
    override fun setComponentZOrder(comp: Component, index: Int) = frame.setComponentZOrder(comp, index)
    override fun getComponentZOrder(comp: Component): Int = frame.getComponentZOrder(comp)
    override fun add(comp: Component, constraints: Any) = frame.add(comp, constraints)
    override fun add(comp: Component, constraints: Any, index: Int) = frame.add(comp, constraints, index)
    override fun remove(index: Int) = frame.remove(index)
    override fun remove(comp: Component) = frame.remove(comp)
    override fun removeAll() = frame.removeAll()
    override fun getLayout(): LayoutManager = frame.layout
    override fun setLayout(mgr: LayoutManager) = run { frame.layout = mgr }
    override fun doLayout() = frame.doLayout()
    override fun invalidate() = frame.invalidate()
    override fun validate() = frame.validate()
    override fun paintComponents(g: Graphics) = frame.paintComponents(g)
    override fun printComponents(g: Graphics) = frame.printComponents(g)

    @Synchronized
    override fun addContainerListener(l: ContainerListener) = frame.addContainerListener(l)

    @Synchronized
    override fun removeContainerListener(l: ContainerListener) = frame.removeContainerListener(l)

    @Synchronized
    override fun getContainerListeners(): Array<ContainerListener> = frame.containerListeners
    override fun getComponentAt(x: Int, y: Int): Component = frame.getComponentAt(x, y)
    override fun getComponentAt(p: Point): Component = frame.getComponentAt(p)

    @Throws(HeadlessException::class)
    override fun getMousePosition(allowChildren: Boolean): Point = frame.getMousePosition(allowChildren)
    override fun findComponentAt(x: Int, y: Int): Component = frame.findComponentAt(x, y)
    override fun findComponentAt(p: Point): Component = frame.findComponentAt(p)
    override fun isAncestorOf(c: Component): Boolean = frame.isAncestorOf(c)
    override fun list(out: PrintStream, indent: Int) = frame.list(out, indent)
    override fun list(out: PrintWriter, indent: Int) = frame.list(out, indent)
    override fun getFocusTraversalKeys(id: Int): Set<AWTKeyStroke> = frame.getFocusTraversalKeys(id)
    override fun areFocusTraversalKeysSet(id: Int): Boolean = frame.areFocusTraversalKeysSet(id)
    override fun isFocusCycleRoot(container: Container): Boolean = frame.isFocusCycleRoot(container)
    override fun setFocusTraversalPolicy(policy: FocusTraversalPolicy) = run { frame.focusTraversalPolicy = policy }
    override fun getFocusTraversalPolicy(): FocusTraversalPolicy = frame.focusTraversalPolicy
    override fun isFocusTraversalPolicySet(): Boolean = frame.isFocusTraversalPolicySet
    override fun setFocusCycleRoot(focusCycleRoot: Boolean) = run { frame.isFocusCycleRoot = focusCycleRoot }
    override fun isFocusCycleRoot(): Boolean = frame.isFocusCycleRoot
    override fun transferFocusDownCycle() = frame.transferFocusDownCycle()
    override fun applyComponentOrientation(o: ComponentOrientation) = frame.applyComponentOrientation(o)
    override fun addPropertyChangeListener(listener: PropertyChangeListener) = frame.addPropertyChangeListener(listener)
    override fun addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener) =
        frame.addPropertyChangeListener(propertyName, listener)

    override fun getName(): String = frame.name
    override fun setName(name: String) = run { frame.name = name }
    override fun getParent(): Container = frame.parent

    @Synchronized
    override fun setDropTarget(dt: DropTarget) = run { frame.dropTarget = dt }

    @Synchronized
    override fun getDropTarget(): DropTarget = frame.dropTarget
    override fun getGraphicsConfiguration(): GraphicsConfiguration = frame.graphicsConfiguration
    override fun getToolkit(): Toolkit = frame.toolkit
    override fun isValid(): Boolean = false // Needs
    override fun isDisplayable(): Boolean = frame.isDisplayable
    override fun isVisible(): Boolean = frame.isVisible

    @Throws(HeadlessException::class)
    override fun getMousePosition(): Point = frame.mousePosition
    override fun isShowing(): Boolean = frame.isShowing
    override fun isEnabled(): Boolean = frame.isEnabled
    override fun enableInputMethods(enable: Boolean) = frame.enableInputMethods(enable)
    override fun getForeground(): Color = frame.foreground
    override fun isForegroundSet(): Boolean = frame.isForegroundSet
    override fun getBackground(): Color = frame.background
    override fun isBackgroundSet(): Boolean = frame.isBackgroundSet
    override fun getFont(): Font = frame.font
    override fun isFontSet(): Boolean = frame.isFontSet
    override fun getLocale(): Locale = frame.locale
    override fun setLocale(l: Locale) = run { frame.locale = l }
    override fun getColorModel(): ColorModel = frame.colorModel
    override fun getLocation(): Point = frame.location
    override fun getLocationOnScreen(): Point = frame.locationOnScreen
    override fun setLocation(x: Int, y: Int) = frame.setLocation(x, y)
    override fun setLocation(p: Point) = run { frame.location = p }
    override fun getSize(): Dimension = frame.size
    override fun setSize(width: Int, height: Int) = frame.setSize(width, height)
    override fun setSize(d: Dimension) = run { frame.size = d }
    override fun getBounds(): Rectangle = frame.bounds
    override fun setBounds(x: Int, y: Int, width: Int, height: Int) = frame.setBounds(x, y, width, height)
    override fun setBounds(r: Rectangle) = run { frame.bounds = r }
    override fun isLightweight(): Boolean = frame.isLightweight
    override fun isPreferredSizeSet(): Boolean = frame.isPreferredSizeSet
    override fun isMinimumSizeSet(): Boolean = frame.isMinimumSizeSet
    override fun isMaximumSizeSet(): Boolean = frame.isMaximumSizeSet
    override fun setCursor(cursor: Cursor) = run { frame.cursor = cursor }
    override fun getCursor(): Cursor = frame.cursor
    override fun isCursorSet(): Boolean = frame.isCursorSet
    override fun paintAll(g: Graphics) = frame.paintAll(g)
    override fun repaint() = frame.repaint()
    override fun repaint(tm: Long) = frame.repaint(tm)
    override fun repaint(x: Int, y: Int, width: Int, height: Int) = frame.repaint(x, y, width, height)
    override fun imageUpdate(img: Image, infoflags: Int, x: Int, y: Int, w: Int, h: Int): Boolean = frame.imageUpdate(img, infoflags, x, y, w, h)
    override fun createImage(producer: ImageProducer): Image = frame.createImage(producer)
    override fun createImage(width: Int, height: Int): Image = frame.createImage(width, height)
    override fun createVolatileImage(width: Int, height: Int): VolatileImage = frame.createVolatileImage(width, height)

    @Throws(AWTException::class)
    override fun createVolatileImage(width: Int, height: Int, caps: ImageCapabilities): VolatileImage = frame.createVolatileImage(width, height, caps)
    override fun prepareImage(image: Image, observer: ImageObserver): Boolean = frame.prepareImage(image, observer)
    override fun prepareImage(image: Image, width: Int, height: Int, observer: ImageObserver): Boolean =
        frame.prepareImage(image, width, height, observer)

    override fun checkImage(image: Image, observer: ImageObserver): Int = frame.checkImage(image, observer)
    override fun checkImage(image: Image, width: Int, height: Int, observer: ImageObserver): Int = frame.checkImage(image, width, height, observer)
    override fun setIgnoreRepaint(ignoreRepaint: Boolean) = run { frame.ignoreRepaint = ignoreRepaint }
    override fun getIgnoreRepaint(): Boolean = frame.ignoreRepaint
    override fun contains(p: Point): Boolean = frame.contains(p)

    @Synchronized
    override fun addComponentListener(l: ComponentListener) = frame.addComponentListener(l)

    @Synchronized
    override fun removeComponentListener(l: ComponentListener) = frame.removeComponentListener(l)

    @Synchronized
    override fun getComponentListeners(): Array<ComponentListener> = frame.componentListeners

    @Synchronized
    override fun addFocusListener(l: FocusListener) = frame.addFocusListener(l)

    @Synchronized
    override fun removeFocusListener(l: FocusListener) = frame.removeFocusListener(l)

    @Synchronized
    override fun getFocusListeners(): Array<FocusListener> = frame.focusListeners
    override fun addHierarchyListener(l: HierarchyListener) = frame.addHierarchyListener(l)
    override fun removeHierarchyListener(l: HierarchyListener) = frame.removeHierarchyListener(l)

    @Synchronized
    override fun getHierarchyListeners(): Array<HierarchyListener> = frame.hierarchyListeners
    override fun addHierarchyBoundsListener(l: HierarchyBoundsListener) = frame.addHierarchyBoundsListener(l)
    override fun removeHierarchyBoundsListener(l: HierarchyBoundsListener) = frame.removeHierarchyBoundsListener(l)

    @Synchronized
    override fun getHierarchyBoundsListeners(): Array<HierarchyBoundsListener> = frame.hierarchyBoundsListeners

    @Synchronized
    override fun addKeyListener(l: KeyListener) = frame.addKeyListener(l)

    @Synchronized
    override fun removeKeyListener(l: KeyListener) = frame.removeKeyListener(l)

    @Synchronized
    override fun getKeyListeners(): Array<KeyListener> = frame.keyListeners

    @Synchronized
    override fun addMouseListener(l: MouseListener) = frame.addMouseListener(l)

    @Synchronized
    override fun removeMouseListener(l: MouseListener) = frame.removeMouseListener(l)

    @Synchronized
    override fun getMouseListeners(): Array<MouseListener> = frame.mouseListeners

    @Synchronized
    override fun addMouseMotionListener(l: MouseMotionListener) = frame.addMouseMotionListener(l)

    @Synchronized
    override fun removeMouseMotionListener(l: MouseMotionListener) = frame.removeMouseMotionListener(l)

    @Synchronized
    override fun getMouseMotionListeners(): Array<MouseMotionListener> = frame.mouseMotionListeners

    @Synchronized
    override fun addMouseWheelListener(l: MouseWheelListener) = frame.addMouseWheelListener(l)

    @Synchronized
    override fun removeMouseWheelListener(l: MouseWheelListener) = frame.removeMouseWheelListener(l)

    @Synchronized
    override fun getMouseWheelListeners(): Array<MouseWheelListener> = frame.mouseWheelListeners

    @Synchronized
    override fun addInputMethodListener(l: InputMethodListener) = frame.addInputMethodListener(l)

    @Synchronized
    override fun removeInputMethodListener(l: InputMethodListener) = frame.removeInputMethodListener(l)

    @Synchronized
    override fun getInputMethodListeners(): Array<InputMethodListener> = frame.inputMethodListeners
    override fun getInputMethodRequests(): InputMethodRequests = frame.inputMethodRequests
    override fun getInputContext(): InputContext = frame.inputContext
    override fun isFocusable(): Boolean = frame.isFocusable
    override fun setFocusable(focusable: Boolean) = run { frame.isFocusable = focusable }
    override fun setFocusTraversalKeysEnabled(focusTraversalKeysEnabled: Boolean) =
        run { frame.focusTraversalKeysEnabled = focusTraversalKeysEnabled }

    override fun getFocusTraversalKeysEnabled(): Boolean = frame.focusTraversalKeysEnabled
    override fun requestFocus(cause: FocusEvent.Cause) = frame.requestFocus(cause)
    override fun requestFocusInWindow(cause: FocusEvent.Cause): Boolean = frame.requestFocusInWindow(cause)
    override fun getFocusCycleRootAncestor(): Container = frame.focusCycleRootAncestor
    override fun transferFocus() = frame.transferFocus()
    override fun transferFocusBackward() = frame.transferFocusBackward()
    override fun transferFocusUpCycle() = frame.transferFocusUpCycle()
    override fun hasFocus(): Boolean = frame.hasFocus()
    override fun isFocusOwner(): Boolean = frame.isFocusOwner
    override fun add(popup: PopupMenu) = frame.add(popup)
    override fun remove(popup: MenuComponent) = frame.remove(popup)
    override fun toString(): String = frame.toString()
    override fun list() = frame.list()
    override fun list(out: PrintStream) = frame.list(out)
    override fun list(out: PrintWriter) = frame.list(out)
    override fun removePropertyChangeListener(listener: PropertyChangeListener) = frame.removePropertyChangeListener(listener)
    override fun getPropertyChangeListeners(): Array<PropertyChangeListener> = frame.propertyChangeListeners
    override fun removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener) =
        frame.removePropertyChangeListener(propertyName, listener)

    override fun getPropertyChangeListeners(propertyName: String): Array<PropertyChangeListener> = frame.getPropertyChangeListeners(propertyName)
    override fun firePropertyChange(propertyName: String, oldValue: Byte, newValue: Byte) = frame.firePropertyChange(propertyName, oldValue, newValue)
    override fun firePropertyChange(propertyName: String, oldValue: Short, newValue: Short) =
        frame.firePropertyChange(propertyName, oldValue, newValue)

    override fun firePropertyChange(propertyName: String, oldValue: Long, newValue: Long) = frame.firePropertyChange(propertyName, oldValue, newValue)
    override fun firePropertyChange(propertyName: String, oldValue: Float, newValue: Float) =
        frame.firePropertyChange(propertyName, oldValue, newValue)

    override fun firePropertyChange(propertyName: String, oldValue: Double, newValue: Double) =
        frame.firePropertyChange(propertyName, oldValue, newValue)

    override fun setComponentOrientation(o: ComponentOrientation) = run { frame.componentOrientation = o }
    override fun getComponentOrientation(): ComponentOrientation = frame.componentOrientation
    override fun getAccessibleContext(): AccessibleContext = frame.accessibleContext
    override fun setMixingCutoutShape(shape: Shape) = frame.setMixingCutoutShape(shape)
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as FrameWrapper
        return frame == that.frame
    }

    override fun hashCode(): Int = Objects.hash(frame)
}