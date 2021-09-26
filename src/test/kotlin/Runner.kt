import io.sfx.*

fun main() {
    frame("hello") {
        panel(boxLayout(Axis.X)) {
            button("hello") {
                action {
                    println("hello")
                }
            }
            panel(cardLayout) {

            }
        }
    }
}