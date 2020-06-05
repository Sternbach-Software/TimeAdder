import java.lang.Integer.parseInt

fun indexOfAOrP(time1: String)=if (time1.contains("a",ignoreCase=true)) time1.indexOf("a", ignoreCase=true) else time1.indexOf("p", ignoreCase=true)
const val time1 = "4:31 PM"
var time2 = "4:33:03 PM"

fun main() {
    val minute1 = time1.substring(time1.indexOf(":") + 1, time1.indexOf(":") + 3).toInt()
    val minute2 = time2.substring(time2.indexOf(":") + 1, time1.indexOf(":") + 3).toInt()

    val second1: Int = if (time1.split(":").size > 2) parseInt(time1.split(":"," ")[2]) else 0
    val second2: Int = if (time2.split(":").size > 2) parseInt(time2.split(":"," ")[2]) else 0

    val hour1 = time1.substring(0, time1.indexOf(":")).toInt()
    val hour2 = time2.substring(0, time2.indexOf(":")).toInt()


    val ampm1 = time1.substring(indexOfAOrP(time1)).toLowerCase()//to allow "4:30pm", end index should be: {if test1a.contains("a",ignoreCase=true) indexOf("a", ignoreCase=true) else indexOf("p", ignoreCase=true)}
    val ampm2 = time2.substring(indexOfAOrP(time2)).toLowerCase()

    print(hour1,minute1,second1,ampm1)
//    print(hour1,minute1,second1,ampm1)
    print(hour2,minute2,second2,ampm2)

}
fun print(x: Int, y: Int, z: Int, a: String){
    println(x)
    println(y)
    println(z)
    println(a)
}