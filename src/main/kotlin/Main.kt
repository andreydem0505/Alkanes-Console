fun main() {
    println("Введите название изомера алкана:")
    val input = readLine()!!
    println(AlkanesFactory.convertNameToScheme(input))
}