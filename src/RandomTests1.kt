import java.io.BufferedInputStream
import java.util.*

object RandomTests1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val stdin = Scanner(BufferedInputStream(System.`in`))
        var counter = 0
        while (stdin.hasNext()) {
            val x = stdin.next()
            if (x == "done") break

            println(x.split("\n"))
            counter++
        }
        println(counter)
    }
}