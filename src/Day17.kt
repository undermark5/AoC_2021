import kotlin.math.abs

val regex = Regex("""\d+..\d+""")
val triangleNumbers = Array(1000) { ((it.toLong() * (it.toLong() + 1L)) / 2L) }

val velocities = (
        "23,-10  25,-9   27,-5   29,-6   22,-6   21,-7   9,0     27,-7   24,-5\n" +
                "25,-7   26,-6   25,-5   6,8     11,-2   20,-5   29,-10  6,3     28,-7\n" +
                "8,0     30,-6   29,-8   20,-10  6,7     6,4     6,1     14,-4   21,-6\n" +
                "26,-10  7,-1    7,7     8,-1    21,-9   6,2     20,-7   30,-10  14,-3\n" +
                "20,-8   13,-2   7,3     28,-8   29,-9   15,-3   22,-5   26,-8   25,-8\n" +
                "25,-6   15,-4   9,-2    15,-2   12,-2   28,-9   12,-3   24,-6   23,-7\n" +
                "25,-10  7,8     11,-3   26,-7   7,1     23,-9   6,0     22,-10  27,-6\n" +
                "8,1     22,-8   13,-4   7,6     28,-6   11,-4   12,-4   26,-9   7,4\n" +
                "24,-10  23,-8   30,-8   7,0     9,-1    10,-1   26,-5   22,-9   6,5\n" +
                "7,5     23,-6   28,-10  10,-2   11,-1   20,-9   14,-2   29,-7   13,-3\n" +
                "23,-5   24,-8   27,-9   30,-7   28,-5   21,-10  7,9     6,6     21,-5\n" +
                "27,-10  7,2     30,-9   21,-8   22,-7   24,-9   20,-6   6,9     29,-5\n" +
                "8,-2    27,-8   30,-5   24,-7").split("\n").flatMap { it.split("  ").map { it.trim() } }
    .filter { it.isNotBlank() }.map {
        val pair = it.split(",")
        Pair(pair.first().toInt(), pair.last().toInt())
    }.sortedBy { it.second }.sortedBy { it.first }

fun main() {
    fun maxHeightVelocity(input: List<String>): Int {
        val targetBounds = input.first().split(":").last().split(",").map { it.trim().split("=").last() }.map {
            val bounds = it.split("..")
            bounds.first().toInt()..bounds.last().toInt()
        }
        val maxXVelocity = targetBounds.first().last
        val minXVelocity = triangleNumbers.indexOfFirst { it > targetBounds.first().first }
        val minXPos = triangleNumbers[minXVelocity]
        assert(minXPos <= maxXVelocity)
        var yVelocityGuess = triangleNumbers.lastIndex
        var maxY = triangleNumbers[yVelocityGuess]
        var finalYPositions = triangleNumbers.map { maxY - it }
        while (finalYPositions.none { it in targetBounds.last() }) {
            yVelocityGuess--
            maxY = triangleNumbers[yVelocityGuess]
            finalYPositions = triangleNumbers.map { maxY - it }
        }
        return yVelocityGuess
    }

    fun part1(input: List<String>): Long {
        return triangleNumbers[maxHeightVelocity(input)]
    }

    fun part2(input: List<String>): Long {
        val targetBounds = input.first().split(":").last().split(",").map { it.trim().split("=").last() }.map {
            val bounds = it.split("..")
            bounds.first().toInt()..bounds.last().toInt()
        }
        val maxXVelocity = targetBounds.first().last
        val minXVelocity = triangleNumbers.indexOfFirst { it > targetBounds.first().first }
        val maxYVelocity = maxHeightVelocity(input)
        val minYVelocity = targetBounds.last().first
        val pairs = mutableListOf<Pair<Int, Int>>()
        for (i in minXVelocity..maxXVelocity) {
            for (j in minYVelocity..maxYVelocity) {
                val xTrajectory = triangleNumbers.map { triangleNumbers[i] - it }.takeWhile { it != 0L }.toMutableList().also { it.add(0) ;it.reverse() }
                val yTrajectory = if (j >= 0) {
                    val maxHeight = triangleNumbers[j]
                    val finalHeights = triangleNumbers.map { maxHeight - it }
                    finalHeights.takeWhile { it != 0L }.toMutableList().also {
                        it.add(0)
                        it.reverse()
                        it.addAll(finalHeights)
                    }
                } else {
                    val offset = abs(j) - 1
                    triangleNumbers.map { triangleNumbers[offset] - it }
                        .slice(offset + 1..triangleNumbers.lastIndex).toMutableList().also { it.add(0, 0) }
                }
                val paddingSize = yTrajectory.size - xTrajectory.size
                val padding = { xTrajectory.last() } * paddingSize
                xTrajectory.addAll(padding)
                val trajectory = xTrajectory.zip(yTrajectory)
                if (trajectory.any { it.first in targetBounds.first() && it.second in targetBounds.last() }) {
                    pairs.add(Pair(i,j))
                }
            }
        }
        return pairs.size.toLong()
    }

    println(velocities.joinToString("\n") { "${it.first.toString().padStart(2)},${it.second.toString().padStart(3)}" })

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45L)
    check(part2(testInput).also { println(it) } == 112L)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
