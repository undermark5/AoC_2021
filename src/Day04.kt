import kotlin.math.min

data class BingoCell(val value:String) {var marked: Boolean = false}
fun main() {
    fun getBoards(input: List<String>): List<List<List<BingoCell>>> {
        val boards = mutableListOf<List<List<BingoCell>>>()
        var board = mutableListOf<List<BingoCell>>()
        input.forEach {
            if (it.isBlank()) {
                if (board.isNotEmpty()) {
                    boards.add(board)
                }
                board = mutableListOf()
            }
            else {
                board.add(it.split(" ").filter{it.isNotBlank()}.map { BingoCell(it) })
            }
        }
        if (board.isNotEmpty()) {
            boards.add(board)
        }
        return boards
    }

    fun checkForPossibleBingo(
        board: List<List<BingoCell>>,
        rowIndex: Int,
        colIndex: Int
    ): Boolean {
        val up = rowIndex - 1
        val down = rowIndex + 1
        val left = colIndex - 1
        val right = colIndex + 1
        val upValid = try {
            board[up][colIndex].marked
        } catch (e: IndexOutOfBoundsException) {
            true
        }
        val downValid = try {
            board[down][colIndex].marked
        } catch (e: IndexOutOfBoundsException) {
            true
        }
        val rightValid = try {
            board[rowIndex][right].marked
        } catch (e: IndexOutOfBoundsException) {
            true
        }
        val leftValid = try {
            board[rowIndex][left].marked
        } catch (e: IndexOutOfBoundsException) {
            true
        }
        return leftValid && rightValid && upValid && downValid
    }

    fun checkForRealBingo(
        board: List<List<BingoCell>>,
        rowIndex: Int,
        colIndex: Int
    ): Boolean {
        return board[rowIndex].all { it.marked } || board.map { it[colIndex] }.all { it.marked }
    }

    fun markBoardAndCheckForBingo(board: List<List<BingoCell>>, number: String): Boolean {
        val rowIndex = board.indexOfFirst { it.any { it.value == number } }
        if (rowIndex == -1) return false
        val colIndex = board[rowIndex].indexOfFirst { it.value == number }
        if (colIndex == -1) return false
        board[rowIndex][colIndex].marked = true
        val possibleBingo = checkForPossibleBingo(board, rowIndex, colIndex)
        if (possibleBingo) {
            return checkForRealBingo(board, rowIndex, colIndex)
        }
        return false
    }

    fun getSoonestBingo(board: List<List<BingoCell>>, randomNumbers: List<String>): Int {
        val cols = board.indices.map { index -> board.map { it[index].value } }
        val rows = board.map { it.map { it.value } }.toSet()
        val soonestIndexForRow = rows.map { row ->
            val lastIndex = randomNumbers.indexOfLast {it in row}
            if (randomNumbers.subList(0, lastIndex + 1).containsAll(row)) lastIndex else randomNumbers.size
        }.minOrNull() ?: -1
        val soonestIndexForCol = cols.map { col ->
            val lastIndex = randomNumbers.indexOfLast { it in col }
            if (randomNumbers.subList(0, lastIndex + 1).containsAll(col)) lastIndex else randomNumbers.size
        }.minOrNull() ?: -1
        return min(soonestIndexForCol, soonestIndexForRow)
    }

    fun getLatestBingo(board:List<List<BingoCell>>, randomNumbers: List<String>): Int {
        val cols = board.indices.map { index -> board.map { it[index].value } }
        val rows = board.map { it.map { it.value } }.toSet()
        val soonestIndexForRow = rows.map { row ->
            val lastIndex = randomNumbers.indexOfLast {it in row}
            if (randomNumbers.subList(0, lastIndex + 1).containsAll(row)) lastIndex else -1
        }.maxOrNull() ?: randomNumbers.size
        val soonestIndexForCol = cols.map { col ->
            val lastIndex = randomNumbers.indexOfLast { it in col }
            if (randomNumbers.subList(0, lastIndex + 1).containsAll(col)) lastIndex else randomNumbers.size
        }.maxOrNull() ?: randomNumbers.size
        return min(soonestIndexForCol, soonestIndexForRow)
    }

    fun findWinningBoard(input: List<String>):Int {
        val randomNumbers = input.first().split(",")
        val boards = getBoards(input.subList(1, input.size))
        val winningEntry = boards.associateBy { getSoonestBingo(it, randomNumbers) }.minByOrNull { it.key } ?: error("illegal")
        val randSubList = randomNumbers.subList(0, winningEntry.key + 1)
        return winningEntry.value.flatMap { it.filter { it.value !in randSubList }.map { it.value.toInt() } }.sum() * randomNumbers[winningEntry.key].toInt()
    }

    fun part1(input: List<String>): Int {
        return findWinningBoard(input)
    }

    fun part2(input: List<String>): Int {
        val randomNumbers = input.first().split(",")
        val boards = getBoards(input.subList(1, input.size))
        val winningEntry = boards.associateBy { getSoonestBingo(it, randomNumbers) }.maxByOrNull { it.key } ?: error("illegal")
        val randSubList = randomNumbers.subList(0, winningEntry.key + 1)
        return winningEntry.value.flatMap { it.filter { it.value !in randSubList }.map { it.value.toInt() } }.sum() * randomNumbers[winningEntry.key].toInt()
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
