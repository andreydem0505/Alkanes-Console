import java.lang.Exception
import java.util.*

object AlkanesFactory {
    private lateinit var stringBuilder: StringBuilder
    private lateinit var list: LinkedList<Array<Int?>>
    private var mainIndex: Int = 0
    private var size: Int = 0

    fun convertNameToScheme(data: String): String {
        try {
            var main: String? = null
            for (alkane in Alkanes.map.keys) {
                if (data.contains(alkane)) {
                    main = alkane
                    break
                }
            }
            size = Alkanes.map[main]!!
            list = LinkedList(mutableListOf(arrayOfNulls(size)))
            list[0][0] = 3
            list[0][size - 1] = 3
            for (i in 1 until size - 1) {
                list[0][i] = 2
            }
            val prefixes = data.removeSuffix(main!!).split("-")
            for (i in prefixes.indices step 2) {
                val indexes = prefixes[i].split(",").map { it.toInt() }
                val alkanes = prefixes[i+1]
                val alkane = alkanes.removePrefix(Numbers.map[indexes.size - 1])
                for (index in indexes) {
                    for (j in 1..Alkanes.map[alkane.replace("ил", "ан")]!!) {
                        if (mainIndex == j-1) {
                            list.push(arrayOfNulls(size))
                            list[0][index-1] = 3
                            list[1][index-1] = list[1][index-1]!! - 1
                            mainIndex++
                        } else {
                            if (list[mainIndex-j][index-1] == null) {
                                list[mainIndex-j][index-1] = 3
                                list[mainIndex-j+1][index-1] = list[mainIndex-j+1][index-1]!! - 1
                            } else {
                                if (list.size <= mainIndex + j) {
                                    list.add(arrayOfNulls(size))
                                }
                                list[mainIndex+j][index-1] = 3
                                list[mainIndex+j-1][index-1] = list[mainIndex+j-1][index-1]!! - 1
                            }
                        }
                    }
                }
            }
            stringBuilder = StringBuilder()
            for (i in list.indices) {
                if (i > mainIndex) {
                    addVerticalDashes(i)
                    addHydrogenBlocks(i)
                } else {
                    addHydrogenBlocks(i)
                    addVerticalDashes(i)
                }
            }
            return stringBuilder.toString()
        } catch (e: Exception) {
            return "Невозможно обработать этот запрос"
        }
    }

    private fun getSpacesByHydrogenQuantity(number: Int) = when (number) {
        0 -> 2
        1 -> 3
        else -> 4
    }

    private fun getHydrogen(number: Int): String {
        return "C" + if (number > 0) {
            if (number == 1) {
                "H"
            } else {
                "H$number"
            }
        } else ""
    }

    private fun addHydrogenBlocks(i: Int) {
        var debt = 0
        for (j in list[i].indices) {
            if (list[i][j] == null) {
                stringBuilder.append(" ".repeat(getSpacesByHydrogenQuantity(list[mainIndex][j]!!) - debt))
            } else {
                stringBuilder.append(getHydrogen(list[i][j]!!))
                if (i == mainIndex && j < size - 1) {
                    stringBuilder.append("-")
                } else {
                    val difference = getHydrogen(list[mainIndex][j]!!).length - getHydrogen(list[i][j]!!).length + 1
                    if (difference > 0) {
                        stringBuilder.append(" ".repeat(difference))
                    } else {
                        debt = -difference
                    }
                }
            }
        }
        stringBuilder.append("\n")
    }

    private fun addVerticalDashes(i: Int) {
        if (i != mainIndex) {
            for (j in list[i].indices) {
                if (list[i][j] == null) {
                    stringBuilder.append(" ".repeat(getSpacesByHydrogenQuantity(list[mainIndex][j]!!)))
                } else {
                    stringBuilder.append("|")
                    stringBuilder.append(" ".repeat(getHydrogen(list[mainIndex][j]!!).length))
                }
            }
            stringBuilder.append("\n")
        }
    }
}