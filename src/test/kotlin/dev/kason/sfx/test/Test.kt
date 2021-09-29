package dev.kason.sfx.test

import dev.kason.sfx.*
import kotlin.system.exitProcess

fun main() {
    frame("Hello") {
        panel(border) {
            left {
                button("1")
            }
            right {
                button("2")
            }
            center {
                button("3")
            }
            bottom {
                button("4")
            }
            top {
                button("5")
            }
        }
        onClose {
            exitProcess(1)
        }
        size = dim(100, 100)
    }
}