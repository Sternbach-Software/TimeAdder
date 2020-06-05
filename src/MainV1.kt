import java.lang.Integer.parseInt
import java.text.DecimalFormat
import java.text.ParseException
import java.util.regex.Pattern

object MainV1 {
    //    var multi = "[hm]?[0-9]?[0-9]?[0-9]?:?[hms][0-9]?[0-9]?[0-9]"
    private var hhCmmCss = "-?[0-9]+:-?[0-9]+:-?[0-9]+" //such as "15:22:16"
    private var hhhCmm = "h-?[0-9]+:-?[0-9]+" //such as "h15:22"
    private var hhh = "h-?[0-9]+" //such as "h15"
    private var hhCmm = "-?[0-9]+:-?[0-9]+" //such as "15:22"
    private var mmmCss = "m-?[0-9]+:-?[0-9]+" //such as "m25:16"
    private var mmm = "m-?[0-9]+" //such as "m25"
    private var mmCss = "-?[0-9]+:-?[0-9]+" //such as "22:26"
    private var mm = "-?[0-9]+" //only to be used in Minute Mode(2)
    private var sss = "s-?[0-9]+" //such as "s25"
    private var ss = "-?[0-9]+?" //only to be used in Second Mode(1)
    //TODO put comment in mmCss and hhCmm that you can add numbers like 1052:16 (i.e. 1052 min 16 sec or 1052 hr 16 min)
    //TODO make sure minute mode wont print seconds
    //TODO add feature to deal with adding seconds and minutes larger than 59(e.g. m12:798)
    //TODO
    //TODO add MainScreen GUI with radio buttons for each mode
    //TODO add adder gui
    //TODO add start and end time gui with one slot for each
    //TODO add a constantly updating total every time you enter a value
    //TODO the time adder should have the ability to add a time by pressing enter so you dont have to take your hands off the keyboard
    //TODO add big text editor pane which you could paste (e.g. from Excel) a list of properly formatted times(using .split("\n")  )
    //TODO Give the user an option in the main menu as to whether they are adding decimal values or integer values. If they are adding decimal values, direct them to a whole 'nother version of the program where every long and int is replaced with a double
    //TODO update instructions for each mode to reflect the types of numbers they can process and their capabilities
    //TODO update "Incorrectly Formatted" messages to include the new types of numbers the functions can and can't now hold, and make the messages uniform
    //    TODO should i implement the TryAgainException Class and make every "Incorrectly formatted" uniform like that?
    //fix the big that the results display times like 8238.641666666668 hr
    @Throws(ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        println("Would you like to use the Second Mode(1), Minute Mode(2), Minute/Second Mode(3), Hour/Minute Mode(4),\nHour/Minute/Second Mode(5), Multi Mode(6), or Start/End Mode(7)? Once started, type \"done\" to see the final result or \"total\" to see the current total:")
        when (getInput()) {
            "1" -> timeAdd(ss, 1, "Enter numbers in the following format: \"16\" (i.e. 16 sec), up to \"9999\"")
            "2" -> timeAdd(mm, 2, "Enter numbers in the following format: \"22\" (i.e. 22 min), up to \"9999\"")
            "3" -> timeAdd(
                mmCss,
                3,
                "Enter numbers in the following format: \"22:16\" (i.e. 22 min 16 sec), up to \"9999:59\""
            )
            "4" -> timeAdd(
                hhCmm,
                4,
                "Enter numbers in the following format: \"15:22\" (i.e. 15 hr 22 min), up to \"9999:59\""
            )
            "5" -> timeAdd(
                hhCmmCss,
                5,
                "Enter numbers in the following format: \"15:22:16\" (i.e. 15 hr 22 min 16 sec), up to \"9999:59:59\""
            )
            "6" -> timeAddMulti()
            "7" -> startEndMode()
        }
    }
    private fun timeAdd(regex: String, mode: Int, message: String) {
        var inputNumber = 1
        println(message /*"Enter numbers in any of the following formats:  15:22 (i.e. 15 hr 22 min), 4:22 (4 hr 22 min), m42 (42 min), or h12 (12 hr)"*/)
        print("Time$inputNumber: ")
        var input = getInput()
        var hour = 0.0
        var minute = 0.0
        var second = 0.0
        //Collect input times:
        while (true) {
            if (Pattern.matches(regex, input)) {
                //tree of destructuring decision base on time format:
                if (regex == hhCmmCss) { //if in h:mm format
                    val times = input.split(":".toRegex()).toTypedArray()
                    hour += times[0].toDouble()
                    if (minute + times[1].toDouble() > 60) {
                        minute = minute + times[1].toDouble() - 60
                        hour += 1.0
                    } else minute += times[1].toDouble()
                    if (second + times[2].toDouble() > 60) {
                        second = second + times[2].toDouble() - 60
                        minute += 1.0
                    } else second += times[2].toDouble()
                } else if (regex == hhCmm && mode == 4) { //if in h:mm format
                    val times = input.split(":".toRegex()).toTypedArray()
                    hour += times[0].toDouble()
                    if (minute + times[1].toDouble() > 60) {
                        minute = minute + times[1].toDouble() - 60
                        hour += 1.0
                    } else minute += times[1].toDouble()
                } else if (regex == mmCss && mode == 3) { //if in mm:ss format
                    val times = input.split(":".toRegex()).toTypedArray()
                    if (minute + times[0].toDouble() > 60) {
                        minute = minute + times[0].toDouble() - 60
                        hour += 1.0
                    } else minute += times[0].toDouble()
                    if (second + times[1].toDouble() > 60) {
                        second = second + times[1].toDouble() - 60
                        minute += 1.0
                    } else second += times[1].toDouble()
                } else if (regex == mmm) { //if in mmm(e.g. m42) format
                    input = input.substring(1)
                    if (minute + input.toDouble() > 60) {
                        minute = minute + input.toDouble() - 60
                        hour += 1.0
                    } else minute += input.toDouble()
                } else if (regex == hhh) {
                    input = input.substring(1)
                    hour += input.toDouble()
                } else if (regex == mm && mode == 2) {
                    if (minute + input.toDouble() > 60) {
                        minute = minute + input.toDouble() - 60
                        hour += 1.0
                    } else minute += input.toDouble()
                } else if (regex == ss && mode == 1) {
                    if (second + input.toDouble() > 60) {
                        second = second + input.toDouble() - 60
                        minute += 1.0
                    } else second += input.toDouble()
                }
            }
            else {
                if (!(input == "total" || input == "done")) {
                    println("Incorrectly Formatted Number. Try Again.")
                    println("Time$inputNumber: ")
                    input = getInput()
                    continue
                } else if (input == "done") break
                else if (input == "total") {
                    minute += (second / 60)
                    hour += (minute / 60)
                    second %= 60
                    minute %= 60
                    val secondString = second.toString()
                    val sintegerPlaces = secondString.indexOf('.')
                    val sdecimalPlaces = secondString.length - sintegerPlaces - 1
                    var dfs: DecimalFormat
                    dfs = if (sdecimalPlaces > 3) {
                        DecimalFormat("#." + "#".repeat(sdecimalPlaces))
                    } else {
                        DecimalFormat("#.#" + "#".repeat(sdecimalPlaces))
                    }
                    val minuteString = minute.toString()
                    val mintegerPlaces = minuteString.indexOf('.')
                    val mdecimalPlaces = minuteString.length - mintegerPlaces - 1
                    val dfm = DecimalFormat("#.#" + "#".repeat(mdecimalPlaces))
                    val hourString = hour.toString()
                    val hintegerPlaces = hourString.indexOf('.')
                    val hdecimalPlaces = hourString.length - hintegerPlaces - 1
                    val dfh = DecimalFormat("#.#" + "#".repeat(hdecimalPlaces))
                    when {
                        (second.toInt() > 0) and (hour.toInt() > 0) and (minute.toInt() > 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(minute.toInt()) + " min " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() == 0) and (minute.toInt() == 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(
                                minute.toInt()
                            ) + " min " + dfs.format(second.toInt()) + " sec"
                        )
                        (second.toInt() > 0) and (hour.toInt() > 0) and (minute.toInt() == 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() > 0) and (hour.toInt() == 0) and (minute.toInt() > 0) -> print(
                            dfm.format(minute.toInt()) + " min " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() > 0) and (minute.toInt() > 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(
                                minute.toInt()
                            ) + " min"
                        )
                        (second.toInt() == 0) and (hour.toInt() == 0) and (minute.toInt().toInt() > 0) -> print(dfm.format(minute.toInt()) + " min")
                        (second.toInt() > 0) and (hour.toInt() == 0) and (minute.toInt() == 0) -> print(
                            dfs.format(second.toInt()) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() > 0) and (minute.toInt() == 0) -> print(dfh.format(hour.toInt()) + " hr")
                    }
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
        minute += (second / 60)
        hour += (minute / 60)
        second %= 60
        minute %= 60
        val secondString = second.toString()
        val sintegerPlaces = secondString.indexOf('.')
        val sdecimalPlaces = secondString.length - sintegerPlaces - 1
        val dfs = DecimalFormat("#.#" + "#".repeat(sdecimalPlaces))
        val minuteString = minute.toString()
        val mintegerPlaces = minuteString.indexOf('.')
        val mdecimalPlaces = minuteString.length - mintegerPlaces - 1
        val dfm = DecimalFormat("#.#" + "#".repeat(mdecimalPlaces))
        val hourString = hour.toString()
        val hintegerPlaces = hourString.indexOf('.')
        val hdecimalPlaces = hourString.length - hintegerPlaces - 1
        val dfh = DecimalFormat("#.#" + "#".repeat(hdecimalPlaces))
        if (second > 0) print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec") else println(
            dfh.format(hour) + " hr " + dfm.format(minute) + " min"
        )
        println()
    }
    @Throws(ParseException::class)
    private fun startEndMode() {
        var i = 1
        var ab = "a"
        println("Enter Time $i$ab:")
        var time1 = getInput()
        ab = "b"
        println("Enter Time $i$ab:")
        var time2 = getInput()
        while (true) {
            val minute1: Int = time1.substring(time1.indexOf(":") + 1, time1.indexOf(":") + 3).trimEnd().toInt()
            val minute2: Int = time2.substring(time2.indexOf(":") + 1, time2.indexOf(":") + 3).trimEnd().toInt()
            val second1: Int = if (time1.split(":").size > 2) parseInt(time1.split(":", " ")[2]) else 0
            val second2: Int = if (time2.split(":").size > 2) parseInt(time2.split(":", " ")[2]) else 0
            var hour1 = time1.substring(0, time1.indexOf(":")).toInt()
            var hour2 = time2.substring(0, time2.indexOf(":")).toInt()
            val ampm1 = time1.substring(indexOfAOrP(time1)).toLowerCase()
            val ampm2 = time2.substring(indexOfAOrP(time2)).toLowerCase()


            var secondsElapsedBetweenTimes: Int

            //convert AM-PM to 24-hour format
            hour1 = hour1.to24HourFormat(ampm1)
            hour2 = hour2.to24HourFormat(ampm2)

            //Convert hh:mm:ss to seconds
            val totalSeconds1 = (hour1 * 3600) + (minute1 * 60) + second1
            val totalSeconds2 = (hour2 * 3600) + (minute2 * 60) + second2
            secondsElapsedBetweenTimes = totalSeconds2 - totalSeconds1

            //convert seconds to hh:mm:ss
            var minutesElapsedBetweenTimes = 0
            var hoursElapsedBetweenTimes = 0
            minutesElapsedBetweenTimes += (secondsElapsedBetweenTimes / 60)
            hoursElapsedBetweenTimes += (minutesElapsedBetweenTimes / 60)
            secondsElapsedBetweenTimes %= 60
            minutesElapsedBetweenTimes %= 60
            i++
            ab = "a"
            println("Enter Time $i$ab:")
            time1 = getInput()
            if (time1 == "total") {
                println("$hoursElapsedBetweenTimes hr $minutesElapsedBetweenTimes min $secondsElapsedBetweenTimes sec")
                println("Enter Time $i$ab:")
                time1 = getInput()
            }
            if (time1 == "done") break
            ab = "b"
            println("Enter Time $i$ab:")
            time2 = getInput()
            if (time2 == "total") {
                println("$hoursElapsedBetweenTimes hr $minutesElapsedBetweenTimes min $secondsElapsedBetweenTimes sec")
                println("Enter Time $i$ab:")
                time2 = getInput()
            }
            if (time2 == "done") break
        }
    }
    private fun timeAddMulti() {
        var inputNumber = 1
        println("Enter numbers in any of the following formats:  15:22:31 (15 hr 22 min 31 sec), h15:22 (15 hr 22 min),\nm12:31 (12 min 31 sec), h4:22 (4 hr 22 min), m1:31 (1 min 31 sec), h12 (12 hr), m12 (12 min), or s12 (12 sec)")
        print("Time$inputNumber: ")
        var input = getInput()
        var hour = 0.0
        var minute = 0.0
        var second = 0.0
        //Collect input times:
        while (true) {
            if (Pattern.matches(hhCmmCss, input)) { //such as
                val times = input.split(":".toRegex()).toTypedArray()
                hour += times[0].toDouble()
                if (minute + times[1].toDouble() > 60) {
                    minute = minute + times[1].toDouble() - 60
                    hour += 1.0
                } else minute += times[1].toDouble()
                if (second + times[2].toDouble() > 60) {
                    second = second + times[2].toDouble() - 60
                    minute += 1.0
                } else second += times[2].toDouble()
            } else if (Pattern.matches(hhhCmm, input)) { //if in h:mm format
                input = input.substring(1)
                val times = input.split(":".toRegex()).toTypedArray()
                hour += times[0].toDouble()
                if (minute + times[1].toDouble() > 60) {
                    minute = minute + times[1].toDouble() - 60
                    hour += 1.0
                } else minute += times[1].toDouble()
            } else if (Pattern.matches(mmmCss, input)) { //if in mmm(e.g. m42) format
                input = input.substring(1)
                val times = input.split(":".toRegex()).toTypedArray()
                if (minute + times[0].toDouble() > 60) {
                    minute = minute + times[0].toDouble() - 60
                    hour += 1.0
                } else minute += times[0].toDouble()
                if (second + times[1].toDouble() > 60) {
                    second = second + times[1].toDouble() - 60
                    minute += 1.0
                } else second += times[1].toDouble()
            } else if (Pattern.matches(mmm, input)) { //if in mmm(e.g. m42) format
                input = input.substring(1)
                if (minute + input.toDouble() > 60) {
                    minute = minute + input.toDouble() - 60
                    hour += 1.0
                } else minute += input.toDouble()
            } else if (Pattern.matches(hhh, input)) {
                input = input.substring(1)
                hour += input.toDouble()
            } else if (Pattern.matches(sss, input)) { //if in sss(e.g. s42) format
                input = input.substring(1)
                if (second + input.toDouble() > 60) {
                    second = second + input.toDouble() - 60
                    minute += 1.0
                } else second += input.toDouble()
            } else {
                if (!(input == "total" || input == "done")) {
                    println("Incorrectly Formatted Number. Try Again.")
                    print("Time$inputNumber: ")
                    input = getInput()
                    continue
                } else if (input == "done") break else if (input == "total") {
                    minute += second / 60
                    hour += minute / 60
                    second %= 60
                    minute %= 60
                    val secondString = second.toString()
                    val sintegerPlaces = secondString.indexOf('.')
                    val sdecimalPlaces = secondString.length - sintegerPlaces - 1
                    val dfs = DecimalFormat("#.#" + "#".repeat(sdecimalPlaces))
                    val minuteString = minute.toString()
                    val mintegerPlaces = minuteString.indexOf('.')
                    val mdecimalPlaces = minuteString.length - mintegerPlaces - 1
                    val dfm = DecimalFormat("#.#" + "#".repeat(mdecimalPlaces))
                    val hourString = hour.toString()
                    val hintegerPlaces = hourString.indexOf('.')
                    val hdecimalPlaces = hourString.length - hintegerPlaces - 1
                    val dfh = DecimalFormat("#.#" + "#".repeat(hdecimalPlaces))
                    when {
                        (second.toInt() > 0) and (hour.toInt() > 0) and (minute.toInt() > 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(minute.toInt()) + " min " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() == 0) and (minute.toInt() == 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(
                                minute.toInt()
                            ) + " min " + dfs.format(second.toInt()) + " sec"
                        )
                        (second.toInt() > 0) and (hour.toInt() > 0) and (minute.toInt() == 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() > 0) and (hour.toInt() == 0) and (minute.toInt() > 0) -> print(
                            dfm.format(minute.toInt()) + " min " + dfs.format(
                                second.toInt()
                            ) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() > 0) and (minute.toInt() > 0) -> print(
                            dfh.format(hour.toInt()) + " hr " + dfm.format(
                                minute.toInt()
                            ) + " min"
                        )
                        (second.toInt() == 0) and (hour.toInt() == 0) and (minute.toInt().toInt() > 0) -> print(dfm.format(minute.toInt()) + " min")
                        (second.toInt() > 0) and (hour.toInt() == 0) and (minute.toInt() == 0) -> print(
                            dfs.format(second.toInt()) + " sec"
                        )
                        (second.toInt() == 0) and (hour.toInt() > 0) and (minute.toInt() == 0) -> print(dfh.format(hour.toInt()) + " hr")
                    }
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
        minute += second / 60
        hour += minute / 60
        second %= 60
        minute %= 60
        val secondString = second.toString()
        val sintegerPlaces = secondString.indexOf('.')
        val sdecimalPlaces = secondString.length - sintegerPlaces - 1
        val dfs = DecimalFormat("#.#" + "#".repeat(sdecimalPlaces))
        val minuteString = minute.toString()
        val mintegerPlaces = minuteString.indexOf('.')
        val mdecimalPlaces = minuteString.length - mintegerPlaces - 1
        val dfm = DecimalFormat("#.#" + "#".repeat(mdecimalPlaces))
        val hourString = hour.toString()
        val hintegerPlaces = hourString.indexOf('.')
        val hdecimalPlaces = hourString.length - hintegerPlaces - 1
        val dfh = DecimalFormat("#.#" + "#".repeat(hdecimalPlaces))
        when {
            (second == 0.0) and (hour > 0) and (minute == 0.0) -> print(dfh.format(hour) + " hr")
            (second > 0) and (hour == 0.0) and (minute == 0.0) -> print(dfs.format(second) + " sec")
            (second == 0.0) and (hour == 0.0) and (minute > 0) -> print(dfm.format(minute) + " min")
            (second > 0) and (hour > 0) and (minute == 0.0) -> print(dfh.format(hour) + " hr " + dfs.format(second) + " sec")
            (second == 0.0) and (hour > 0) and (minute > 0) -> print(dfh.format(hour) + " hr " + dfm.format(minute) + " min")
            (second > 0) and (hour == 0.0) and (minute > 0) -> print(dfm.format(minute) + " min " + dfs.format(second) + " sec")
            (second > 0) and (hour > 0) and (minute > 0) -> print(
                dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(
                    second
                ) + " sec"
            )
            (second == 0.0) and (hour == 0.0) and (minute == 0.0) -> print(
                dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(
                    second
                ) + " sec"
            )
        }
    }
    private fun getInput() = readLine() ?: ""
    private fun Int.to24HourFormat(ampm: String): Int {
        var hour21: Int = this
        if (this == 12 && ampm == "am") hour21 =
            0 //if ampm1 is "pm" (i.e. it is 12:00pm going into 1:00pm/13:00) it should be 12:00
        when (hour21) {
            1 -> hour21 = if (ampm == "pm") {
                13
            } else 1
            2 -> hour21 = if (ampm == "pm") {
                14
            } else 2
            3 -> hour21 = if (ampm == "pm") {
                15
            } else 3
            4 -> hour21 = if (ampm == "pm") {
                16
            } else 4
            5 -> hour21 = if (ampm == "pm") {
                17
            } else 5
            6 -> hour21 = if (ampm == "pm") {
                18
            } else 6
            7 -> hour21 = if (ampm == "pm") {
                19
            } else 7
            8 -> hour21 = if (ampm == "pm") {
                20
            } else 8
            9 -> hour21 = if (ampm == "pm") {
                21
            } else 9
            10 -> hour21 = if (ampm == "pm") {
                22
            } else 10
            11 -> hour21 = if (ampm == "pm") {
                23
            } else 11
        }
        return hour21
    }
}