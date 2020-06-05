import MainV1FullyFunctional_UsesInts.formatAddAndReformatTimes
import MainV1FullyFunctional_UsesInts.hhCmm
import MainV1FullyFunctional_UsesInts.hhCmmCss
import MainV1FullyFunctional_UsesInts.hhh
import MainV1FullyFunctional_UsesInts.mm
import MainV1FullyFunctional_UsesInts.mmCss
import MainV1FullyFunctional_UsesInts.mmm
import MainV1FullyFunctional_UsesInts.ss
import MainV1FullyFunctional_UsesInts.sss
import java.util.regex.Pattern

fun main() {
    println(parseExcelTimes(listOf("444","3a","666"),ss,1))//should return 27 min 45 sec
}
private fun parseExcelTimes(times: List<String>, regex: String, mode: Int): List<Int>? {
    var hour = 0
    var minute = 0
    var second = 0
    for (time1 in times) {
        var time = time1
        if (Pattern.matches(regex, time)) {
            //tree of destructuring decision based on time format:
            if (regex == hhCmmCss) { //if in hh:mm:ss format
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    times[0].toInt(),
                    times[1].toInt(),
                    times[2].toInt()
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == hhCmm && mode == 4) { //if in h:mm format
                //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds. I am assuming it is 0
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    times[0].toInt(),
                    times[1].toInt(),
                    0
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == mmCss && mode == 3) { //if in mm:ss format
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    0,
                    times[0].toInt(),
                    times[1].toInt()
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == mmm) { //if in mmm(e.g. m42) format
                //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    0,
                    time.toInt(),
                    0
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == hhh) {
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    time.toInt(),
                    0,
                    0
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == sss) {
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    0,
                    0,
                    time.toInt()
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == mm && mode == 2) {
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    0,
                    time.toInt(),
                    0
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
            else if (regex == ss && mode == 1) {
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    hour,
                    minute,
                    second,
                    0,
                    0,
                    time.toInt()
                ).split(",")
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            }
        } else {
            println("The value \"$time1\" at position ${times.indexOf(time1)+1} was incorrectly formatted. Please correct the value and enter the file path again: ")
            return null
        }
        val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
            hour,
            minute,
            second,
            0,
            0,
            0
        ).split(",")
        hour = newHour.toInt()
        minute = newMinute.toInt()
        second = newSecond.toInt()
    }
    return listOf(hour, minute, second)
}
