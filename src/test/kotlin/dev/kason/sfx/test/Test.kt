package dev.kason.sfx.test

import dev.kason.sfx.nodes.*
import javax.swing.BoxLayout

fun main(): Unit = frame("Hello") {
    panel(box(BoxLayout.X_AXIS)) {
        define {
            textfield("Enter your number here!").link = -1
            textfield("left node").link = 1
        }
        splitpane(nodes[-1], nodes[1], continuouslyResize = true)
    }
}.run {}
