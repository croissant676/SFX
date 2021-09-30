package dev.kason.sfx.test

import dev.kason.sfx.*
import kotlin.system.exitProcess

fun main() {
    frame("Hello") {
        panel {
            button("asdf") {
                action {
                    println("nice")
                }
            }
        }
        onClose {
            exitProcess(1)
        }
        size = dim(100, 100)
    }
}