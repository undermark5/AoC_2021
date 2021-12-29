import kotlin.math.abs
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun isEveryoneHome(curNodes: List<AmphipodNode>): Boolean {
    val result = curNodes.filter { it.isHall }.all { it.amphipod.isBlank() } &&
            curNodes.filter { !it.isHall }.all {
                it.amphipod == it.homeFor
            }
    if (!!result) {
        return true
    } else
        return false
}

class AmphipodNode(
    var amphipod: String,
    val isBlockingEntrance: Boolean,
    val isHall: Boolean,
    val homeFor: String,
    val i: Int,
    val j: Int,
) {
    var up: AmphipodNode? = null
    var left: AmphipodNode? = null
    var right: AmphipodNode? = null
    var down: AmphipodNode? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AmphipodNode

        if (amphipod != other.amphipod) return false
        if (isBlockingEntrance != other.isBlockingEntrance) return false
        if (isHall != other.isHall) return false
        if (homeFor != other.homeFor) return false
        if (i != other.i) return false
        if (j != other.j) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amphipod.hashCode()
        result = 31 * result + isBlockingEntrance.hashCode()
        result = 31 * result + isHall.hashCode()
        result = 31 * result + homeFor.hashCode()
        result = 31 * result + i
        result = 31 * result + j
        return result
    }

    fun clone(): AmphipodNode {
        return AmphipodNode(amphipod, isBlockingEntrance, isHall, homeFor, i, j)
    }

}

class AmphipodMap(val nodes: List<AmphipodNode>, val cost: Int) {
    constructor(pair: Pair<List<AmphipodNode>, Int>) : this(pair.first, pair.second)

    override fun toString(): String {
        val sb = StringBuilder("\n")
        for (i in 0 until 7) {
            for (j in 0 until 13) {
                val node = nodes.firstOrNull { it.i == i && it.j == j }
                if (node == null) {
                    sb.append("#")
                } else {
                    sb.append(if (node.amphipod.isBlank()) "." else node.amphipod)
                }
            }
            if (i == 0) sb.append("  $cost")
            sb.append("\n")
        }
        return sb.toString()
    }
}

@ExperimentalTime
fun main() {


    val aHomeIndex = 3
    val bHomeIndex = 5
    val cHomeIndex = 7
    val dHomeIndex = 9

    val homeIndexes = listOf(aHomeIndex, bHomeIndex, cHomeIndex, dHomeIndex)

    fun generateMap(input: List<String>): List<AmphipodNode> {
        val nodes = mutableListOf<AmphipodNode>()
        input.forEachIndexed { i, row ->
            row.padEnd(13).forEachIndexed { j, c ->
                if (c != '#' && c != ' ') {
                    val node = AmphipodNode(if (c != '.') c.toString() else "",
                        i == 1 && j in homeIndexes,
                        i == 1,
                        if (i != 1) when (j) {
                            aHomeIndex -> "A"
                            bHomeIndex -> "B"
                            cHomeIndex -> "C"
                            dHomeIndex -> "D"
                            else -> ""
                        } else "", i, j)
                    nodes.add(node)
                }
            }
        }
        nodes.forEach { cur ->
            cur.up = nodes.firstOrNull { cur.i - 1 == it.i && cur.j == it.j }
            cur.down = nodes.firstOrNull { cur.i + 1 == it.i && cur.j == it.j }
            cur.left = nodes.firstOrNull { cur.i == it.i && cur.j - 1 == it.j }
            cur.right = nodes.firstOrNull { cur.i == it.i && cur.j + 1 == it.j }
        }
        return nodes
    }

    fun computeAbsoluteMinimumCost(nodes: List<AmphipodNode>): Int {
        val aNodes = nodes.filter { it.amphipod == "A" && it.amphipod != it.homeFor }
        val homeANodes = nodes.filter { it.amphipod == "A" && it.amphipod == it.homeFor }
        val bNodes = nodes.filter { it.amphipod == "B" && it.amphipod != it.homeFor }
        val homeBNodes = nodes.filter { it.amphipod == "B" && it.amphipod == it.homeFor }
        val cNodes = nodes.filter { it.amphipod == "C" && it.amphipod != it.homeFor }
        val homeCNodes = nodes.filter { it.amphipod == "C" && it.amphipod == it.homeFor }
        val dNodes = nodes.filter { it.amphipod == "D" && it.amphipod != it.homeFor }
        val homeDNodes = nodes.filter { it.amphipod == "D" && it.amphipod == it.homeFor }
        val aNumMoves =
            aNodes.sumOf {
                abs(aHomeIndex - it.j) + it.i - 1 /*compute number of moves to get to hall*/
            } + 3 /*constant number of moves to get both home from hall entrance*/ - if (homeANodes.isNotEmpty()) (if (homeANodes.size == 1) (if (homeANodes.first().i == 2) 1 else 2) else 3) else 0
        val bNumMoves =
            bNodes.sumOf {
                abs(bHomeIndex - it.j) + it.i - 1 /*compute number of moves to get to hall*/
            } + 3 /*constant number of moves to get both home from hall entrance*/ - if (homeBNodes.isNotEmpty()) (if (homeBNodes.size == 1) (if (homeBNodes.first().i == 2) 1 else 2) else 3) else 0
        val cNumMoves =
            cNodes.sumOf {
                abs(cHomeIndex - it.j) + it.i - 1 /*compute number of moves to get to hall*/
            } + 3 /*constant number of moves to get both home from hall entrance*/ - if (homeCNodes.isNotEmpty()) (if (homeCNodes.size == 1) (if (homeCNodes.first().i == 2) 1 else 2) else 3) else 0
        val dNumMoves =
            dNodes.sumOf {
                abs(dHomeIndex - it.j) + it.i - 1 /*compute number of moves to get to hall*/
            } + 3 /*constant number of moves to get both home from hall entrance*/ - if (homeDNodes.isNotEmpty()) (if (homeDNodes.size == 1) (if (homeDNodes.first().i == 2) 1 else 2) else 3) else 0
        return aNumMoves + (10 * bNumMoves) + (100 * cNumMoves) + (1000 * dNumMoves)
    }

    fun cloneNodes(nodes: List<AmphipodNode>): List<AmphipodNode> {
        val clones = nodes.map { it.clone() }
        clones.forEach { cur ->
            cur.up = clones.firstOrNull { cur.i - 1 == it.i && cur.j == it.j }
            cur.down = clones.firstOrNull { cur.i + 1 == it.i && cur.j == it.j }
            cur.left = clones.firstOrNull { cur.i == it.i && cur.j - 1 == it.j }
            cur.right = clones.firstOrNull { cur.i == it.i && cur.j + 1 == it.j }
        }
        return clones
    }

    fun generateBranches(
        curNodes: List<AmphipodNode>,
        currentCost: Int,
        previous: List<Pair<List<AmphipodNode>, Int>>,
    ): Set<Triple<List<AmphipodNode>, Int, List<Pair<List<AmphipodNode>, Int>>>> {
        val results =
            mutableSetOf<Triple<List<AmphipodNode>, Int, List<Pair<List<AmphipodNode>, Int>>>>()
        val amphipodNodes = curNodes.filter { it.amphipod.isNotBlank() }
        amphipodNodes.forEach { startNode ->
            val costIncrement = when (startNode.amphipod) {
                "A" -> 1
                "B" -> 10
                "C" -> 100
                "D" -> 1000
                else -> error("illegal")
            }
            var curNode: AmphipodNode? = startNode
            val homeNodes =
                curNodes.filter { it.homeFor == startNode.amphipod }.sortedBy { it.i }
            val homeIndex = homeNodes.firstOrNull()?.j ?: error("illegal")
            val canAttemptToGoHome =
                homeNodes.all { it.amphipod == startNode.amphipod || it.amphipod.isBlank() }
            val isFirst = homeNodes.all { it.amphipod.isBlank() }
            if (homeNodes.all { it.amphipod == it.homeFor }) return@forEach
            val lowestIndex = homeNodes.filter { it.amphipod.isBlank() }.maxOfOrNull { it.i } ?: 0
            if (startNode.i == 1) {
                if (canAttemptToGoHome) {

                    val finalNode = homeNodes.firstOrNull { it.i == lowestIndex } ?: error("illegal")
                    val homeDirIsRight = startNode.j < homeIndex
                    var canMakeItHome = true

                    while (curNode != null && curNode.down?.homeFor != startNode.amphipod) {
                        curNode = if (homeDirIsRight) curNode.right else curNode.left
                        if (curNode?.amphipod?.isNotBlank() == true) {
                            canMakeItHome = false
                            break
                        }
                    }
                    if (canMakeItHome) {
                        val clones = cloneNodes(curNodes)
                        val clonedStart =
                            clones.firstOrNull { startNode.i == it.i && startNode.j == it.j }
                                ?: error("illegal")
                        clonedStart.amphipod = ""
                        val clonedFinal =
                            clones.firstOrNull { finalNode.i == it.i && finalNode.j == it.j }
                                ?: error("illegal")
                        clonedFinal.amphipod = startNode.amphipod
                        results.add(Triple(clones,
                            currentCost + (abs(homeIndex - startNode.j) * costIncrement) + ((lowestIndex - 1) * costIncrement),
                            listOf(Pair(curNodes, currentCost), *previous.toTypedArray())))
                    }
                }
            } else if (startNode.up != null && startNode.up?.amphipod?.isBlank() == true && (startNode.up?.up == null || startNode.up?.up?.amphipod?.isBlank() == true)) {
                val hallStartNode = curNodes.firstOrNull { it.i == 1 && it.j == startNode.j }
                val costToHall = (startNode.i - 1) * costIncrement
                if (canAttemptToGoHome) {
                    val finalNode = homeNodes.firstOrNull { it.i == lowestIndex } ?: error("illegal")
                    val homeDirIsRight = startNode.j < homeIndex
                    var canMakeItHome = true
                    curNode = hallStartNode
                    while (curNode != null && curNode.down?.homeFor != startNode.amphipod) {
                        curNode = if (homeDirIsRight) curNode.right else curNode.left
                        if (curNode?.amphipod?.isNotBlank() == true) {
                            canMakeItHome = false
                            break
                        }
                    }
                    if (canMakeItHome) {
                        val clones = cloneNodes(curNodes)
                        val clonedStart =
                            clones.firstOrNull { startNode.i == it.i && startNode.j == it.j }
                                ?: error("illegal")
                        clonedStart.amphipod = ""
                        val clonedFinal =
                            clones.firstOrNull { finalNode.i == it.i && finalNode.j == it.j }
                                ?: error("illegal")
                        clonedFinal.amphipod = startNode.amphipod
                        results.add(Triple(clones,
                            currentCost + (abs(homeIndex - startNode.j) * costIncrement) + costToHall + ((lowestIndex - 1) * costIncrement),
                            listOf(Pair(curNodes, currentCost), *previous.toTypedArray())))
                    }
                } else {
                    curNode = hallStartNode
                    var cost = costToHall
                    while (curNode?.right != null) {
                        curNode = curNode.right
                        if (curNode == null) break
                        if (curNode.amphipod.isNotBlank()) break
                        if (!curNode.isBlockingEntrance) {
                            val clones = cloneNodes(curNodes)
                            val clonedStart =
                                clones.firstOrNull { startNode.i == it.i && startNode.j == it.j }
                                    ?: error("illegal")
                            clonedStart.amphipod = ""
                            val clonedFinal =
                                clones.firstOrNull { curNode?.i == it.i && curNode?.j == it.j }
                                    ?: error("illegal")
                            clonedFinal.amphipod = startNode.amphipod
                            results.add(
                                Triple(
                                    clones,
                                    currentCost + (abs(clonedFinal.j - clonedStart.j) * costIncrement) + costToHall,
                                    listOf(Pair(curNodes, currentCost), *previous.toTypedArray())
                                )
                            )
                        }
                    }
                    curNode = hallStartNode
                    while (curNode?.left != null) {
                        curNode = curNode.left
                        if (curNode == null) break
                        if (curNode.amphipod.isNotBlank()) break
                        if (!curNode.isBlockingEntrance) {
                            val clones = cloneNodes(curNodes)
                            val clonedStart =
                                clones.firstOrNull { startNode.i == it.i && startNode.j == it.j }
                                    ?: error("illegal")
                            clonedStart.amphipod = ""
                            val clonedFinal =
                                clones.firstOrNull { curNode?.i == it.i && curNode?.j == it.j }
                                    ?: error("illegal")
                            clonedFinal.amphipod = startNode.amphipod
                            results.add(
                                Triple(
                                    clones,
                                    currentCost + (abs(clonedFinal.j - clonedStart.j) * costIncrement) + costToHall,
                                    listOf(Pair(curNodes, currentCost), *previous.toTypedArray())
                                )
                            )
                        }
                    }
                }
            }
        }
        return results
    }


    fun part1(input: List<String>, costUpperBound: Int): Int {
        val nodes = generateMap(input)
        val seenConfigurations = mutableMapOf<List<AmphipodNode>, Int>()
        val stack = mutableListOf(Triple(nodes, 0, listOf<Pair<List<AmphipodNode>, Int>>()))
        var minCost = Int.MAX_VALUE
        var maxCost = 0
        var maxPath = listOf<Pair<List<AmphipodNode>, Int>>()
        while (stack.isNotEmpty()) {
            val (curNodes, costToGetHere, previous) = stack.pop()
            if (isEveryoneHome(curNodes)) {
                minCost = min(minCost, costToGetHere)
            } else {
                seenConfigurations[curNodes] = costToGetHere
            }
            val absoluteMinCost = computeAbsoluteMinimumCost(curNodes) + costToGetHere
            if (absoluteMinCost > costUpperBound || absoluteMinCost > minCost) {
                continue
            }
            val branches: Set<Triple<List<AmphipodNode>, Int, List<Pair<List<AmphipodNode>, Int>>>> =
                generateBranches(curNodes, costToGetHere, previous)
            val unseen = branches.filter { it.first !in seenConfigurations }
            val seen = branches.filter { it.first in seenConfigurations }
            stack.addAll(0, unseen)
            unseen.associateByTo(seenConfigurations, { it.first }, { it.second })
            seen.forEach { (nodes, cost, previous) ->
                val previousCost = seenConfigurations[nodes] ?: Int.MAX_VALUE
                if (cost < previousCost) {
                    seenConfigurations[nodes] = cost
                    stack.add(0, Triple(nodes, cost, previous))
                }
            }
        }
        return minCost
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput, 12521) == 12521)

    val input1 = readInput("Day23")
    println(part1(input1, 14379))
    check(part1(input1, 14379) == 14371)
    val input2 = readInput("Day23_part2")
    println(measureTime {
        println(part1(input2, 60000))
    })
}
