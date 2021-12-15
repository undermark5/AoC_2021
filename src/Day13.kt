val coordRegex = Regex("""\d+,\d+""")

fun main() {
    fun part1(input: List<String>): Int {
        val coords = input.filter { it.matches(coordRegex) }.map { it.split(",") }
            .map { Pair(it.first().toInt(), it.last().toInt()) }
        val instructions = input.filter { !it.matches(coordRegex) && it.isNotBlank() }.map { it.split("=") }.map { Pair(it.first().last(), it.last().toInt()) }
        val maxHeight = coords.maxOf { it.second } + 1
        val maxWidth = coords.maxOf { it.first } + 1
        val grid = Array(maxHeight) { y ->
            Array(maxWidth) { x ->
                if (Pair(x, y) in coords) {
                    "#"
                } else "."
            }
        }
        val foldInstruction = instructions.first()
        return if (foldInstruction.first == 'y') {
            val top = grid.take(foldInstruction.second)
            val bottom = grid.takeLast(maxHeight - foldInstruction.second + 1)
            check(grid[foldInstruction.second].all { it == "." })
            check(grid[foldInstruction.second] !== top.last() && grid[foldInstruction.second] !== bottom.first())
            val flippedBottom = bottom.reversed()
            Array(foldInstruction.second) { y ->
                Array(maxWidth) { x ->
                    if (top[y][x] == "#" || flippedBottom[y][x] == "#") "#" else "."
                }
            }.flatten().count { it == "#" }
        } else {
            val left = grid.map { it.take(foldInstruction.second) }
            val right = grid.map { it.takeLast(maxWidth - foldInstruction.second + 1) }
            val flippedRight = right.map { it.reversed() }
            Array(maxHeight) { y ->
                Array(foldInstruction.second) { x ->
                    if (left[y][x] == "#" || flippedRight[y][x] == "#") "#" else "."
                }
            }.flatten().count { it == "#" }
        }
    }

    fun part2(input: List<String>): String {
        val coords = input.filter { it.matches(coordRegex) }.map { it.split(",") }
            .map { Pair(it.first().toInt(), it.last().toInt()) }
        val instructions = input.filter { !it.matches(coordRegex) && it.isNotBlank() }.map { it.split("=") }.map { Pair(it.first().last(), it.last().toInt()) }
        val maxHeight = coords.maxOf { it.second } + 1
        val maxWidth = coords.maxOf { it.first } + 1
        val grid = Array(maxHeight) { y ->
            Array(maxWidth) { x ->
                if (Pair(x, y) in coords) {
                    "#"
                } else "."
            }
        }
       return instructions.map { foldInstruction ->
            { grid: Array<Array<String>> ->
                if (foldInstruction.first == 'y') {
                    val top = grid.take(foldInstruction.second)
                    val bottom = grid.takeLast(maxHeight - foldInstruction.second + 1)
                    check(grid[foldInstruction.second].all { it == "." })
                    check(grid[foldInstruction.second] !== top.last() && grid[foldInstruction.second] !== bottom.first())
                    val flippedBottom = bottom.reversed()
                    Array(foldInstruction.second) { y ->
                        Array(top[y].size) { x ->
                            if (top[y][x] == "#" || flippedBottom[y][x] == "#") "#" else "."
                        }
                    }
                } else {
                    val left = grid.map { it.take(foldInstruction.second) }
                    val right = grid.map { it.takeLast(maxWidth - foldInstruction.second + 1) }
                    val flippedRight = right.map { it.reversed() }
                    Array(left.size) { y ->
                        Array(left[y].size) { x ->
                            if (left[y][x] == "#" || flippedRight[y][x] == "#") "#" else "."
                        }
                    }
                }
            }
        }.reduce { acc: (Array<Array<String>>) -> Array<Array<String>>, function: (Array<Array<String>>) -> Array<Array<String>> ->
            { grid ->
                function(acc(grid))
            }
        }(grid).joinToString("\n") { it.joinToString("") { it } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == """
        #####
        #...#
        #...#
        #...#
        #####
        .....
        .....""".trimIndent())

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
