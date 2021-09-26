@file:Suppress("unused")

package io.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.FlowLayout
import java.awt.LayoutManager
import javax.swing.BoxLayout
import javax.swing.JPanel
import kotlin.properties.Delegates

val borderLayout
    get() = Layout("Border") {
        BorderLayout()
    }

fun borderLayout(width: Number, height: Number) = Layout("Border") {
    BorderLayout(width.toInt(), height.toInt())
}

fun boxLayout(axis: Axis) = AttachingLayout("Box") {
    BoxLayout(this, axis.value)
}

@MagicConstant(valuesFromClass = BoxLayout::class)
fun boxLayout(axis: Int) = AttachingLayout("Box") {
    BoxLayout(this, axis)
}

val cardLayout
    get() = Layout("Card") {
        CardLayout()
    }

fun cardLayout(width: Number, height: Number) = Layout("Card") {
    CardLayout(width.toInt(), height.toInt())
}

val flowLayout
    get() = Layout("Flow") {
        FlowLayout()
    }

fun flowLayout(align: FlowAlign) = Layout("Flow") {
    FlowLayout(align.value)
}

@MagicConstant(valuesFromClass = FlowLayout::class)
fun flowLayout(align: Int) = Layout("Flow") {
    FlowLayout(align)
}

fun flowLayout(align: FlowAlign, width: Number, height: Number) = Layout("Flow") {
    FlowLayout(align.value, width.toInt(), height.toInt())
}

@MagicConstant(valuesFromClass = FlowLayout::class)
fun flowLayout(align: Int, width: Number, height: Number) = Layout("Flow") {
    FlowLayout(align, width.toInt(), height.toInt())
}



open class Layout<T> where T : LayoutManager {

    internal var layoutName: String by Delegates.notNull()
    private var block: () -> T by Delegates.notNull()

    internal constructor(layout: String, block: () -> T) {
        this.layoutName = layout
        this.block = block
    }

    protected constructor(layout: String) {
        layoutName = layout
    }

    open fun create(parameter: Any?): T? = block()

    override fun equals(other: Any?): Boolean {
        return other != null && other is Layout<*> && layoutName == other.layoutName
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + block.hashCode()
        return result
    }

    override fun toString(): String = "Layout{name=$layoutName}"
}

class AttachingLayout<T> internal constructor(layoutName: String, val block: JPanel.() -> T) : Layout<T>(layoutName) where T : LayoutManager {

    override fun create(parameter: Any?): T? {
        if (parameter == null || parameter !is JPanel) {
            return null
        }
        return block(parameter)
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is AttachingLayout<*> && layoutName == other.layoutName
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + block.hashCode()
        return result
    }

    override fun toString(): String = "Layout{name=$layoutName}"
}

enum class Axis(val value: Int) {
    X(BoxLayout.X_AXIS),
    Y(BoxLayout.Y_AXIS),
    LINE(BoxLayout.LINE_AXIS),
    PAGE(BoxLayout.PAGE_AXIS);

    override fun toString(): String {
        return "Axis{name=$name, value=$value}"
    }
}

enum class FlowAlign(val value: Int) {
    LEFT(0),
    CENTER(1),
    RIGHT(2),
    LEADING(3),
    TRAILING(4);

    override fun toString(): String {
        return "FlowAlign{name=$name, value=$value}"
    }
}