package dev.kason.sfx.nodes

import org.intellij.lang.annotations.MagicConstant
import java.awt.*
import javax.swing.BoxLayout
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.SpringLayout

val border get() = Layout() { BorderLayout() }

fun border(horizontalGap: Number, verticalGap: Number) = Layout() { BorderLayout(horizontalGap.toInt(), verticalGap.toInt()) }

fun box(axis: BoxAxis) = Layout() { BoxLayout(it ?: throw IllegalArgumentException("Panel is null"), axis.value) }

fun box(@MagicConstant(valuesFromClass = BoxLayout::class, intValues = [0, 1, 2, 3]) axis: Int) =
    Layout() { BoxLayout(it ?: throw IllegalArgumentException("Panel is null"), axis) }

val card get() = Layout() { CardLayout() }

fun card(horizontalGap: Number, verticalGap: Number) = Layout() { CardLayout(horizontalGap.toInt(), verticalGap.toInt()) }

val flow get() = Layout() { FlowLayout() }

fun flow(align: FlowAlign) =
    Layout() { FlowLayout(align.value) }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class, intValues = [0, 1, 2, 3, 4]) align: Int) =
    Layout() { FlowLayout(align) }

fun flow(align: FlowAlign, horizontalGap: Number, verticalGap: Number) =
    Layout() { FlowLayout(align.value, horizontalGap.toInt(), verticalGap.toInt()) }

fun flow(@MagicConstant(valuesFromClass = FlowLayout::class, intValues = [0, 1, 2, 3, 4]) align: Int, horizontalGap: Number, verticalGap: Number) =
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

enum class BoxAxis(val value: Int) {
    X(BoxLayout.X_AXIS),
    Y(BoxLayout.Y_AXIS),
    LINE(BoxLayout.LINE_AXIS),
    PAGE(BoxLayout.PAGE_AXIS);

    override fun toString(): String = "BoxAxis{name=$name, ordinal=$ordinal, value=$value}"
}

enum class FlowAlign(val value: Int) {
    LEFT(FlowLayout.LEFT),
    CENTER(FlowLayout.CENTER),
    RIGHT(FlowLayout.RIGHT),
    LEADING(FlowLayout.LEADING),
    TRAILING(FlowLayout.TRAILING);

    override fun toString(): String = "FlowAlign{name=$name, ordinal=$ordinal, value=$value}"
}