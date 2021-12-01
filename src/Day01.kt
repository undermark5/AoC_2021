fun main() {
    fun part1(input: List<Int>): Int {
        return input.mapIndexed { index, value ->
            if (index == 0) false else value > input[index - 1]
        }.count { it }
    }

    fun part2(input: List<Int>): Int {
        val windowedSums: List<Int> = input.windowed(3).map {it.sum()}
        return part1(windowedSums)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
