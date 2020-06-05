import Main.*
import MainV1FullyFunctional_UsesInts.formatAddAndReformatTimes
import java.util.regex.Pattern
import MainV1FullyFunctional_UsesInts.hhCmmCssAMPM
import MainV1FullyFunctional_UsesInts.to24HourFormat
import MainV1FullyFunctional_UsesInts.toSeconds
import MainV1FullyFunctional_UsesInts.toHrMinSec
import MainV1FullyFunctional_UsesInts.getInput
import MainV1FullyFunctional_UsesInts.printTimeConcisely


fun main() {
    timeAddMulti()
}

fun timeAddMulti() {
    var inputNumber = 1
    println("Enter numbers in any of the following formats:  15:22:31 (15 hr 22 min 31 sec), h15:22 (15 hr 22 min), m12:31 (12 min 31 sec), h4:2 (4 hr 2 min), m1:31 (1 min 31 sec), h12 (12 hr), m12 (12 min), or s12 (12 sec)")
    print("Time$inputNumber: ")
    var input = getInput()
    var hour = 0
    var minute = 0
    var second = 0
    //Collect input times:
    while (true) {
        if (Pattern.matches(hhCmmCss, input)) { //such as
            val times = input.split(":".toRegex()).toTypedArray()
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,times[0].toInt(), times[1].toInt(), times[2].toInt()).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else if (Pattern.matches(hhhCmm, input)) { //if in h:mm format
            input = input.substring(1)
            val times = input.split(":".toRegex()).toTypedArray()
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,times[0].toInt(), times[1].toInt(), 0).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else if (Pattern.matches(mmmCss, input)) { //if in mmm(e.g. m42) format
            input = input.substring(1)
            val times = input.split(":".toRegex()).toTypedArray()
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second, 0, times[0].toInt(), times[1].toInt()).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else if (Pattern.matches(mmm, input)) { //if in mmm(e.g. m42) format
            input = input.substring(1)
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,0, input.toInt(), 0).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else if (Pattern.matches(hhh, input)) {
            input = input.substring(1)
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,input.toInt(), 0, 0).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else if (Pattern.matches(sss, input)) { //if in sss(e.g. s42) format
            input = input.substring(1)
            val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,0, 0, input.toInt()).split(",")
            hour=newHour.toInt()
            minute=newMinute.toInt()
            second=newSecond.toInt()
        }
        else {
            if (!(input == "total" || input == "done")) {
                println("Incorrectly Formatted Number. Try Again.")
                print("Time$inputNumber: ")
                input = getInput()
                continue
            }
            else if (input == "done") break
            else if (input == "total") {
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,0, 0, 0).split(",")
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
                printTimeConcisely(hour, minute, second)
                println()
                print("Time$inputNumber: ")
                input = getInput()
                continue
            }
        }
        inputNumber += 1
        print("Time$inputNumber: ")
        input = getInput()
    }
    println()
    val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(hour,minute,second,0, 0, 0).split(",")
    hour=newHour.toInt()
    minute=newMinute.toInt()
    second=newSecond.toInt()

    printTimeConcisely(hour, minute, second)
    println()
}
