@file:Suppress("unused")

package dev.kason.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.*
import javax.swing.*

val border get() = Layout { BorderLayout() }

fun border(horizontalGap: Number, verticalGap: Number) = Layout { BorderLayout(horizontalGap.toInt(), verticalGap.toInt()) }

fun box(@MagicConstant(valuesFromClass = BoxLayout::class) axis: Int) =
    Layout { BoxLayout(it ?: throw IllegalArgumentException("Panel is null"), axis) }

val card get() = Layout { CardLayout() }

fun card(horizontalGap: Number, verticalGap: Number) = Layout { CardLayout(horizontalGap.toInt(), verticalGap.toInt()) }

val flow get() = Layout { FlowLayout() }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class) align: Int) =
    Layout { FlowLayout(align) }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class) align: Int, horizontalGap: Number, verticalGap: Number) =
    Layout { FlowLayout(align, horizontalGap.toInt(), verticalGap.toInt()) }

val gridBag get() = Layout { GridBagLayout() }

val grid get() = Layout { GridLayout() }

fun grid(rows: Number, cols: Number) = Layout { GridLayout(rows.toInt(), cols.toInt()) }

fun grid(rows: Number, cols: Number, horizontalGap: Number, verticalGap: Number) =
    Layout { GridLayout(rows.toInt(), cols.toInt(), horizontalGap.toInt(), verticalGap.toInt()) }

val group get() = Layout { GroupLayout(it ?: throw IllegalArgumentException("Panel is null")) }

val spring get() = Layout { SpringLayout() }

class Layout<T> internal constructor(private val block: (JPanel?) -> T) where T : LayoutManager {
    fun createLayout(panel: JPanel? = null): T = block(panel)
    override fun equals(other: Any?): Boolean = other != null && other is Layout<*> && other.block == block
    override fun hashCode(): Int = block.hashCode()
    override fun toString(): String = "Layout{block=$block}"
}

inline fun <reified T> JPanel.layout(block: T.() -> Unit) where T : LayoutManager {
    if (layout is T) {
        block(layout as T)
    }
}

// Border Extensions

fun JComponent.left(block: JComponent.() -> Unit = {}) = manage(block, BorderLayout.WEST)
fun JComponent.bottom(block: JComponent.() -> Unit = {}) = manage(block, BorderLayout.SOUTH)
fun JComponent.right(block: JComponent.() -> Unit = {}) = manage(block, BorderLayout.EAST)
fun JComponent.top(block: JComponent.() -> Unit = {}) = manage(block, BorderLayout.NORTH)
fun JComponent.center(block: JComponent.() -> Unit = {}) = manage(block, BorderLayout.CENTER)

var JPanel.left: Component?
    get() = (layout as? BorderLayout)?.getLayoutComponent(BorderLayout.WEST)
    set(value) = run { if (value != null) add(value, BorderLayout.WEST) }
var JPanel.bottom: Component?
    get() = (layout as? BorderLayout)?.getLayoutComponent(BorderLayout.SOUTH)
    set(value) = run { if (value != null) add(value, BorderLayout.SOUTH) }
var JPanel.right: Component?
    get() = (layout as? BorderLayout)?.getLayoutComponent(BorderLayout.EAST)
    set(value) = run { if (value != null) add(value, BorderLayout.EAST) }
var JPanel.top: Component?
    get() = (layout as? BorderLayout)?.getLayoutComponent(BorderLayout.NORTH)
    set(value) = run { if (value != null) add(value, BorderLayout.NORTH) }
var JPanel.center: Component?
    get() = (layout as? BorderLayout)?.getLayoutComponent(BorderLayout.CENTER)
    set(value) = run { if (value != null) add(value, BorderLayout.CENTER) }

internal var currentLocation: String? = null

internal fun JComponent.manage(block: JComponent.() -> Unit, location: String) {
    if (layout !is BorderLayout) {
        block(this)
        return
    }
    val previous = currentLocation
    currentLocation = location
    block(this)
    currentLocation = previous
}
// Box extension

fun JComponent.verticalStrut(width: Number): Component = Box.createVerticalStrut(width.toInt())
fun JComponent.horizontalStrut(width: Number): Component = Box.createHorizontalStrut(width.toInt())
fun JComponent.verticalGlue(): Component = Box.createVerticalGlue()
fun JComponent.horizontalGlue(): Component = Box.createHorizontalGlue()

operator fun JComponent.plusAssign(value: Pair<JComponent, Number>) = add(value.first, value.second)

// Card Extensions

