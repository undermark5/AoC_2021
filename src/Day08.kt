data class SevenSegmentDisplay(val segments: String) {
    fun getCommonElements(other: SevenSegmentDisplay): String {
        return segments.toSet().intersect(other.segments.toSet()).joinToString("") { "$it" }
    }

    fun getMissingElements(other: SevenSegmentDisplay): String {
        return segments.toSet().filter { it !in other.segments.toSet() }.joinToString("") { "$it" }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is SevenSegmentDisplay) return false
        return segments.toSet().containsAll(other.segments.toSet()) && other.segments.toSet().containsAll(segments.toSet())
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.flatMap { it.split("|").map { it.trim() }[1].split(" ").map { it.length } }
            .count { it == 2 || it == 4 || it == 3 || it == 7 }
    }

    fun part2(input: List<String>): Long {
        val inputs = input.map { it.split("|").map { it.trim().split(" ") } }
        val sum = inputs.map {
            val mappings = it.first()
            val oneMapping = SevenSegmentDisplay(mappings.first { it.length == 2 })
            val sevenMapping = SevenSegmentDisplay(mappings.first { it.length == 3 })
            val fourMapping = SevenSegmentDisplay(mappings.first { it.length == 4 })
            val eightMapping = SevenSegmentDisplay(mappings.first { it.length == 7 })
            val remainingMappings = mappings.filterNot { it.length == 2 || it.length == 4 || it.length == 3 || it.length == 7 }
            val threeMapping = SevenSegmentDisplay(remainingMappings.filter { it.length == 5 }.first {SevenSegmentDisplay(it).getCommonElements(oneMapping).length == 2})
            val antiOneMapping = SevenSegmentDisplay(eightMapping.getMissingElements(threeMapping))
            val centerBar = SevenSegmentDisplay(fourMapping.getMissingElements(oneMapping)).getMissingElements(antiOneMapping)
            val sixMapping = SevenSegmentDisplay(remainingMappings.filter { it.length == 6 }.first { SevenSegmentDisplay(it).getCommonElements(antiOneMapping).length == 2 && it.contains(centerBar)})
            val zeroMapping = SevenSegmentDisplay(remainingMappings.filter {it.length == 6}.first {SevenSegmentDisplay(it).getCommonElements(antiOneMapping).length == 2 && !it.contains(centerBar)})
            val fiveMapping = SevenSegmentDisplay(remainingMappings.filter { it.length == 5 }.first { SevenSegmentDisplay(it).getCommonElements(sixMapping).length == 5})
            val nineMapping = SevenSegmentDisplay(remainingMappings.filter {it.length == 6}.first { item: String -> SevenSegmentDisplay(item).getCommonElements(oneMapping).length == 2 && item.contains(centerBar) })
            val twoMapping = SevenSegmentDisplay(remainingMappings.filter {it.length == 5}.first { SevenSegmentDisplay(it).getCommonElements(fiveMapping).length == 3})
            val output = it.last().map { SevenSegmentDisplay(it) }.map {
                when (it) {
                    zeroMapping -> "0"
                    oneMapping -> "1"
                    twoMapping -> "2"
                    threeMapping -> "3"
                    fourMapping -> "4"
                    fiveMapping -> "5"
                    sixMapping -> "6"
                    sevenMapping -> "7"
                    eightMapping -> "8"
                    nineMapping -> "9"
                    else -> "NaN"
                }
            }.joinToString("") { it }
            if (output.contains("NaN")) error("illegal")
            output.toLong()
        }.sum()

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229L)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
