package dev.kason.sfx.nodes

import org.intellij.lang.annotations.MagicConstant
import java.awt.*
import javax.swing.*

val border get() = Layout() { BorderLayout() }

fun border(horizontalGap: Number, verticalGap: Number) = Layout() { BorderLayout(horizontalGap.toInt(), verticalGap.toInt()) }

fun box(@MagicConstant(valuesFromClass = BoxLayout::class) axis: Int) =
    Layout() { BoxLayout(it ?: throw IllegalArgumentException("Panel is null"), axis) }

val card get() = Layout() { CardLayout() }

fun card(horizontalGap: Number, verticalGap: Number) = Layout() { CardLayout(horizontalGap.toInt(), verticalGap.toInt()) }

val flow get() = Layout() { FlowLayout() }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class) align: Int) =
    Layout() { FlowLayout(align) }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class) align: Int, horizontalGap: Number, verticalGap: Number) =
    Layout() { FlowLayout(align, horizontalGap.toInt(), verticalGap.toInt()) }

val gridBag get() = Layout() { GridBagLayout() }

val grid get() = Layout() { GridLayout() }

fun grid(rows: Number, cols: Number) = Layout() { GridLayout(rows.toInt(), cols.toInt()) }

fun grid(rows: Number, cols: Number, horizontalGap: Number, verticalGap: Number) =
    Layout() { GridLayout(rows.toInt(), cols.toInt(), horizontalGap.toInt(), verticalGap.toInt()) }

val group get() = Layout() { GroupLayout(it ?: throw IllegalArgumentException("Panel is null")) }

val spring get() = Layout{ SpringLayout() }

class Layout<T> internal constructor(private val block: (JPanel?) -> T) where T : LayoutManager {
    fun createLayout(panel: JPanel? = null): T = block(panel)
    override fun equals(other: Any?): Boolean = other != null && other is Layout<*> && other.block == block
    override fun hashCode(): Int = block.hashCode()
    override fun toString(): String = "Layout{block=$block}"
}

inline fun <reified T> JPanel.layout(block: T.() -> Unit) where T: LayoutManager{
    if(layout is T) {
        block(layout as T)
    }
}