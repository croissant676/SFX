package dev.kason.sfx.test

import dev.kason.sfx.action
import dev.kason.sfx.button
import dev.kason.sfx.closeOperation
import dev.kason.sfx.createInternalFrame
import dev.kason.sfx.dim
import dev.kason.sfx.frame
import dev.kason.sfx.onClose
import dev.kason.sfx.panel
import javax.swing.JFrame

@Suppress("SpellCheckingInspection")
fun main() {
    frame("Hello") {
        panel {
            createInternalFrame("sdf", resizable = true) {
                isVisible = true
                button ("Press me!"){
                    action {
                        println("yeet")
                    }
                }
                size = dim(500, 500)
            }
        }
        closeOperation = JFrame.EXIT_ON_CLOSE
        onClose {
            println("Window closed!")
        }
        size = dim(100, 100)
    }
}
