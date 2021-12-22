import kotlin.math.ceil

fun main() {

    fun convert(string: String): Pair<*, *> {
        val stack = mutableListOf<Any>()
        var i = 0
        while (i < string.length) {
            if (string[i] == '[') {
                stack.push('[')
            } else if (string[i] == ']') {
                val second = stack.pop()
                val first = stack.pop()
                val symbol = stack.pop()
                stack.push(Pair(first, second))
            } else if (string[i].isDigit()) {
                stack.push(string[i].digitToInt())
            }
            i++
        }
        val result = stack.pop()
        if (result is Pair<*, *>) {
            return result
        } else error("illegal")
    }

    fun explosionReduction(unreduced: String): Pair<Boolean, String> {
        var i = 0
        val stack = mutableListOf<Char>()
        while (i < unreduced.length) {
            if (unreduced[i] == '[') {
                stack.push('[')
            } else if (unreduced[i] == ']') {
                stack.pop()
            }
            if (stack.size > 4) {
                break
            }
            i++
        }
        if (i >= unreduced.length) return Pair(false, unreduced)
        val sb = StringBuilder()
        var firstPart = unreduced.substring(0 until i++)
        while (unreduced[i] != ',') {
            sb.append(unreduced[i++])
        }
        val left = sb.toString().toInt()
        i++
        sb.clear()
        while (unreduced[i] != ']') {
            sb.append(unreduced[i++])
        }
        val right = sb.toString().toInt()
        var secondPart = unreduced.substring(i + 1)
        i = firstPart.lastIndex
        while (i >= 0) {
            if (firstPart[i].isDigit()) {
                break
            }
            i--
        }
        sb.clear()
        if (i >= 0 && firstPart[i].isDigit()) {
            val startIndex = i + 1
            while (firstPart[i].isDigit()) {
                sb.append(firstPart[i--])
            }
            val leftNumber = sb.reverse().toString().toInt()
            firstPart = firstPart.substring(0..i) + (left + leftNumber) + firstPart.substring(startIndex)
        }
        i = 0
        while (i < secondPart.length) {
            if (secondPart[i].isDigit()) {
                break
            }
            i++
        }
        sb.clear()
        if (i <= secondPart.lastIndex && secondPart[i].isDigit()) {
            val startIndex = i
            while (secondPart[i].isDigit()) {
                sb.append(secondPart[i++])
            }
            val rightNumber = sb.toString().toInt()
            secondPart = secondPart.substring(0 until startIndex) + (right + rightNumber) + secondPart.substring(i)
        }
        return Pair(true, firstPart + 0 + secondPart)
    }

    fun splitsReduction(unreduced: String): String {
        val leftMostLargest =
            unreduced.filter { it == ',' || it.isDigit() }.split(",").firstOrNull { it.length >= 2 } ?: return unreduced
        val location = unreduced.indexOf(leftMostLargest)
        if (location < 0) error("illegal")
        val number = leftMostLargest.toInt()
        return unreduced.replaceFirst(leftMostLargest, "[${number / 2},${ceil(number / 2.0).toInt()}]")
    }

    fun reduceSnailFishNumber(basicSum: String): String {
        var unreduced = basicSum
        var semiReduced = unreduced
        do {
            unreduced = semiReduced
            val result = explosionReduction(unreduced)
            semiReduced = result.second
            if (!result.first) {
                semiReduced = splitsReduction(unreduced)
            }

        } while (semiReduced != unreduced)
        return semiReduced
    }

    fun addSnailFishNumbers(first: String, second: String): String {
        val basicSum = "[$first,$second]"
        val reduced = reduceSnailFishNumber(basicSum)
        return reduced
    }


    fun computeMagnitude(pair: Pair<*, *>): Long {
        val first = pair.first
        val second = pair.second
        val firstMagnitude =
            if (first is Int) first.toLong() else if (first is Pair<*, *>) computeMagnitude(first) else error("illegal")
        val secondMagnitude =
            if (second is Int) second.toLong() else if (second is Pair<*, *>) computeMagnitude(second) else error("illegal")
        return 3 * firstMagnitude + 2 * secondMagnitude
    }

    fun computeMagnitude(sum: String): Long {
        val pair = convert(sum)
        return computeMagnitude(pair)
    }

    fun part1(input: List<String>): Long {
        val sum = input.reduce { acc, s -> addSnailFishNumbers(acc, s) }
        return computeMagnitude(sum)
    }

    fun part2(input: List<String>): Long {
        return input.mapIndexed { index, s ->
            val myCopy = input.toMutableList()
            myCopy.removeAt(index)
            myCopy.maxOfOrNull { part1(listOf(s, it)) } ?: error("illegal")
        }.maxOrNull() ?: error("illegal")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140L)
    check(part2(testInput) == 3993L)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}


