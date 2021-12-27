fun main() {


    fun marchEasternMoving(
        seaFloor: List<List<String>>,
        easternMoving: List<Triple<String, Int, Int>>,
    ): List<List<String>> {
        val newSeaFloor = seaFloor.map { it.toMutableList() }.toMutableList()
        val canMove = easternMoving.filter { seaFloor[it.second][(it.third + 1) % seaFloor[it.second].size] == "." }
        canMove.forEach {
            newSeaFloor[it.second][(it.third + 1) % seaFloor[it.second].size] = it.first
            newSeaFloor[it.second][it.third] = "."
        }
        return newSeaFloor
    }

    fun marchSouthernMoving(
        seaFloor: List<List<String>>,
        southernMoving: List<Triple<String, Int, Int>>,
    ): List<List<String>> {
        val newSeaFloor = seaFloor.map { it.toMutableList() }.toMutableList()
        val canMove = southernMoving.filter { seaFloor[(it.second + 1) % seaFloor.size][it.third] == "." }
        canMove.forEach {
            newSeaFloor[(it.second + 1) % seaFloor.size][it.third] = it.first
            newSeaFloor[it.second][it.third] = "."
        }
        return newSeaFloor
    }

    fun march(
        seaFloor: List<List<String>>,
        easternMoving: List<Triple<String, Int, Int>>,
        southernMoving: List<Triple<String, Int, Int>>,
    ): List<List<String>> {
        var newSeaFloor = marchEasternMoving(seaFloor, easternMoving)
        newSeaFloor = marchSouthernMoving(newSeaFloor, southernMoving)

        return newSeaFloor
    }

    fun part1(input: List<String>): Int {
        var seaFloor = input.map { it.map { it.toString() } }
        var seaPickles = seaFloor
            .mapIndexed { i, row -> row.mapIndexed { j, s -> Triple(s, i, j) } }
        var easternMoving = seaPickles.flatten().filter { it.first == ">" }
        var southernMoving = seaPickles.flatten().filter { it.first == "v" }
        var count = 0
        lateinit var oldRepresentation: String
        var newRepresentation = seaFloor.flatten().joinToString { it }
        do {
            oldRepresentation = newRepresentation
            seaFloor = march(seaFloor, easternMoving, southernMoving)
            seaPickles = seaFloor
                .mapIndexed { i, row -> row.mapIndexed { j, s -> Triple(s, i, j) } }
            easternMoving = seaPickles.flatten().filter { it.first == ">" }
            southernMoving = seaPickles.flatten().filter { it.first == "v" }
            newRepresentation = seaFloor.flatten().joinToString { it }
            count++
            val gridView = seaFloor.joinToString("\n") { it.joinToString("") { it } }
            println("After $count step${if (count != 1) "s" else ""}:")
            println(gridView)
        } while (newRepresentation != oldRepresentation)

        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
