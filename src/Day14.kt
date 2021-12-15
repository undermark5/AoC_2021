fun main() {
    fun part1(input: List<String>): Int {
        val insertionRules = input.filter { it.contains("->") && it.isNotBlank() }
        val starting = input.first()
        val insertionFunction = insertionRules.map { it.split("->").map { it.trim() } }.map {
            { input: String ->
                if (input == it.first()) {
                    it.last()
                } else ""
            }
        }.reduce { acc: (String) -> String, function: (String) -> String ->
            { input ->
                acc(input) + function(input)
            }
        }
        var curStep = starting
        for (i in 1..10) {
            curStep = curStep.windowed(2).map {
                val result = insertionFunction(it)
                "${it.first()}${result}${it.last()}"
            }.joinToString("") {
                if (it.length == 2) it else it.take(2)
            } + curStep.last()
        }
        val histogram = curStep.map { it.toString() }.groupingBy { it }.eachCount()
        return (histogram.maxOfOrNull { it.value } ?: 0) - (histogram.minOfOrNull { it.value } ?: 0)
    }

    fun part2(input: List<String>): Long {
        val insertionRules = input.filter { it.contains("->") && it.isNotBlank() }
        val starting = input.first()
        val replacementMappings = insertionRules.map { it.split("->").map { it.trim() } }.associate {
            Pair(
                it.first(),
                Triple(it.first().first() + it.last(), it.last() + it.first().last(), it.last())
            )
        }
        var pairHistogram = starting.windowed(2).groupingBy { it }.fold(0L) { accumulator, element ->
            accumulator + 1L
        }.toMutableMap()
        val lettersHistogram =
            starting.groupBy { it.toString() }.mapValues { (_, value) -> value.size.toLong() }.toMutableMap()
        for (i in 1..40) {
            val newMap = mutableMapOf<String, Long>()
            pairHistogram.entries.toList().forEach { (key, value: Long) ->
                replacementMappings[key]?.let { triple ->
                    newMap[triple.first] = value + (newMap[triple.first] ?: 0L)
                    newMap[triple.second] = value + (newMap[triple.second] ?: 0L)
                    lettersHistogram[triple.third] = value + (lettersHistogram[triple.third] ?: 0L)
                }
            }
            pairHistogram = newMap
        }
        return (lettersHistogram.maxOfOrNull { (_, value) -> value }
            ?: 0) - (lettersHistogram.minOfOrNull { (_, value) -> value } ?: 0)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
