fun main() {
    fun part1(input: List<String>): Int {
        val forward = input.filter { it.startsWith("forward") }.sumOf { it.split(" ").last().toInt() }
        val up = input.filter { it.startsWith("up") }.sumOf { it.split(" ").last().toInt() }
        val down = input.filter { it.startsWith("down") }.sumOf { it.split(" ").last().toInt() }
        return (forward * (down - up))
    }

    fun part2(input: List<String>): Int {
        var curHorizontal = 0
        var curVertical = 0
        var currentAim = 0
        input.forEach {
            val split = it.split(" ")
            when (split.first()) {
                "forward" -> {
                    val num = split.last().toInt()
                    curHorizontal += num
                    curVertical += num * currentAim
                }
                "down" -> {currentAim += split.last().toInt()}
                "up" -> {currentAim -= split.last().toInt()}
            }
        }
        return curHorizontal * curVertical
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
