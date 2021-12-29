fun main() {
    fun part1(input: List<String>, numTimes: Int = 2): Int {
        val enhancementAlgorithm = input.first()
        val imageBase = input.takeLastWhile { it.isNotBlank() }
        var image =
            imageBase.map { it.padStart(it.length * 2, '.').padEnd(it.length * 3, '.') }
                .toMutableList()
                .apply {
                    addAll(0,
                        { '.' * this[0].length } * imageBase.size)
                    addAll({ '.' * this[0].length } * imageBase.size)
                }.map { it.map { it.toString() } }
//        println(image.joinToString("\n") { it.joinToString("") { it } })
        var newImage = image.map { it.map { it }.toMutableList() }.toMutableList()
        var count = 0
        while (count++ < numTimes) {
            for (i in 1 until newImage.lastIndex) {
                for (j in 1 until newImage[i].lastIndex) {
                    val topSlice = image[i - 1].slice(j - 1..j + 1)
                    val middleSlice = image[i].slice(j - 1..j + 1)
                    val bottomSlice = image[i + 1].slice(j - 1..j + 1)
                    val num =
                        (topSlice + middleSlice + bottomSlice).map { if (it == ".") "0" else "1" }
                            .joinToString("") { it }.toInt(2)
                    newImage[i][j] = enhancementAlgorithm[num].toString()
                }
            }

            newImage[0] = newImage[0].map { newImage[1][1] }.toMutableList()
            newImage[newImage.lastIndex] =
                newImage[newImage.lastIndex].map { newImage[newImage.lastIndex - 1][newImage.lastIndex - 1] }
                    .toMutableList()

            newImage.forEach {
                it[0] = newImage[1][1]
                it[it.lastIndex] = newImage[newImage.lastIndex - 1][newImage.lastIndex - 1]
            }

            image = newImage
            newImage = image.map { it.map { it }.toMutableList() }.toMutableList()
            println(image.joinToString("\n", postfix = "\n") { it.joinToString("") { it } })
        }

        return image.flatten().count { it == "#" }
    }

    fun part2(input: List<String>): Int {
        return part1(input, 50)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}

private operator fun Char.times(length: Int): String {
    val array = CharArray(length) { this }
    return String(array)
}
