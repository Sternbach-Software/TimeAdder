import java.io.File
import java.text.ParseException
import java.util.regex.Pattern
import kotlin.properties.Delegates

object MainCleanedUp {
    var hhCmmCssAMPM = "[0-9]+:[0-9]+:?[0-9]+? [AaPpMm]{2}" //such as "3:22:16 PM"
    var hhCmmCss = "-?[0-9]+:-?[0-9]+:-?[0-9]+" //such as "15:22:16"
    var hhhCmm = "h-?[0-9]+:-?[0-9]+" //such as "h15:22"
    var hhh = "h-?[0-9]+" //such as "h15"
    var hhCmm = "-?[0-9]+:-?[0-9]+" //such as "15:22"
    var mmmCss = "m-?[0-9]+:-?[0-9]+" //such as "m25:16"
    var mmm = "m-?[0-9]+" //such as "m25"
    var mmCss = "-?[0-9]+:-?[0-9]+" //such as "22:26"
    var mm = "-?[0-9]+" //only to be used in Minute Mode(2)
    var sss = "s-?[0-9]+" //such as "s25"
    var ss = "-?[0-9]+?" //only to be used in Second Mode(1)

    //Internal accumulators
    var _hour = 0
    var _minute = 0
    var _second = 0
    var totalSeconds = 0

    //For undoing previous addition operation
    var _previousHour = 0
    var _previousMinute = 0
    var _previousSecond = 0

    //TODO make TimeAdder GUI
    //TODO add MainScreen GUI with radio buttons for each mode
    //TODO add adder gui
    //TODO add start and end time gui with one slot for each
    //TODO add a constantly updating total every time you enter a value
    //TODO the time adder should have the ability to add a time by pressing enter so you dont have to take your hands off the keyboard
    //TODO add big text editor pane which you could paste (e.g. from Excel) a list of properly formatted times(using .split("\n")  )

    //TODO checklist for addition of "excel" feature

    //TODO should accommodate the possibility of one of the times/lines ending in a space(i.e. trim() )
    //TODO add "excel" to startEnd()
    //TODO when checking if parseExcelTimeStartEnd() returned null(meaning that a value in one of the files was formatted incorrectly), check if the variable wasThereAProblemWithTimeAOrTimeB equals "a" or "b" to know whether the problem is with timeA(start time) or timeB(end time) to appropriately inform the user
    //TODO add error message in case one of the files has a time which doesnt have a counterpart, e.g. there is a time in the start times file which does not have a counterpart in the end times file
    var shouldIFinish =
        false//for some reason, when timeAddMulti would finish and go to main menu, it would loop asking the user which mode they wanted, so I introcuced a check before when(getInput()) to this variable to stop the loop if i came from Multi where I set this to true
    lateinit var wasThereAProblemWithTimeAOrTimeB: String

    /**
     * Request input from the user by first printing [firstMessageToDisplay] and then calling readLine()
     * Loops for input until the input matches the provided [regex], printing [messageToDisplayOnError] every time the user enters an invalid input until a valid input is entered
     * @param regex the regex to check the input against
     * @param firstMessageToDisplay message to be displayed before input is requested; The string ": " will be appended.
     * @param messageToDisplayOnError message to be displayed when user's input does not  match [regex]; The string ": " will be appended.
     * @return an input from the user which matches [predicate]
     * */
    fun getValidatedInput(
        regex: Regex,
        firstMessageToDisplay: String,
        messageToDisplayOnError: String
    ) = getValidatedInput(firstMessageToDisplay, messageToDisplayOnError) {
        (it?.matches(regex)?.not() == true) /*doesn't match regex (written in a roundabout way to retain nullability)*/
    }

    fun getValidatedInput(
        firstMessageToDisplay: String,
        messageToDisplayOnError: String,
        isValid: (String?) -> Boolean
    ): String? {
        print("$firstMessageToDisplay: ")
        var input = readLine()?.trim()
        while (!isValid(input)) {
            print("$messageToDisplayOnError: ")
            input = readLine()?.trim()
        }
        return input
    }

    @Throws(ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        lateinit var regex: String
        var mode by Delegates.notNull<Int>()
        lateinit var message: String
        lateinit var getHrMinSecFromInput: (String) -> Triple<Int, Int, Int>
        entireProgram@ while (true) {
            val response = getValidatedInput(
                "[1234567]".toRegex(),
                "Would you like to use the Second Mode(1), Minute Mode(2), Minute/Second Mode(3), Hour/Minute Mode(4),\nHour/Minute/Second Mode(5), Multi Mode(6), or Start/End Mode(7)? Once started, type \"done\" to see the final result and exit, \"total\" to see the current total, \"reset\" to reset the running total to 0 and restart the current mode from Time1, \"excel\" to enter a list of times to add (or for Start/End Mode, start times and end times to add together,) or \"main\" to go back to the main menu(doing so will reset the running total to 0 without first displaying the running total):",
                "Invalid Input. Please Try Again: "
            )
            when (response) {
                "1" -> {
                    regex = ss
                    mode = 1
                    message =
                        "Enter numbers in the following format: \"116\" (i.e. 16 sec) or \"-116\" to add or subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        Triple(0, 0, it.toInt())
                    }
                }
                "2" -> {
                    regex = mm
                    mode = 2
                    message =
                        "Enter numbers in the following format: \"222\" (i.e. 222 min) or \"-222\" to add or subtract 222 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        Triple(0, it.toInt(), 0)
                    }
                }
                "3" -> {
                    regex = mmCss
                    mode = 3
                    message =
                        "Enter numbers in the following format: \"222:16\" (i.e. 222 min 16 sec) or \"222:-16\", etc. to add 222 minutes and add/subtract 16 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        val times = it.split(":")
                        Triple(0, times[0].toInt(), times[1].toInt())
                    }
                }
                "4" -> {
                    regex = hhCmm
                    mode = 4
                    message =
                        "Enter numbers in the following format: \"115:22\" (i.e. 115 hr 22 min) or \"115:-22\", etc. to add 115 hours and add/subtract 22 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        val times = it.split(":")
                        Triple(times[0].toInt(), times[1].toInt(), 0)
                    }
                }
                "5" -> {
                    regex = hhCmmCss
                    mode = 5
                    message =
                        "Enter numbers in the following format: \"115:22:116\" (i.e. 115 hr 22 min 116 sec) or \"-115:22:-116\", etc. to add/subtract 115 hours, add 22 minutes, and add/subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        val times = it.split(":")
                        Triple(times[0].toInt(), times[1].toInt(), times[2].toInt())
                    }
                }
                "6" -> {
                    regex = listOf(
                        ss,
                        mm,
                        mmCss,
                        hhCmm,
                        hhCmmCss
                    ).joinToString("|", "(", ")") { "($it)" }
                    message =
                        "Enter numbers in any of the following formats:  15:22:31 (15 hr 22 min 31 sec), h15:22 (15 hr 22 min), h1:2 (1 hr 2 min), m22:31 (22 min 31 sec), m2:3 (2 min 3 sec), h15 (15 hr), h1 (1 hr), m22 (22 min), m2 (2 min), s31 (31 sec), or s3 (3 sec). Add a minus before any number, e.g. h-12:-14 to subtract that number from the total. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                    getHrMinSecFromInput = {
                        when {
                            Pattern.matches(hhCmmCss, it) -> { //such as
                                val times = it.split(":")
                                Triple(times[0].toInt(), times[1].toInt(), times[2].toInt())
                            }
                            Pattern.matches(hhhCmm, it) -> { //if in h:mm format
                                val times = it.substring(1).split(":")
                                Triple(times[0].toInt(), times[1].toInt(), 0)
                            }
                            Pattern.matches(mmmCss, it) -> { //if in mmm(e.g. m42) format
                                val times = it.substring(1).split(":")
                                Triple(0, times[0].toInt(), times[1].toInt())
                            }
                            Pattern.matches(mmm, it) -> { //if in mmm(e.g. m42) format
                                Triple(0, it.substring(1).toInt(), 0)
                            }
                            Pattern.matches(hhh, it) -> {
                                Triple(it.substring(1).toInt(), 0, 0)
                            }
                            Pattern.matches(sss, it) -> { //if in sss(e.g. s42) format
                                Triple(0, 0, it.substring(1).toInt())
                            }
                            else -> {
                                Triple(0, 0, 0)
                            }
                        }
                    }
                }
                "7" -> {
                    startEndMode()
                }
                else -> {}
            }
            val addingIndividualTimes = true
            if (addingIndividualTimes) {
                println(message /*"Enter numbers in any of the following formats:  15:22 (i.e. 15 hr 22 min), 4:22 (4 hr 22 min), m42 (42 min), or h12 (12 hr)"*/)
                var inputNumber = 1
                getInput@ while (true) {
                    var input = getValidatedInput(
                        getRegexOrOptions(regex)/*TODO reuse*/,
                        "Time$inputNumber: ",
                        "Incorrectly Formatted Number. Try Again."
                    )
                    if (Pattern.matches(regex, input)) {
                        val newTimes = getHrMinSecFromInput(input!!)
                        accumulateTime(newTimes.first, newTimes.second, newTimes.third)
                    } else {
                        when (input) {
                            "done" -> break@getInput
                            "total" -> {
                                printTimeConcisely()
                                continue@getInput
                            }
                            "reset" -> {
                                _hour = 0
                                _minute = 0
                                _second = 0

                                _previousHour = 0
                                _previousMinute = 0
                                _previousSecond = 0
                                println("Time reset to 0.")
                                continue@getInput
                            }
                            "undo" -> {
                                println(
                                    "Undoing addition of ${
                                        getConsiceTime(
                                            _previousHour,
                                            _previousMinute,
                                            _previousSecond
                                        )
                                    }"
                                )
                                accumulateTime(-_previousHour, -_previousMinute, -_previousSecond)
                                println("New total: ${getConsiceTime()}")
                                continue@getInput
                            }
                            "main" -> {
                                continue@entireProgram
                            }
                            "excel" -> {
                                val messageOfExcel =
                                    "Enter the file path to a .txt file which contains all of the times you would like to add with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\"."
                                println(messageOfExcel)
                                var file: File
                                getExcelInput@ while (true) {
                                    val pair = getValidFile(inputNumber, input) ?: continue@getInput
                                    file = pair.first
                                    input = pair.second
                                    val times = file.readLines()
                                    val listofHourMinuteAndSecondTotalfromfile =
                                        parseExcelTimes(times, regex, mode) ?: continue@getExcelInput
                                    accumulateTime(
                                        listofHourMinuteAndSecondTotalfromfile[0],
                                        listofHourMinuteAndSecondTotalfromfile[1],
                                        listofHourMinuteAndSecondTotalfromfile[2]
                                    )
                                    val wantsToContinue = getValidatedInput(
                                        "(resume)|(excel)".toRegex(),
                                        "Would you like to resume adding times using regular mode, starting with Time$inputNumber, or continue using excel mode? Enter \"resume\" to resume adding with regular mode, or \"excel\" to continue entering file paths in the aforementioned format: ",
                                        "Invalid input. Please try again: "
                                    )
                                    if (wantsToContinue == "resume") continue@getInput
                                    if (wantsToContinue == "excel") continue@getExcelInput
                                }
                            }
                        }
                    }
                    inputNumber++
                }
                println("Total")
                printTimeConcisely()
            }
            break
        }
    }

    private fun getRegexOrOptions(regex: String) = "($regex)|(total)(done)|(reset)|(main)|(excel)".toRegex()

    fun accumulateTime(hour: Int, minute: Int, second: Int) {
        totalSeconds += toSeconds(hour, minute, second)
        val triple = toHrMinSec(second = totalSeconds)

        _hour = triple.first
        _minute = triple.second
        _second = triple.third

        _previousHour = triple.first
        _previousMinute = triple.second
        _previousSecond = triple.third
    }

    private fun getValidFile(
        inputNumber: Int,
        input: String?
    ): Pair<File, String>? {
        val filename = getValidatedInput(
            "Enter the file path to a .txt file which contains all of the times you would like to add with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\".",
            "Invalid input. Either the file was not found or the command you entered is not recognized. Please try again. To resume regular mode without entering a file path, enter \"resume\" or \"back\": "
        ) { pathname ->
            pathname != null && (File(pathname).exists() || (pathname.toLowerCase() == "example").also {
                createAddTimeExampleFileAndOpenFile()
                println("Enter the file path to a .txt file which contains all of the times you would like to add, with each time on a new line. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
            } || (pathname.toLowerCase() == "resume" || pathname.toLowerCase() == "back").also {
                null
            })
        }
        return filename?.let { File(filename) to filename }
    }

    fun createAddTimeExampleFileAndOpenFile(): String {

        val batchFile = File(System.getProperty("user.dir") + "\\OpenAddTimeExample.bat")
        batchFile.writeText("notepad \'" + System.getProperty("user.dir") + "\\OpenAddTimeExample\'")
        val filepath = System.getProperty("user.dir") + "\\OpenAddTimeExample.txt"
        val textFile = File(filepath)
        textFile.writeText("15\n45\n162\n-115\n89\n6")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.deleteOnExit()
        batchFile.delete()
        return filepath
    }

    fun createAddTimeMultiExampleFileAndOpenFile() {
        val batchFile = File(System.getProperty("user.dir") + "\\OpenAddMultiTimeExample.bat")
        batchFile.writeText("notepad \'" + System.getProperty("user.dir") + "\\OpenAddMultiTimeExample\'")
        val textFile = File(System.getProperty("user.dir") + "\\OpenAddMultiTimeExample.txt")
        textFile.writeText("s15\nm45:-10\n-162:23:-18\nm-115\nh1:2\nh6\nm2:3")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.delete()
        batchFile.delete()
    }

    fun createStartEndTimeExampleFileAndOpenFile() {
        val batchFile = File(System.getProperty("user.dir") + "\\OpenStartEndTimeExample.bat")
        batchFile.writeText("notepad \'" + System.getProperty("user.dir") + "\\OpenStartEndTimeExample\'")
        val textFile = File(System.getProperty("user.dir") + "\\OpenStartEndTimeExample.txt")
        textFile.writeText("12:42 pm\n9:16:45 AM\n8:23 am\n5:48:27 PM\n11:15 AM\n9:21 am\n8:16:21 Am\n9:28:15 pM\n9:28:15 Pm")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.delete()
        batchFile.delete()
    }

    fun toSeconds(hour1: Int, minute1: Int, second1: Int) = (hour1 * 3600) + (minute1 * 60) + second1
    fun toHrMinSec(hour: Int = 0, minute: Int = 0, second: Int = 0): Triple<Int, Int, Int> {
        var minute1 = minute
        var second1 = second
        var hour1 = hour
        minute1 += (second1 / 60)
        hour1 += (minute1 / 60)
        second1 %= 60
        minute1 %= 60
        return Triple(hour1, minute1, second1)
    }

    /*fun parseExcelTimesMulti(times: List<String>): List<Int>? {
        var hour = 0
        var minute = 0
        var second = 0
        for (time1 in times) {
            var time = time1
            if (Pattern.matches(hhCmmCss, time)) { //such as
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    times[0].toInt(),
                    times[1].toInt(),
                    times[2].toInt()
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else if (Pattern.matches(hhhCmm, time)) { //if in h:mm format
                time = time.substring(1)
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    times[0].toInt(),
                    times[1].toInt(),
                    0
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else if (Pattern.matches(mmmCss, time)) { //if in mmm(e.g. m42) format
                time = time.substring(1)
                val times = time.split(":".toRegex()).toTypedArray()
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    0,
                    times[0].toInt(),
                    times[1].toInt()
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else if (Pattern.matches(mmm, time)) { //if in mmm(e.g. m42) format
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    0,
                    time.toInt(),
                    0
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else if (Pattern.matches(hhh, time)) {
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    time.toInt(),
                    0,
                    0
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else if (Pattern.matches(sss, time)) { //if in sss(e.g. s42) format
                time = time.substring(1)
                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    0,
                    0,
                    time.toInt()
                )
                hour = newHour.toInt()
                minute = newMinute.toInt()
                second = newSecond.toInt()
            } else {
                println("The value \"$time1\" at position ${times.indexOf(time1) + 1} was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
                return null
            }
            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                0,
                0,
                0
            )
            hour = newHour.toInt()
            minute = newMinute.toInt()
            second = newSecond.toInt()
        }
        printTimeConcisely()
        println(" added to total")
        return listOf(hour, minute, second)
    }*/

    fun getConsiceTime(hour: Int = _hour, minute: Int = _minute, second: Int = _second): CharSequence {
        val string = StringBuilder()
        if (hour != 0) string.append("$hour hr ") //notice the trailing space
        if (minute != 0) string.append("$minute min ") //^
        if (second != 0) string.append("$second min")
        return string.trimEnd()
    }

    fun printTimeConcisely() = print(getConsiceTime())

    fun getInput() = readLine()?.toLowerCase() ?: ""

    fun Int.to24HourFormat(ampm: String): Int = this + if (ampm == "pm") 12 else 0

    fun formatAddAndReformatTimes(
        inputHour: Int,
        inputMinute: Int,
        inputSecond: Int
    ): Triple<Int, Int, Int> {
        return toHrMinSec(
            second = totalSeconds + toSeconds(
                inputHour,
                inputMinute,
                inputSecond
            )
        ) //previous time + new time
    }


    @Throws(ParseException::class)
    fun startEndMode() {
        var placeHolder3 = true
        var alreadyPrintedMessage = false
        entireProgram@ while (placeHolder3) {

            if (!alreadyPrintedMessage) {
                println("Enter a start time for time A and an end time for time B to add to the total the time elapsed between them. Numbers can be in the format 3:22 PM, 03:22 PM, 3:22 am, 03:22:16 am, 03:22:16 AM, etc. If the start time is later than the end time, (e.g. TimeA: 4:22 PM, TimeB: 3:15 PM), the time elapsed between them will be subtracted from the total. Conversely, they will be added if the order is reversed, ut supra. ")
                alreadyPrintedMessage = true
            } else {
                println();println("Time Reset.");println();println()
            }

            var hourA by Delegates.notNull<Int>()
            var minuteA by Delegates.notNull<Int>()
            var secondA by Delegates.notNull<Int>()
            lateinit var ampmA: String
            var hourB = 0
            var minuteB = 0
            var secondB = 0
            var ampmB = ""
            var i = 1
            var ab: String
            var timeB: String//I do not have to initialize timeA because If I want to break the loop to get input for timeA if the user types "total" the first time around when ampmA/B is not initialized, all i have to do is continue@getInputs to redo inputs so that ampmA is initialized by time I get

            var totalSecondsElapsedBetweenTimes = 0
            var totalHoursElapsedBetweenTimes = 0
            var totalMinutesElapsedBetweenTimes = 0



            getInputs@ while (true) {
                ab = "a"
                print("Enter Time $i$ab: ")
                val errorMessage = "Incorrectly Formatted Number. Try Again."
                val regex = getRegexOrOptions(hhCmmCssAMPM)
                val timeA = getValidatedInput(
                    regex,
                    "Enter Time $i$ab: ",
                    errorMessage
                )!!// getInput()
                when (timeA) {
                    "total" -> print(
                        getConsiceTime(
                            totalHoursElapsedBetweenTimes,
                            totalMinutesElapsedBetweenTimes,
                            totalSecondsElapsedBetweenTimes
                        )
                    )
                    "redo" -> {
                        println()
                        continue@entireProgram
                    }
                    "main" -> {
                        println()
                        throw RedoMainException()
                    }
                    "excel" -> {
                        TODO("unimplemented")
                    }
                    else -> {
                        hourA = timeA.substring(0, timeA.indexOf(":")).toInt()
                        minuteA = timeA.substring(timeA.indexOf(":") + 1, timeA.indexOf(":") + 3).trimEnd().toInt()
                        secondA = if (timeA.split(":").size > 2) Integer.parseInt(timeA.split(":", " ")[2]) else 0
                        ampmA = timeA.substring(indexOfAOrP(timeA)).toLowerCase()
                    }
                }
                ab = "b"
                val timeB = getValidatedInput(
                    regex,
                    "Enter Time $i$ab: ",
                    errorMessage
                )!!

                when (timeB) {
                    "total" -> print(
                        getConsiceTime(
                            totalHoursElapsedBetweenTimes,
                            totalMinutesElapsedBetweenTimes,
                            totalSecondsElapsedBetweenTimes
                        )
                    )
                    "redo" -> {
                        println()
                        continue@entireProgram
                    }
                    "main" -> {
                        println()
                        throw RedoMainException()
                    }
                    "excel" -> {
                        TODO("unimplemented")
                        /*val wantsToContinue = getValidatedInput(
                            "(yes)|(no)".toRegex(),
                            "Proceeding with excel mode now will delete your previous entry for Time${i}a. Would you still like to continue? Enter \"yes\" to continue with excel mode and \"no\" to resume adding in regular mode with Time$i$ab",
                            "Invalid Input. Please Try Again: "
                        )
                        if(wantsToContinue == "yes") {
                            println("First enter the file path to a .txt file which contains all of the start times you would like to add, with each time on a new line. You will soon enter the file path to .txt file which contains all of their respective end times, each on the same line. For example, the program will calculate the time elapsed from the time on the 8th line in the first to the time on the 8th line in the second file, hence making the time on the 8th line in the first file the start time, and the time on the 8th line in the second file the end time. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\": ")
                            var fileA: File
                            var fileB: File
                            getExcelInput@ while (true) {
                                getFilePathA@ while (true) {
                                    val filename =
                                        try {
                                            val pathname = readLine() ?: ""
                                            if (File(pathname).exists()) File(pathname)
                                            else if (pathname.toLowerCase() == "example") {
                                                createStartEndTimeExampleFileAndOpenFile()
                                                println("First enter the file path to a .txt file which contains all of the start times you would like to add, with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\": ")
                                                continue@getFilePathA
                                            } else if (pathname.toLowerCase() == "resume" || pathname.toLowerCase() == "back") {
                                                continue@getInputs
                                            } else {
                                                print("Invalid input. Either the file was not found or the command you entered is not recognized. Please try again. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                                continue@getFilePathA
                                            }
                                        } catch (e: SecurityException) {
                                            print("File access denied. Please either run this program as Administrator or grant read access to the user who ran this program. Once done, please enter the file path again to continue with the calculations: ")
                                            continue@getFilePathA
                                        }
                                    fileA = filename
                                    break@getFilePathA
                                }
                                println("Now enter the file path to a .txt file which contains all of the end times you would like to add, with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\": ")
                                getFilePathB@ while (true) {
                                    val filename =
                                        try {
                                            val pathname = readLine() ?: ""
                                            if (File(pathname).exists()) File(pathname)
                                            else if (pathname.toLowerCase() == "example") {
                                                createStartEndTimeExampleFileAndOpenFile()
                                                println("Now enter the file path to a .txt file which contains all of the end times you would like to add, with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\": ")
                                                continue@getFilePathB
                                            } else if (pathname.toLowerCase() == "resume" || pathname.toLowerCase() == "back") {
                                                continue@getInputs
                                            } else {
                                                print("Invalid input. Either the file was not found or the command you entered is not recognized. Please try again. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                                continue@getFilePathB
                                            }
                                        } catch (e: SecurityException) {
                                            print("File access denied. Please either run this program as Administrator or grant read access to the user who ran this program. Once done, please enter the file path again to continue with the calculations: ")
                                            continue@getFilePathB
                                        }
                                    fileB = filename
                                    break@getFilePathB
                                }
                                readFiles@ while (true) {
                                    val startTimes = fileA.readLines()
                                    val endTimes = fileB.readLines()
                                    if (startTimes.size != endTimes.size) {
                                        if (startTimes.size > endTimes.size) {
                                            print("There are more start times than there are end times. Please make sure that all of the start times line up with their proper end times and that you have not skipped or forgotten any end times. Would you like to retry the calculation once you've corrected the files? If so, enter \"retry\". Otherwise, enter \"redo\" to be redirected to enter both file paths once more, starting with the .txt file containing the start times: ")
                                            getAnswer@ while (true) {
                                                val answer = getInput()
                                                if (answer == "redo") continue@getExcelInput
                                                else if (answer == "retry") continue@readFiles
                                                else {
                                                    print("Invalid input. Please try again: ")
                                                    continue@getAnswer
                                                }
                                            }
                                        } else {
                                            println("There are more end times than there are start times. Please make sure that all of the start times line up with their proper end times and that you have not skipped or forgotten any start times. Would you like to retry the calculation once you've corrected the files? If so, enter \"retry\". Otherwise, enter \"redo\" to be redirected to enter both file paths once more, starting with the .txt file containing the start times: ")
                                            getAnswer@ while (true) {
                                                val answer = getInput()
                                                if (answer == "redo") continue@getExcelInput
                                                else if (answer == "retry") continue@readFiles
                                                else {
                                                    print("Invalid input. Please try again: ")
                                                    continue@getAnswer
                                                }
                                            }
                                        }
                                    }
                                    val listOfHoursMinutesSecondsFromExcelCalculation = parseExcelTimesStartEnd(
                                        startTimes,
                                        endTimes,
                                        fileA.absolutePath,
                                        fileB.absolutePath
                                    )
                                    if (listOfHoursMinutesSecondsFromExcelCalculation == null) {
                                        if (wasThereAProblemWithTimeAOrTimeB == "a") {
                                            TODO("move the ending brackets of getFilePathA@ and getFilePathB@ to at least here so that i can write: if(problem was with file b) ask for file b again, else ask for file a. Summarized by the function: checkWhichFileContainedAnErrorAndRequestNewInputs()")
                                        }
                                    } else {
                                        totalHoursElapsedBetweenTimes += listOfHoursMinutesSecondsFromExcelCalculation[0]
                                        totalMinutesElapsedBetweenTimes += listOfHoursMinutesSecondsFromExcelCalculation[1]
                                        totalSecondsElapsedBetweenTimes += listOfHoursMinutesSecondsFromExcelCalculation[2]
                                        println("Would you like to resume adding times using regular mode, starting with Time${i}a, or continue using excel mode? Enter \"resume\" to resume adding with regular mode, or \"excel\" to continue entering file paths in the aforementioned format: ")
                                        getAnswer@ while (true) {
                                            val answer = getInput()
                                            if (answer == "resume") {
                                                continue@getInputs
                                            } else if (answer == "excel") {
                                                println("Enter the file path to a .txt file which contains all of the times you would like to add with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\".")
                                                continue@getExcelInput
                                            } else {
                                                print("Invalid input. Please try again: ")
                                                continue@getAnswer
                                            }
                                        }
                                    }
                                }
                            }
                        }*/
                    }
                    else -> {
                        hourB = timeB.substring(0, timeB.indexOf(":")).toInt()
                        minuteB = timeB.substring(timeB.indexOf(":") + 1, timeB.indexOf(":") + 3).trimEnd().toInt()
                        secondB = if (timeB.split(":").size > 2) Integer.parseInt(timeB.split(":", " ")[2]) else 0
                        ampmB = timeB.substring(indexOfAOrP(timeB)).toLowerCase()
                    }
                }

                //convert AM-PM to 24-hour format
                hourA = hourA.to24HourFormat(ampmA)
                hourB = hourB.to24HourFormat(ampmB)

                //Convert hh:mm:ss to seconds
                val totalSecondsA = toSeconds(hourA, minuteA, secondA)
                val totalSecondsB = toSeconds(hourB, minuteB, secondB)
                val secondsElapsedBetweenTimes = totalSecondsB - totalSecondsA

                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    0,
                    0,
                    secondsElapsedBetweenTimes
                )
                totalHoursElapsedBetweenTimes = newHour
                totalMinutesElapsedBetweenTimes = newMinute
                totalSecondsElapsedBetweenTimes = newSecond
                i++
            }
        }
    }

    class RedoMainException : Exception()

    fun parseExcelTimes(times: List<String>, regex: String, mode: Int = 0): List<Int>? {
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
                        times[0].toInt(),
                        times[1].toInt(),
                        times[2].toInt()
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == hhCmm && mode == 4) { //if in h:mm format
                    //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds. I am assuming it is 0
                    val times = time.split(":".toRegex()).toTypedArray()
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        times[0].toInt(),
                        times[1].toInt(),
                        0
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == mmCss && mode == 3) { //if in mm:ss format
                    val times = time.split(":".toRegex()).toTypedArray()
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        0,
                        times[0].toInt(),
                        times[1].toInt()
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == mmm) { //if in mmm(e.g. m42) format
                    //TODO This code will not work if the user inputs a decimal value for minutes. Because, 4.5 minutes is 4 min 30 sec, and i do not update the value for seconds
                    time = time.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        0,
                        time.toInt(),
                        0
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == hhh) {
                    time = time.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        time.toInt(),
                        0,
                        0
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == sss) {
                    time = time.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        0,
                        0,
                        time.toInt()
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == mm && mode == 2) {
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        0,
                        time.toInt(),
                        0
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                } else if (regex == ss && mode == 1) {
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        0,
                        0,
                        time.toInt()
                    )
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                }
            } else {
                println("The value \"$time1\" at position ${times.indexOf(time1) + 1} was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
                return null
            }
            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                0,
                0,
                0
            )
            hour = newHour.toInt()
            minute = newMinute.toInt()
            second = newSecond.toInt()
        }
        printTimeConcisely()
        println(" added to total")
        return listOf(hour, minute, second)
    }

    fun parseExcelTimesStartEnd(
        startTimes: List<String>,
        endTimes: List<String>,
        startPath: String,
        endPath: String
    ): List<Int>? {


        require(startTimes.size == endTimes.size) { "There are either more start times than end times or more end times than start times. There must be a one-to-one ratio" }


        var hourA: Int
        var minuteA: Int
        var secondA: Int
        var ampmA: String
        var hourB = 0
        var minuteB = 0
        var secondB = 0
        var ampmB = ""
        var timeA: String
        var timeB: String
        var (totalHoursElapsedBetweenTimes, totalMinutesElapsedBetweenTimes, totalSecondsElapsedBetweenTimes) = listOf(
            0,
            0,
            0
        )
        val startMappedToEnd = startTimes.zip(endTimes).toMap()


        startMappedToEnd.forEach { (first, second) ->
            timeA = first
            timeB = second

            if (Pattern.matches(hhCmmCssAMPM, timeA)) {
                hourA = timeA.substring(0, timeA.indexOf(":")).toInt()
                minuteA = timeA.substring(timeA.indexOf(":") + 1, timeA.indexOf(":") + 3).trimEnd().toInt()
                secondA = if (timeA.split(":").size > 2) Integer.parseInt(timeA.split(":", " ")[2]) else 0
                ampmA = timeA.substring(indexOfAOrP(timeA)).toLowerCase()
            } else {
                println("The start time \"$timeA\" at position ${startTimes.indexOf(timeA) + 1} in the file $startPath  was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
                wasThereAProblemWithTimeAOrTimeB = "a"
                return null
            }

            if (Pattern.matches(hhCmmCssAMPM, timeB)) {
                hourB = timeB.substring(0, timeB.indexOf(":")).toInt()
                minuteB = timeB.substring(timeB.indexOf(":") + 1, timeB.indexOf(":") + 3).trimEnd().toInt()
                secondB = if (timeB.split(":").size > 2) Integer.parseInt(timeB.split(":", " ")[2]) else 0
                ampmB = timeB.substring(indexOfAOrP(timeB)).toLowerCase()
            } else {
                println("The start time \"$timeB\" at position ${endTimes.indexOf(timeB) + 1} in the file $endPath was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
                wasThereAProblemWithTimeAOrTimeB = "b"
                return null
            }

            //convert AM-PM to 24-hour format
            hourA = hourA.to24HourFormat(ampmA)
            hourB = hourB.to24HourFormat(ampmB)

            //Convert hh:mm:ss to seconds
            val totalSecondsA = toSeconds(hourA, minuteA, secondA)
            val totalSecondsB = toSeconds(hourB, minuteB, secondB)
            val secondsElapsedBetweenTimes = totalSecondsB - totalSecondsA

            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                0,
                0,
                secondsElapsedBetweenTimes
            )
            totalHoursElapsedBetweenTimes = newHour.toInt()
            totalMinutesElapsedBetweenTimes = newMinute.toInt()
            totalSecondsElapsedBetweenTimes = newSecond.toInt()
        }

        print(
            getConsiceTime(
                totalHoursElapsedBetweenTimes,
                totalMinutesElapsedBetweenTimes,
                totalSecondsElapsedBetweenTimes
            )
        )
        println(" added to total")
        return listOf(totalHoursElapsedBetweenTimes, totalMinutesElapsedBetweenTimes, totalSecondsElapsedBetweenTimes)
    }
}