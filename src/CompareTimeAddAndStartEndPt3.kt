import java.util.regex.Pattern
import MainV1FullyFunctional_UsesInts.hhCmmCssAMPM
import MainV1FullyFunctional_UsesInts.to24HourFormat
import MainV1FullyFunctional_UsesInts.toSeconds
import MainV1FullyFunctional_UsesInts.toHrMinSec
import MainV1FullyFunctional_UsesInts.getInput
import MainV1FullyFunctional_UsesInts.printTimeConcisely
import java.lang.Integer.parseInt


fun main() {
    startEndMode()
}

fun startEndMode() {
    var i = 1
    var ab = "a"
    print("Enter Time $i$ab: ")
    var time1 = getInput()
    while(!Pattern.matches(hhCmmCssAMPM,time1)){
        if(time1=="total") println("No time calculated yet. No total to display.")
        else println("Incorrectly Formatted Number. Try Again.")
        print("Enter Time $i$ab: ")
        time1 = getInput()
    }
    ab = "b"
    print("Enter Time $i$ab: ")
    var time2 = getInput()
    while(!Pattern.matches(hhCmmCssAMPM,time2)){
        if(time2=="total") println("No time calculated yet. No total to display.")
        else println("Incorrectly Formatted Number. Try Again.")
        print("Enter Time $i$ab: ")
        time2 = getInput()
    }
    while (true) {
        var hour1 = time1.substring(0, time1.indexOf(":")).toInt()
        val minute1: Int = time1.substring(time1.indexOf(":") + 1, time1.indexOf(":") + 3).trimEnd().toInt()
        val second1: Int = if (time1.split(":").size > 2) parseInt(time1.split(":", " ")[2]) else 0
        val ampm1 = time1.substring(indexOfAOrP(time1)).toLowerCase()

        var hour2 = time2.substring(0, time2.indexOf(":")).toInt()
        val minute2: Int = time2.substring(time2.indexOf(":") + 1, time2.indexOf(":") + 3).trimEnd().toInt()
        val second2: Int = if (time2.split(":").size > 2) parseInt(time2.split(":", " ")[2]) else 0
        val ampm2 = time2.substring(indexOfAOrP(time2)).toLowerCase()

        //convert AM-PM to 24-hour format
        hour1 = hour1.to24HourFormat(ampm1)
        hour2 = hour2.to24HourFormat(ampm2)

        //Convert hh:mm:ss to seconds
        val totalSeconds1 = toSeconds(hour1, minute1, second1)
        val totalSeconds2 = toSeconds(hour2, minute2, second2)
        var secondsElapsedBetweenTimes = totalSeconds2 - totalSeconds1

        //convert seconds to hh:mm:ss
        var minutesElapsedBetweenTimes = 0
        var hoursElapsedBetweenTimes = 0
        val triple = toHrMinSec(hoursElapsedBetweenTimes, minutesElapsedBetweenTimes, secondsElapsedBetweenTimes)

        hoursElapsedBetweenTimes = triple.first
        minutesElapsedBetweenTimes = triple.second
        secondsElapsedBetweenTimes = triple.third

        while(true) {
            i++
            ab = "a"
            print("Enter Time $i$ab:")
            time1 = getInput()
            while (time1 == "total") {
                printTimeConcisely(hoursElapsedBetweenTimes, minutesElapsedBetweenTimes, secondsElapsedBetweenTimes)
                println()
                print("Enter Time $i$ab:")
                time1 = getInput()
            }
            if (time1 == "done") break
            else {
                println("Incorrectly Formatted Number. Try Again.")
                print("Enter Time $i$ab:")
                time1 = getInput()
            }
            ab = "b"
            println("Enter Time $i$ab:")
            time2 = getInput()
            while (time2 == "total") {
                printTimeConcisely(hoursElapsedBetweenTimes, minutesElapsedBetweenTimes, secondsElapsedBetweenTimes)
                println()
                print("Enter Time $i$ab:")
                time2 = getInput()
            }
            if (time2 == "done") break
            else {
                println("Incorrectly Formatted Number. Try Again.")
                print("Enter Time $i$ab:")
                time2 = getInput()
            }
        }
    }
}
