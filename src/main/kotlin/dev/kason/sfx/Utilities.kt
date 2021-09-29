package dev.kason.sfx

import org.intellij.lang.annotations.MagicConstant
import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
import java.awt.Point
import javax.swing.SwingUtilities

fun dim(dimension: Dimension) = Dimension(dimension)
fun dim(numbers: Pair<Int, Int>) = Dimension(numbers.first, numbers.second)
fun dim(numbers: Map.Entry<Int, Int>) = Dimension(numbers.key, numbers.value)
fun dim(width: Number, height: Number) = Dimension(width.toInt(), height.toInt())

fun insets(all: Number) = Insets(all.toInt(), all.toInt(), all.toInt(), all.toInt())
fun insets(vertical: Number, horizontal: Number) = Insets(vertical.toInt(), horizontal.toInt(), vertical.toInt(), horizontal.toInt())
fun insets(top: Number, left: Number, bottom: Number, right: Number) = Insets(top.toInt(), left.toInt(), bottom.toInt(), right.toInt())
fun insets(insets: Insets) = Insets(insets.top, insets.left, insets.bottom, insets.right)
fun insets(numbers: Pair<Int, Int>) = insets(numbers.first, numbers.second)
fun insets(numbers: Map.Entry<Int, Int>) = insets(numbers.key, numbers.value)

fun font(
    string: String,
    @MagicConstant(valuesFromClass = Font::class)
    style: Int,
    size: Int
): Font = font(string, style, size)

fun point(x: Number, y: Number) = Point(x.toInt(), y.toInt())
fun point(numbers: Pair<Int, Int>) = Point(numbers.first, numbers.second)
fun point(numbers: Map.Entry<Int, Int>) = Point(numbers.key, numbers.value)
fun point(point: Point) = Point(point)
fun point() = Point(0, 0)

fun runLater(block: () -> Unit) = SwingUtilities.invokeLater(block)
fun runLater(runnable: Runnable) = SwingUtilities.invokeLater(runnable)
fun runWait(block: () -> Unit) = SwingUtilities.invokeAndWait(block)
fun runWait(runnable: Runnable) = SwingUtilities.invokeAndWait(runnable)