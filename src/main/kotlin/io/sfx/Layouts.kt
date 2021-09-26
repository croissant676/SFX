@file:Suppress("unused")

package io.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.LayoutManager
import javax.swing.BoxLayout
import javax.swing.JPanel
import kotlin.properties.Delegates

val border
    get() = Layout("Border") {
        BorderLayout()
    }

fun border(width: Number, height: Number) = Layout("Border") {
    BorderLayout(width.toInt(), height.toInt())
}

fun box(axis: Axis) = AttachingLayout("Box") {
    BoxLayout(this, axis.value)
}

@MagicConstant(valuesFromClass = BoxLayout::class)
fun box(axis: Int) = AttachingLayout("Box") {
    BoxLayout(this, axis)
}

val card
    get() = Layout("Card") {
        CardLayout()
    }

fun card(width: Number, height: Number) = Layout("Card") {
    CardLayout(width.toInt(), height.toInt())
}

@Suppress("CanBeParameter")
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