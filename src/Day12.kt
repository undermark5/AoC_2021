
data class Node(val name: String) {
    fun clone(): Node {
        val node = Node(name)
        node.visited = visited
        return node
    }

    val isBig = name.all { it.isUpperCase() }
    var visited = false
}

fun main() {
    fun countPathsHelper(start: Node, currentNode: Node, nodeMap: Map<Node, Set<Node>>) : Int {
        if (currentNode == start) {
            return 1
        }
        if (!currentNode.isBig && currentNode.visited) {
            return 0
        }
        currentNode.visited = true

        val currentNeighbors = nodeMap[currentNode] ?: error("illegal")
        if (currentNeighbors.all { it.visited && !it.isBig }) {
            return 0
        }
        return currentNeighbors.filter { it.isBig || !it.visited }.map { node ->
            val newMap = nodeMap.deepCopy()
            val newNode = newMap.keys.firstOrNull { it.name == node.name }?: error("illegal")
            countPathsHelper(start, newNode, newMap)
        }.sum()
    }

    fun countPaths(start: Node, end: Node, nodeMap: Map<Node, Set<Node>>): Int {
        return countPathsHelper(start, end, nodeMap)
    }

    fun part1(input: List<String>): Int {
        val nodeMap = mutableMapOf<Node, MutableSet<Node>>()
        input.forEach {
            val names = it.split("-")
            val first = Node(names.first())
            val second = Node(names.last())
            val firstNeighbors = nodeMap.getOrPut(first) { mutableSetOf() }
            val secondNeighbors = nodeMap.getOrPut(second) { mutableSetOf() }
            firstNeighbors.add(second)
            secondNeighbors.add(first)
        }
        val start = nodeMap.keys.firstOrNull { it.name == "start" } ?: error("illegal")
        val end = nodeMap.keys.firstOrNull { it.name == "end" } ?: error("illegal")
        return countPaths(start, end, nodeMap).also { println(it) }
    }

    fun countPathsHelper2(start: Node, currentNode: Node, nodeMap: Map<Node, Set<Node>>, secondSmallVisitTaken: Boolean) : Int {
        if (currentNode == start) {
            return 1
        }
        if (!currentNode.isBig && currentNode.visited && secondSmallVisitTaken) {
            return 0
        }
        currentNode.visited = true

        val currentNeighbors = nodeMap[currentNode] ?: error("illegal")
        if (currentNeighbors.all { it.visited && !it.isBig } && secondSmallVisitTaken) {
            return 0
        }
        if (secondSmallVisitTaken) {
            return currentNeighbors.filter { it.isBig || !it.visited }.map { node ->
                val newMap = nodeMap.deepCopy()
                val newNode = newMap.keys.firstOrNull { it.name == node.name } ?: error("illegal")
                countPathsHelper2(start, newNode, newMap, true)
            }.sum()
        } else {
            val smallNodes = currentNeighbors.filter { !it.isBig && it.name != "end" }
            val largeNodes = currentNeighbors.filter { it.isBig }
            val largeNodePaths = largeNodes.sumOf { node ->
                val newMap = nodeMap.deepCopy()
                val newNode = newMap.keys.firstOrNull { it.name == node.name } ?: error("illegal")
                countPathsHelper2(start, newNode, newMap, false)
            }
            val smallNodePaths = smallNodes.filter { !it.visited }.sumOf { node ->
                val newMap = nodeMap.deepCopy()
                val newNode = newMap.keys.firstOrNull { it.name == node.name } ?: error("illegal")
                countPathsHelper2(start, newNode, newMap, false)
            } + smallNodes.filter { it.visited }.sumOf { node ->
                val newMap = nodeMap.deepCopy()
                val newNode = newMap.keys.firstOrNull { it.name == node.name } ?: error("illegal")
                newNode.visited = false
                countPathsHelper2(start, newNode, newMap, true)
            }
            return largeNodePaths + smallNodePaths
        }
    }

    fun countPaths2(start: Node, end: Node, nodeMap: Map<Node, Set<Node>>): Int {
        return countPathsHelper2(start, end, nodeMap, false)
    }

    fun part2(input: List<String>): Int {
        val nodeMap = mutableMapOf<Node, MutableSet<Node>>()
        input.forEach {
            val names = it.split("-")
            val first = Node(names.first())
            val second = Node(names.last())
            val firstNeighbors = nodeMap.getOrPut(first) { mutableSetOf() }
            val secondNeighbors = nodeMap.getOrPut(second) { mutableSetOf() }
            firstNeighbors.add(second)
            secondNeighbors.add(first)
        }
        val start = nodeMap.keys.firstOrNull { it.name == "start" } ?: error("illegal")
        val end = nodeMap.keys.firstOrNull { it.name == "end" } ?: error("illegal")
        return countPaths2(start, end, nodeMap).also { println(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)
    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun Map<Node, Set<Node>>.deepCopy(): Map<Node, Set<Node>> {
    val newMap = mutableMapOf<Node, MutableSet<Node>>()
    this.keys.forEach { newMap[it.clone()] = mutableSetOf() }
    this.entries.forEach { entry ->
        entry.value.forEach {
            val node = newMap.keys.firstOrNull { it.name == entry.key.name } ?: error("illegal")
            newMap[it]?.add(node)
        }
    }
    return newMap
}
