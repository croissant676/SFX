package dev.kason.sfx

import java.awt.Component
import java.awt.PopupMenu
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.table.TableColumn

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

private object DefineComponent : JComponent() {
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
    block(DefineComponent)
}

// Spacing

val JTable.columns: List<TableColumn> get() = this.columnModel.columns.toList()
fun JTable.forEachColumn(block: TableColumn.() -> Unit) = columns.forEach { block(it) }