data class Neighborhood(val focus: Int, val neighbors: List<Int>) {
    fun isLocalMin() : Boolean {
        return neighbors.all { it > focus }
    }
}

data class Basin(val primary: Neighborhood, val neighborhoods: List<Neighborhood>)

data class HeightMapNode(val height: Int) {
    val neighbors: MutableList<HeightMapNode> = mutableListOf()
    var visited = false
}

fun main() {
    fun part1(input: List<String>): Int {
        val heightMap = input.map { it.map { it.digitToInt() } }
        return heightMap.flatMapIndexed { i, row ->
            row.mapIndexed { j, value ->
                val neighbors = mutableListOf<Int>()
                if (i != 0) {
                    neighbors.add(heightMap[i - 1][j])
                }
                if (i != heightMap.lastIndex) {
                    neighbors.add(heightMap[i + 1][j])
                }
                if (j != 0) {
                    neighbors.add(row[j - 1])
                }
                if (j != row.lastIndex) {
                    neighbors.add(row[j + 1])
                }
                Neighborhood(value, neighbors)
            }
        }.filter { it.isLocalMin() }.sumOf { it.focus + 1 }
    }

    val queue: MutableList<HeightMapNode> = mutableListOf()

    fun findBasinBuddies(node: HeightMapNode): List<HeightMapNode> {
        val result: MutableList<HeightMapNode> = mutableListOf()
        result.add(node)
        node.visited = true
        queue.addAll(node.neighbors)
        while (queue.isNotEmpty()) {
            val curNode = queue.pop()
            if (!curNode.visited) {
                curNode.visited = true
                if (curNode.height != 9) {
                    result.add(curNode)
                    queue.addAll(curNode.neighbors)
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val heightMap = input.map { it.map { it.digitToInt() } }
        val neighborhoods = heightMap.mapIndexed { i, row ->
            row.mapIndexed { j, value ->
                val neighbors = mutableListOf<Int>()
                if (i != 0) {
                    neighbors.add(heightMap[i - 1][j])
                }
                if (i != heightMap.lastIndex) {
                    neighbors.add(heightMap[i + 1][j])
                }
                if (j != 0) {
                    neighbors.add(row[j - 1])
                }
                if (j != row.lastIndex) {
                    neighbors.add(row[j + 1])
                }
                Neighborhood(value, neighbors)
            }
        }
        val localMins = neighborhoods.flatMapIndexed { i, row ->
            row.mapIndexed { j, neighborhood ->
                Triple(i, j, neighborhood)
            }
        }.filter { it.third.isLocalMin() }.map { Pair(it.first, it.second) }
        val heightMapNodes = heightMap.map { it.map { HeightMapNode(it) } }
        heightMapNodes.forEachIndexed { i , row ->
                row.forEachIndexed { j, heightMapNode ->
                    if (i != 0) {
                        heightMapNode.neighbors.add(heightMapNodes[i - 1][j])
                    }
                    if (i != heightMap.lastIndex) {
                        heightMapNode.neighbors.add(heightMapNodes[i + 1][j])
                    }
                    if (j != 0) {
                        heightMapNode.neighbors.add(row[j - 1])
                    }
                    if (j != row.lastIndex) {
                        heightMapNode.neighbors.add(row[j + 1])
                    }
                }
            }
        return localMins.asSequence().mapNotNull {
            val node = heightMapNodes[it.first][it.second]
            if (!node.visited) {
                val basinBuddies = findBasinBuddies(node)
                basinBuddies.size
            } else null
        }.sortedDescending().take(3).map { it.toLong() }.reduce { acc, l -> acc * l }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134L)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}


