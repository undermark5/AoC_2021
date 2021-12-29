import kotlin.math.max
import kotlin.math.min

private fun getFrequency(sumOfRoll: Int): Int {
    return when (sumOfRoll) {
        3 -> 1
        4 -> 3
        5 -> 6
        6 -> 7
        7 -> 6
        8 -> 3
        9 -> 1
        else -> 0
    }
}

class DeterministicD100 {
    private var cur = 0
    var rollCount = 0

    fun nextRolls(num: Int): Int {
        var sum = 0
        for (i in 1..num) {
            sum += cur + 1
            cur = (cur + 1) % 100
        }
        rollCount += num
        return sum
    }
}

private data class PlayerSummary(val score: Int, val numTurns: Int, val position: Int) {
    fun move(numSpaces: Int): PlayerSummary {
        val newPosition = (position + numSpaces) % 10
        val newScore = newPosition + 1 + score
        return PlayerSummary(newScore, numTurns + 1, newPosition)
    }
}

private val diceRolls = listOf(3, 4, 5, 6, 7, 8, 9)

private data class Player(var position: Int) {

    var score = 0

    fun move(numSpaces: Int) {
        position = (position + numSpaces) % 10
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val player1Start = input[0].takeLastWhile { it.isDigit() }.toInt()
        val player2Start = input[1].takeLastWhile { it.isDigit() }.toInt()
        var player1Pos = player1Start - 1
        var player2Pos = player2Start - 1
        var player1Score = 0
        var player2Score = 0
        val d100 = DeterministicD100()
        while (player2Score < 1000) {
            var roll = d100.nextRolls(3)
            player1Pos = (player1Pos + roll) % 10
            player1Score += player1Pos + 1
            if (player1Score >= 1000) break
            roll = d100.nextRolls(3)
            player2Pos = (player2Pos + roll) % 10
            player2Score += player2Pos + 1
        }
        return min(player1Score, player2Score) * d100.rollCount
    }

    fun part2(input: List<String>): Long {
        val player1Start = input[0].takeLastWhile { it.isDigit() }.toInt()
        val player2Start = input[1].takeLastWhile { it.isDigit() }.toInt()
        val player1Pos = player1Start - 1
        val player2Pos = player2Start - 1
        val scoreLimit = 21
        var player1WinCount = 0L
        var player2WinCount = 0L
        var universeMap = mutableMapOf<Pair<PlayerSummary, PlayerSummary>, Long>(
            Pair(
                PlayerSummary(0, 0, player1Pos),
                PlayerSummary(0, 0, player2Pos)
            ) to 1)
        var totalNumberOfUniverses = 0L
        var unfinishedGames =
            universeMap.filter { (it, _) -> it.first.score < scoreLimit && it.second.score < scoreLimit }
        var curPlayer = 0
        while (unfinishedGames.isNotEmpty()) {
            for (diceRoll in diceRolls) {
                val numNewUniverses = getFrequency(diceRoll)
                unfinishedGames.forEach { (key, value) ->
                    val player1 = key.first
                    val player2 = key.second
                    val newPair = if (curPlayer == 0) {
                        Pair(player1.move(diceRoll), player2)
                    } else {
                        Pair(player1, player2.move(diceRoll))
                    }
                    val currentValue = universeMap.getOrDefault(newPair, 0)
                    universeMap[newPair] = numNewUniverses * value + currentValue
                }
            }
            unfinishedGames.forEach {
                universeMap.remove(it.key)
            }
            curPlayer = (curPlayer + 1) % 2
            unfinishedGames =
                universeMap.filter { (it, _) -> it.first.score < scoreLimit && it.second.score < scoreLimit }
        }
        val finishedGames =
            universeMap.filter { (it, _) -> it.first.score >= scoreLimit || it.second.score >= scoreLimit }
        val player1WinningGames = finishedGames.filter { (it, _) -> it.first.score >= scoreLimit }
        val player2WinningGames = finishedGames.filter { (it, _) -> it.second.score >= scoreLimit }
        assert(finishedGames.size == player1WinningGames.size + player2WinningGames.size)
        player1WinCount = player1WinningGames.values.sum()
        player2WinCount = player2WinningGames.values.sum()
        return max(player1WinCount, player2WinCount)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
