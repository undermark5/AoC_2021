import kotlin.math.pow

val sequence = arrayOf(3,5,7,0,2,4,6,8,1)
val specialIndex = arrayOf(2,7)

fun main() {

//    fun computeTotalProduced(input: Int, numDays: Int): Long {
//        var sequenceIndex = sequence.indexOfFirst { it == input }
//        var doubleOffset = 0
//        var number = 1L
//        for (i in 0 until numDays / 7) {
//            number = number * 2 - doubleOffset
//            if (doubleOffset != 0) {
//                doubleOffset++
//            }
//            if (sequenceIndex in specialIndex) {
//                doubleOffset++
//            }
//            sequenceIndex++
//        }
//
//
//        return number
//    }
//
//    fun part1(input: List<String>): Long {
//        val numDays = 80
//        var doubleOffset = 0
//        return 0
//    }
//
//    fun part2(input: List<String>): Int {
//        val ages = input.flatMap { it.split(",").map { it.toInt() } }.toMutableList()
//        for(j in 0 until 11) {
//            for (i in 0 until 7) {
//                for (j in ages.indices) {
//                    ages[j]--
//                }
//                ages.filter { it == -1 }.forEachIndexed { index, value ->
//                    ages.add(8)
//                }
//                for (j in ages.indices) {
//                    if (ages[j] == -1) {
//                        ages[j] = 6
//                    }
//                }
//            }
//            print("")
//        }
//        for (i in 0 until 3) {
//            for (j in ages.indices) {
//                ages[j]--
//            }
//            ages.filter { it == -1 }.forEachIndexed { index, value ->
//                ages.add(8)
//            }
//            for (j in ages.indices) {
//                if (ages[j] == -1) {
//                    ages[j] = 6
//                }
//            }
//        }
//        print("")
//        return   ages.size
//    }

    fun part1(input: List<String>): Int {
        val ages = input.flatMap { it.split(",").map { it.toInt() } }.toMutableList()
        for (i in 0 until 80) {
            for (j in ages.indices) {
                ages[j]--
            }
            ages.filter { it == -1 }.forEach { _ ->
                ages.add(8)
            }
            for (j in ages.indices) {
                if (ages[j] == -1) {
                    ages[j] = 6
                }
            }
        }
        return ages.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
//    println(computeTotalProduced(3, 70))
//    check(part2(testInput) == 65)
    check(part1(testInput) == 5934)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
