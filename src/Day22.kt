import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, intRange: IntRange = -50..50): Long {
        val inputList = input.map { it.split(" ") }.map {
            val ranges = it.last().split(",").map { it.split("=").last().toIntRange() }
            Pair(
                it.first() == "on",
                Triple(ranges[0], ranges[1], ranges[2])
                    .intersect(Triple(intRange, intRange, intRange))
            )
        }

        val cuboidList = mutableListOf<Pair<Boolean, Triple<IntRange, IntRange, IntRange>>>()
        inputList.forEachIndexed { index, inputCuboid ->
            val newCuboids = mutableListOf<Pair<Boolean, Triple<IntRange, IntRange, IntRange>>>()
            cuboidList.map {
                val triple = inputCuboid.second.intersect(it.second)
                val (first, second, third) = triple
                if (first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty()) {
                    newCuboids.add(Pair(!it.first, triple))
                }
            }
            if (inputCuboid.first) {
                newCuboids.add(inputCuboid)
            }
            cuboidList.addAll(newCuboids)
        }

        return cuboidList.sumOf { it.second.computeArea() * if (it.first) 1 else -1 }
    }

    fun part2(input: List<String>): Long {
        return part1(input, -200000..200000)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 39L)
    val testInput2 = readInput("Day22_test2")
    check(part1(testInput2) == 590784L)
    val testInput3 = readInput("Day22_test3")
    check(part1(testInput3) == 474140L)
    check(part2(testInput3) == 2758514936282235)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

private fun Triple<IntRange, IntRange, IntRange>.computeArea(): Long {
    val firstLength = this.first.last - this.first.first + 1
    val secondLength = this.second.last - this.second.first + 1
    val thirdLength = this.third.last - this.third.first + 1
    return firstLength.toLong() * secondLength * thirdLength
}

private fun IntRange.isNotEmpty(): Boolean {
    return !this.isEmpty()
}

private fun Triple<IntRange, IntRange, IntRange>.intersect(other: Triple<IntRange, IntRange, IntRange>): Triple<IntRange, IntRange, IntRange> {
    val first = max(this.first.first, other.first.first)..min(other.first.last, this.first.last)
    val second =
        max(this.second.first, other.second.first)..min(other.second.last, this.second.last)
    val third = max(this.third.first, other.third.first)..min(other.third.last, this.third.last)
    if (first.isEmpty() || second.isEmpty() || third.isEmpty()) {
        return Triple(0 until 0, 0 until 0, 0 until 0)
    }
    return Triple(
        if (first.isNotEmpty()) first else 0 until 0,
        if (second.isNotEmpty()) second else 0 until 0,
        if (third.isNotEmpty()) third else 0 until 0,
    )
}


operator fun IntRange.contains(intRange: IntRange): Boolean {
    return intRange.all { it in this }
}

