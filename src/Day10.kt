import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long {
        val lines = input.mapNotNull { string ->
            val stack = mutableListOf<Char>()
            string.forEach {
                when (it) {
                    '(', '[', '{', '<' -> {
                        stack.push(it)
                    }
                    ')', ']', '}', '>', -> {
                        when (val c = stack.pop()) {
                            '(' -> {
                                if (it != ')') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '[' -> {
                                if (it != ']') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '{' -> {
                                if (it != '}') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '<' -> {
                                if (it != '>') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                        }
                    }
                }
            }
            if (stack.isNotEmpty()) {
                return@mapNotNull Triple(Char(0), false, string)
            } else {
                return@mapNotNull null
            }
        }
        val corruptLines = lines.filter { it.second }
        return corruptLines.map {
            when (it.first) {
                ')' -> 3L
                ']' -> 57L
                '}' -> 1197L
                '>' -> 25137L
                else -> 0L
            }
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val lines: List<Triple<Any, Boolean, String>> = input.mapNotNull { string ->
            val stack = mutableListOf<Char>()
            string.forEach {
                when (it) {
                    '(', '[', '{', '<' -> {
                        stack.push(it)
                    }
                    ')', ']', '}', '>', -> {
                        when (stack.pop()) {
                            '(' -> {
                                if (it != ')') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '[' -> {
                                if (it != ']') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '{' -> {
                                if (it != '}') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                            '<' -> {
                                if (it != '>') {
                                    return@mapNotNull Triple(it, true, string)
                                }
                            }
                        }
                    }
                }
            }
            if (stack.isNotEmpty()) {
                return@mapNotNull Triple(stack, false, string)
            } else {
                return@mapNotNull null
            }
        }
        val incompleteLines = lines.filter { !it.second && it.first is MutableList<*> }
        val scores = incompleteLines.map { it.first as MutableList<Char> }.map { stack ->
            stack.reversed().mapIndexed { index, c ->
                when (c) {
                    '(' -> 1L * 5.0.pow(index).toLong()
                    '[' -> 2L * 5.0.pow(index).toLong()
                    '{' -> 3L * 5.0.pow(index).toLong()
                    '<' -> 4L * 5.0.pow(index).toLong()
                    else -> 0L
                }
            }.sum()
        }.sorted()
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397L)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun <E> MutableList<E>.push(c: E) {
    this.add(0, c)
}

private fun <E> MutableList<E>.pop(): E {
    return this.removeAt(0)
}
