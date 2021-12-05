import kotlin.math.max
import kotlin.math.min

fun main() {
    data class GridPoint(var count: Int = 0)
    fun makeGrid(size: Int): List<MutableList<GridPoint>> {
        return List(size + 1) {MutableList(size + 1) { GridPoint() } }
    }

    data class Point(val x: Int, val y:Int)

    fun makePoint(input:String): Point {
        val vals = input.split(",")
        return Point(vals[0].toInt(), vals[1].toInt())
    }

    fun plotLines(grid:List<MutableList<GridPoint>>, points:List<Pair<Point,Point>>, flag: Boolean = false) {
        points.forEach {
            if (it.first.x == it.second.x) {
                val col = it.first.x
                val row1 = it.first.y
                val row2 = it.second.y
                val rowStart = min(row1,row2)
                val rowEnd = max(row1, row2)

                grid.subList(rowStart, rowEnd + 1).forEach { it[col].count++ }

            } else if (it.first.y == it.second.y) {
                val row = it.first.y
                val col1 = it.first.x
                val col2 = it.second.x
                val colStart = min(col1, col2)
                val colEnd = max(col1, col2)

                grid[row].subList(colStart, colEnd + 1).forEach { it.count++ }

            } else if (flag) {
                val col1 = it.first.x
                val col2 = it.second.x
                val row1 = it.first.y
                val row2 = it.second.y

                val colStart = min(col1, col2)
                val colEnd = max(col1, col2)

                val cols = (if (col1 > col2) (colStart..colEnd).reversed() else (colStart..colEnd)).toList()
                val rowStart = min(row1,row2)
                val rowEnd = max(row1, row2)
                val rows = (if (row1 > row2) (rowStart..rowEnd).reversed() else (rowStart..rowEnd)).toList()

                rows.forEachIndexed { index, row ->
                    grid[row][cols[index]].count++
                }


                print("")
            }
        }
    }

    fun part1(input: List<String>): Int {
        val max = input.flatMap { it.filter { it.isDigit() || it == ',' || it == ' ' }.replace("  ", ",").split(",").map { it.toInt() } }.maxOrNull() ?: error("illegal")
        val grid = makeGrid(max)
        val points = input.map { it.split("->").map { it.trim() } }
            .map { Pair(makePoint(it[0]), makePoint(it[1])) }
        plotLines(grid, points)

        return grid.flatMap { it.map { it.count } }.filter { it >= 2 }.size
    }

    fun part2(input: List<String>): Int {
        val max = input.flatMap { it.filter { it.isDigit() || it == ',' || it == ' ' }.replace("  ", ",").split(",").map { it.toInt() } }.maxOrNull() ?: error("illegal")
        val grid = makeGrid(max)
        val points = input.map { it.split("->").map { it.trim() } }
            .map { Pair(makePoint(it[0]), makePoint(it[1])) }
        plotLines(grid, points, true)
        return grid.flatMap { it.map { it.count } }.filter { it >= 2 }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
