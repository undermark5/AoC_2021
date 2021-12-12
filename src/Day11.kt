data class Octopus(var energy: Int) {
    val neighbors: MutableList<Octopus> = mutableListOf()
    var flashing: Boolean = false
}

fun main() {

    fun increaseEnergies(octopuses: List<Octopus>) {
        octopuses.forEach {
            it.energy++
        }
    }

    fun flashOctopuses(octopuses: List<Octopus>): Long {
        var count = 0L
        val originalFlashingOctopuses = octopuses.filter { it.energy > 9 }
        originalFlashingOctopuses.forEach {
            it.flashing = true
            count++
        }
        val neighbors = originalFlashingOctopuses.flatMap { it.neighbors }.toTypedArray()
        val queue = mutableListOf(*neighbors)
        while (queue.isNotEmpty()) {
            val octopus = queue.pop()
            if (!octopus.flashing) {
                octopus.energy++
                if (octopus.energy > 9) {
                    octopus.flashing = true
                    count++
                    queue.addAll(octopus.neighbors.filter { !it.flashing })
                }
            }
        }
        return count
    }

    fun resetOctopuses(octopuses: List<Octopus>) {
        octopuses.filter { it.flashing }.forEach {
            it.energy = 0
            it.flashing = false
        }
    }

    fun stepOctopuses(octopuses: List<Octopus>): Long {

        increaseEnergies(octopuses)
        val count = flashOctopuses(octopuses)
        resetOctopuses(octopuses)
        return count
    }

    fun part1(input: List<String>): Long {
        val octopusGrid = input.map { it.map { Octopus(it.digitToInt()) } }
        octopusGrid.forEachIndexed { i, row ->
            row.forEachIndexed {j, octopus ->
                if (i != 0) {
                    octopus.neighbors.add(octopusGrid[i - 1][j])
                }
                if (i != octopusGrid.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i + 1][j])
                }
                if (j != 0) {
                    octopus.neighbors.add(row[j - 1])
                }
                if (j != row.lastIndex) {
                    octopus.neighbors.add(row[j + 1])
                }
                if (i != 0 && j != 0) {
                    octopus.neighbors.add(octopusGrid[i - 1][j - 1])
                }
                if (i != octopusGrid.lastIndex && j != 0) {
                    octopus.neighbors.add(octopusGrid[i + 1][j - 1])
                }
                if (i != 0 && j != row.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i - 1][j + 1])
                }
                if (i != octopusGrid.lastIndex && j != row.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i + 1][j + 1])
                }
            }
        }
        val octopuses = octopusGrid.flatten()
        var count = 0L
        println(octopusGrid.joinToString("\n") { it.joinToString("") { "${it.energy}" } })
        println()
        for (i in 1..100) {
            count += stepOctopuses(octopuses)
            println(octopusGrid.joinToString("\n") { it.joinToString("") { "${it.energy}" } })
            println()
        }
        return count
    }

    fun part2(input: List<String>): Long {
        val octopusGrid = input.map { it.map { Octopus(it.digitToInt()) } }
        octopusGrid.forEachIndexed { i, row ->
            row.forEachIndexed {j, octopus ->
                if (i != 0) {
                    octopus.neighbors.add(octopusGrid[i - 1][j])
                }
                if (i != octopusGrid.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i + 1][j])
                }
                if (j != 0) {
                    octopus.neighbors.add(row[j - 1])
                }
                if (j != row.lastIndex) {
                    octopus.neighbors.add(row[j + 1])
                }
                if (i != 0 && j != 0) {
                    octopus.neighbors.add(octopusGrid[i - 1][j - 1])
                }
                if (i != octopusGrid.lastIndex && j != 0) {
                    octopus.neighbors.add(octopusGrid[i + 1][j - 1])
                }
                if (i != 0 && j != row.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i - 1][j + 1])
                }
                if (i != octopusGrid.lastIndex && j != row.lastIndex) {
                    octopus.neighbors.add(octopusGrid[i + 1][j + 1])
                }
            }
        }
        val octopuses = octopusGrid.flatten()
        var step = 0L
        var flashCount = 0
        while (flashCount != 100) {
            step++
            flashCount = stepOctopuses(octopuses).toInt()

        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656L)
    check(part2(testInput) == 195L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
