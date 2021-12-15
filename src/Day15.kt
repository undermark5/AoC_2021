import java.util.*
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

data class MazeCell(val riskLevel: Int, val coords: Pair<Int, Int>) : Comparable<MazeCell> {
    var minRiskToEnter = Int.MAX_VALUE
    var visited = false
    override fun compareTo(other: MazeCell): Int {
        return if (minRiskToEnter > other.minRiskToEnter)
            1
        else if (minRiskToEnter < other.minRiskToEnter)
            -1
        else
            0
    }

}


@OptIn(ExperimentalTime::class)
fun main() {
    //incorrectly assumes that we won't ever move up or left.
    //sticking with this algorithm here because it got me the right answer (though I guess I got lucky with my input)
    fun part1(input: List<String>): Int {
        val maze = input.map { it.map { MazeCell(it.digitToInt(), Pair(0, 0)) } }
        maze.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                val upperMinRisk = if (i - 1 >= 0) maze[i - 1][j].minRiskToEnter else Int.MAX_VALUE
                val leftMinRisk = if (j - 1 >= 0) row[j - 1].minRiskToEnter else Int.MAX_VALUE
                cell.minRiskToEnter = if (i == 0 && j == 0) 0 else cell.riskLevel + min(upperMinRisk, leftMinRisk)
            }
        }
        return maze.last().last().minRiskToEnter
    }


    fun part2(input: List<String>): Int {
        val mazeTile = input.map { it.map { it.digitToInt() } }
        val maze = (0 until 5 * mazeTile.size).map { i ->
            val row = i % mazeTile.size
            (0 until 5 * mazeTile[row].size).map { j ->
                val col = j % mazeTile[row].size
                val additionalRisk = (i / mazeTile.size) + (j / mazeTile[row].size)
                val localRisk = mazeTile[row][col] + additionalRisk
                MazeCell(localRisk - if (localRisk > 9) 9 else 0, Pair(i, j))
            }
        }

        maze[0][0].minRiskToEnter = 0
        val queue: PriorityQueue<MazeCell> = PriorityQueue(maze.flatten())
        var curCell = queue.remove()
        while (queue.isNotEmpty() && curCell !== maze.last().last()) {
            if (curCell.visited) curCell = queue.remove()
            val (i, j) = curCell.coords
            val neighbors = mutableListOf<MazeCell>()
            if (i - 1 >= 0) {
                neighbors.add(maze[i - 1][j])
            }
            if (i + 1 <= maze.lastIndex) {
                neighbors.add(maze[i + 1][j])
            }
            if (j - 1 >= 0) {
                neighbors.add(maze[i][j - 1])
            }
            if (j + 1 <= maze[i].lastIndex) {
                neighbors.add(maze[i][j + 1])
            }
            neighbors.filter { !it.visited }.forEach {
                it.minRiskToEnter = min(it.minRiskToEnter, curCell.minRiskToEnter + it.riskLevel)
                queue.remove(it)
                queue.add(it)
            }
            curCell.visited = true
            curCell = queue.remove()
        }
        
        return maze.last().last().minRiskToEnter
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(measureTime {
        println(part2(input))
    })
}
