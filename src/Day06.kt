import java.math.BigInteger
import kotlin.math.pow

val sequence = arrayOf(3,5,7,0,2,4,6,8,1)
val specialIndex = arrayOf(2,7)

fun main() {

    fun part1(input: List<String>): Int {
        val ages = input.flatMap { it.split(",").map { it.toInt() } }.toMutableList()
        for (i in 0 until 80) {
            for (j in ages.indices) {
                ages[j]--
            }
            ages.filter { it == -1 }.forEach { _ ->
                ages.add(8)
            }
            for (j in ages.indices) {
                if (ages[j] == -1) {
                    ages[j] = 6
                }
            }
        }
        return ages.size
    }

    fun part2(input: List<String>): Long {
        var original: Map<Int, Long> = input.flatMap { it.split(",").map { it.toInt() } }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        var newCopy = mutableMapOf<Int, Long>()
        for (i in 0 until 256) {
            original.forEach { (key, value) ->
                newCopy[key - 1] = newCopy.getOrPut(key - 1) { 0L } + value
                print("")
            }

            newCopy[8] = newCopy.getOrDefault(-1, 0)
            newCopy[6] = newCopy.getOrDefault(-1, 0) + newCopy.getOrDefault(6, 0)
            newCopy[-1] = 0

            original = newCopy.filter { it.key >= 0 }
            newCopy = mutableMapOf()
        }

        return original.values.map { it }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
