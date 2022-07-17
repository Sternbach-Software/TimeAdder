import MainCleanedUp.mmCss
import MainCleanedUp.hhh
import MainCleanedUp.mm
import MainCleanedUp.ss
import MainCleanedUp.printTimeConcisely
import MainCleanedUp.toHrMinSec
import MainCleanedUp.mmm
import MainCleanedUp.sss
import MainCleanedUp.hhCmm
import MainCleanedUp.hhCmmCss
import MainCleanedUp.getInput
import MainCleanedUp.formatAddAndReformatTimes
import java.util.regex.Pattern

private fun timeAdd(regex: String, mode: Int, message: String) {

    var inputNumber = 1
    var hour = 0
    var minute = 0
    var second = 0

    println(message /*"Enter numbers in any of the following formats:  15:22 (i.e. 15 hr 22 min), 4:22 (4 hr 22 min), m42 (42 min), or h12 (12 hr)"*/)

    print("Time$inputNumber: ")
    var input = getInput()

    //Collect input times:
    while (true) {
        if (Pattern.matches(regex, input)) {
            //tree of destructuring decision base on time format:
            if (regex == hhCmmCss) { //if in hh:mm:ss format
                val times = input.split(":".toRegex()).toTypedArray()
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(
                    times[0].toInt(),
                    times[1].toInt(),
                    times[2].toInt()
                )
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == hhCmm && mode == 4) { //if in h:mm format
                //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds. I am assuming it is 0
                val times = input.split(":".toRegex()).toTypedArray()
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(
                    times[0].toInt(),
                    times[1].toInt(),
                    0
                )
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == mmCss && mode == 3) { //if in mm:ss format
                val times = input.split(":".toRegex()).toTypedArray()
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(
                    0,
                    times[0].toInt(),
                    times[1].toInt()
                )
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == mmm) { //if in mmm(e.g. m42) format
                //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds
                input = input.substring(1)
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(0, input.toInt(), 0)
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == hhh) {
                input = input.substring(1)
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(input.toInt(), 0, 0)
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == sss) {
                input = input.substring(1)
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(0, 0, input.toInt())
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == mm && mode == 2) {
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(0, input.toInt(), 0)
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
            else if (regex == ss && mode == 1) {
                val (newHour,newMinute,newSecond)= formatAddAndReformatTimes(0, 0, input.toInt())
                hour=newHour.toInt()
                minute=newMinute.toInt()
                second=newSecond.toInt()
            }
        }
        else {
            if (!(input == "total" || input == "done")) {
                println("Incorrectly Formatted Number. Try Again.")
                println("Time$inputNumber: ")
                input = getInput()
                continue
            }
            else if (input == "done") break
            else if (input == "total") {
                val formattedTime= toHrMinSec(hour,minute,second)
                hour=formattedTime.first
                minute=formattedTime.second
                second=formattedTime.third
                printTimeConcisely()
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
    val triple = toHrMinSec(hour, minute, second)
    hour = triple.first
    minute = triple.second
    second = triple.third
    printTimeConcisely()
    println()
}
