fun main() {

    fun part1(input: List<String>): Int {
        val size = input.first().length
        val gamma = (0 until size).map { index ->
            input.map { it[index].toString() }.groupingBy { it }.eachCount()
                .maxByOrNull { it.value } ?: error("illegal")
        }.map { it.key }.joinToString("") { it }.toUInt(2).toInt()

        val epsilon = (0 until size).map { index ->
            input.map { it[index].toString() }.groupingBy { it }.eachCount()
                .minByOrNull { it.value } ?: error("illegal")
        }.map { it.key }.joinToString("") { it }.toUInt(2).toInt()

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        var inputCopy = input.toMutableList()
        val size = inputCopy.first().length

        var index = 0
        while (inputCopy.size != 1) {
            val eachCount = inputCopy.map { it[index].toString() }.groupingBy { it }.eachCount()
            val criteria = if (eachCount.count() == 2) {
                if (eachCount["0"] == eachCount["1"])
                    "1"
                else eachCount.maxByOrNull { it.value }?.key ?: error("illegal")
            } else eachCount.keys.first()
            inputCopy.removeAll { it[index].toString() != criteria }
            index++
        }
        val oxygen = inputCopy.first().toInt(2)
        inputCopy = input.toMutableList()
        index = 0
        while (inputCopy.size != 1) {
            val eachCount = inputCopy.map { it[index].toString() }.groupingBy { it }.eachCount()
            val criteria =  if (eachCount.count() == 2) {
                if (eachCount["0"] == eachCount["1"])
                    "0"
                else eachCount.minByOrNull { it.value }?.key ?: error("illegal")
            } else eachCount.keys.first()
                inputCopy.removeAll { it[index].toString() != criteria }
            index++
        }
        val co2 = inputCopy.first().toInt(2)
        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
