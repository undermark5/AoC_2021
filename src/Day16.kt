fun main() {

    fun extractLiteralValue(bits: String): Pair<Long, String> {
        val sb = StringBuilder()
        var extracted = bits.extract(1)
        while (extracted.first == "1") {
            val literalBits = extracted.second.extract(4)
            sb.append(literalBits.first)
            extracted = literalBits.second.extract(1)
        }
        val literalBits = extracted.second.extract(4)
        sb.append(literalBits.first)
        return Pair(sb.toString().toLong(2), literalBits.second)
    }

    fun extractPacketVersionSum(bits: String): Pair<Int, String> {
        if (bits.isEmpty()) return Pair(0, "")
        if (bits.all { it == '0' }) return Pair(0, "")
        var extractedPair = bits.extract(3)
        val version = extractedPair.first.toInt(2)
        extractedPair = extractedPair.second.extract(3)
        val type = extractedPair.first.toInt(2)
        if (type == 4) {
            val (value, remainingBits) = extractLiteralValue(extractedPair.second)
            return Pair(version, remainingBits)
        }
        extractedPair = extractedPair.second.extract(1)
        val lengthTypeId = extractedPair.first.toInt(2)
        if (lengthTypeId == 0) {
            extractedPair = extractedPair.second.extract(15)
            val totalLength = extractedPair.first.toInt(2)
            extractedPair = extractedPair.second.extract(totalLength)
            var versionSum = 0
            val subPackets = extractedPair.first
            var remaining = subPackets
            while (remaining.isNotEmpty()) {
                val (version, newRemaining) = extractPacketVersionSum(remaining)
                versionSum += version
                remaining = newRemaining
            }
            return Pair(version + versionSum, extractedPair.second)
        } else {
            extractedPair = extractedPair.second.extract(11)
            val numSubPackets = extractedPair.first.toInt(2)
            var versionSum = 0
            for (i in 1..numSubPackets) {
                val (version, remaining) = extractPacketVersionSum(extractedPair.second)
                versionSum += version
                extractedPair = Pair("", remaining)
            }
            return Pair(version + versionSum, extractedPair.second)
        }
    }

    fun applyOperator(type: Int, values: List<Long>): Long {
        return when (type) {
            0 -> values.sum()
            1 -> values.reduce(Long::times)
            2 -> values.minOrNull() ?: error("illegal")
            3 -> values.maxOrNull() ?: error("illegal")
            5 -> {
                if (values.size != 2) error("illegal")
                if (values.first() > values.last()) 1
                else 0
            }
            6 -> {
                if (values.size != 2) error("illegal")
                if (values.first() < values.last()) 1
                else 0
            }
            7 -> {
                if (values.size != 2) error("illegal")
                if (values.first() == values.last()) 1
                else 0
            }
            else -> error("illegal")
        }
    }

    fun processPackets(bits: String): Pair<Long, String> {
        if (bits.isEmpty()) return Pair(0, "")
        if (bits.all { it == '0' }) return Pair(0, "")
        var extractedPair = bits.extract(3)
        val version = extractedPair.first.toInt(2)
        extractedPair = extractedPair.second.extract(3)
        val type = extractedPair.first.toInt(2)
        if (type == 4) {
            val (value, remainingBits) = extractLiteralValue(extractedPair.second)
            return Pair(value, remainingBits)
        }
        extractedPair = extractedPair.second.extract(1)
        val lengthTypeId = extractedPair.first.toInt(2)
        if (lengthTypeId == 0) {
            extractedPair = extractedPair.second.extract(15)
            val totalLength = extractedPair.first.toInt(2)
            extractedPair = extractedPair.second.extract(totalLength)
            val subValues = mutableListOf<Long>()
            val subPackets = extractedPair.first
            var remaining = subPackets
            while (remaining.isNotEmpty()) {
                val (value, newRemaining) = processPackets(remaining)
                subValues.add(value)
                remaining = newRemaining
            }
            return Pair(applyOperator(type, subValues), extractedPair.second)
        } else {
            extractedPair = extractedPair.second.extract(11)
            val numSubPackets = extractedPair.first.toInt(2)
            val subValues = mutableListOf<Long>()
            for (i in 1..numSubPackets) {
                val (value, remaining) = processPackets(extractedPair.second)
                subValues.add(value)
                extractedPair = Pair("", remaining)
            }
            return Pair(applyOperator(type, subValues), extractedPair.second)
        }
    }

    fun part1(input: List<String>): Int {
        val bits = input.first().map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("") { it }
        val (versionSum, remaining) = extractPacketVersionSum(bits)

        return versionSum
    }

    fun part2(input: List<String>): Long {
        val bits = input.first().map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("") { it }
        val (value, remaining) = processPackets(bits)

        return value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    val testInput1 = readInput("Day16_test1")
    val testInput2 = readInput("Day16_test2")
    val testInput3 = readInput("Day16_test3")
    val testInput4 = readInput("Day16_test4")
    check(part1(testInput) == 16)
    part1(testInput1)
    check(part1(testInput2) == 12)
    check(part1(testInput3) == 23)
    check(part1(testInput4) == 31)
    val testInput5 = readInput("Day16_test5")
    val testInput6 = readInput("Day16_test6")
    val testInput7 = readInput("Day16_test7")
    val testInput8 = readInput("Day16_test8")
    val testInput9 = readInput("Day16_test9")
    val testInput10 = readInput("Day16_test10")
    val testInput11 = readInput("Day16_test11")
    val testInput12 = readInput("Day16_test12")
    check(part2(testInput5) == 3L)
    check(part2(testInput6) == 54L)
    check(part2(testInput7) == 7L)
    check(part2(testInput8) == 9L)
    check(part2(testInput9) == 1L)
    check(part2(testInput10) == 0L)
    check(part2(testInput11) == 0L)
    check(part2(testInput12) == 1L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun String.extract(i: Int): Pair<String, String> {
    return Pair(this.substring(0 until i), this.substring(i until this.length))
}
