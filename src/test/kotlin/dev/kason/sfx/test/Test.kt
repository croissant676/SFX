package dev.kason.sfx.test

import dev.kason.sfx.*
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