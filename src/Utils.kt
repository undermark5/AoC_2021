import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("input", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <E> MutableList<E>.push(c: E) {
    this.add(0, c)
}

fun <E> MutableList<E>.pop(): E {
    return this.removeAt(0)
}

fun <E> MutableList<E>.peek(): E {
    return this.first()
}

data class MutablePair<T, O>(var first: T, var second: O)

fun String.toIntRange():IntRange {
    val split = this.split("..")
    return split.first().toInt()..split.last().toInt()
}

operator fun <R> (() -> R).times(paddingSize: Int): List<R> {
    val value = this()
    return MutableList(paddingSize) { value }
}