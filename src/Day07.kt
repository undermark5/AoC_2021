import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val positions = input.flatMap { it.split(",").map { it.toInt() } }
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: Int.MAX_VALUE

        var minFuel = Long.MAX_VALUE
        for (i in min..max) {
            val totalCost = positions.sumOf { abs(it - i).toLong() }
            if (totalCost < minFuel) {
                minFuel = totalCost
            }
            else {
                break
            }
        }


        return minFuel + 0
    }

    fun part2(input: List<String>): Long {
        val positions = input.flatMap { it.split(",").map { it.toInt() } }
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: Int.MAX_VALUE

        var minFuel = Long.MAX_VALUE
        for (i in min..max) {
            val totalCost = positions.sumOf {
                val distance = abs(it - i).toLong()
                (distance * (distance + 1L)) / 2L
            }
            if (totalCost < minFuel) {
                minFuel = totalCost
            }
            else {
                break
            }
        }


        return minFuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
