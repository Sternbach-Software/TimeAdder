
import java.io.File
import java.text.ParseException
import java.util.regex.Pattern

object MainV1FullyFunctional_UsesInts {
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
            //TODO add error message in case one of the files has a time which doesnt have a counterpart, e.g. there is a time in the start times file which does not have a counterpart in the end times file
        //TODO add ability to exit out of excel mode before entering the filepath in case the user cannot get the filepath function working or correctly grant access to the file in case it is locked

    var shouldIFinish=false//for some reason, when timeAddMulti would finish and go to main menu, it would loop asking the user which mode they wanted, so I introcuced a check before when(getInput()) to this variable to stop the loop if i came from Multi where I set this to true
    @Throws(ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var placeHolder = true
        mainMenu@ while (placeHolder) {
            try {
                println("Would you like to use the Second Mode(1), Minute Mode(2), Minute/Second Mode(3), Hour/Minute Mode(4),\nHour/Minute/Second Mode(5), Multi Mode(6), or Start/End Mode(7)? Once started, type \"done\" to see the final result and exit, \"total\" to see the current total, \"redo\" to reset the running total to 0 and restart the current mode from Time1, \"excel\" to enter a list of times to add (or for Start/End Mode, start times and end times to add together,) or \"main\" to go back to the main menu(doing so will reset the running total to 0 without first displaying the running total):")
                requestInput@ while (true) {
                    if(shouldIFinish) break@mainMenu
                    when (getInput()) {
                        "1" -> timeAdd(
                            ss,
                            1,
                            "Enter numbers in the following format: \"116\" (i.e. 16 sec) or \"-116\" to add or subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                        )
                        "2" -> timeAdd(
                            mm,
                            2,
                            "Enter numbers in the following format: \"222\" (i.e. 222 min) or \"-222\" to add or subtract 222 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                        )
                        "3" -> timeAdd(
                            mmCss,
                            3,
                            "Enter numbers in the following format: \"222:16\" (i.e. 222 min 16 sec) or \"222:-16\", etc. to add 222 minutes and add/subtract 16 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                        )
                        "4" -> timeAdd(
                            hhCmm,
                            4,
                            "Enter numbers in the following format: \"115:22\" (i.e. 115 hr 22 min) or \"115:-22\", etc. to add 115 hours and add/subtract 22 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                        )
                        "5" -> timeAdd(
                            hhCmmCss,
                            5,
                            "Enter numbers in the following format: \"115:22:116\" (i.e. 115 hr 22 min 116 sec) or \"-115:22:-116\", etc. to add/subtract 115 hours, add 22 minutes, and add/subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds."
                        )
                        "6" -> timeAddMulti()
                        "7" -> startEndMode()
                        else-> {
                            print("Invalid Input. Please Try Again: ")
                            continue@requestInput
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is RedoMainException) {
                    continue@mainMenu
                } else e.printStackTrace()
            }
            placeHolder = false
        }
    }
    fun timeAdd(regex: String, mode: Int, message: String) {
        var placeHolder1 = true
        var alreadyPrintedMessage=false
        entireProgram@ while(placeHolder1) {
            var inputNumber = 1
            var hour = 0
            var minute = 0
            var second = 0
            if(!alreadyPrintedMessage) {
                println(message /*"Enter numbers in any of the following formats:  15:22 (i.e. 15 hr 22 min), 4:22 (4 hr 22 min), m42 (42 min), or h12 (12 hr)"*/)
                alreadyPrintedMessage=true
            }
            else {println();println("Time Reset.");println();println()}

            print("Time$inputNumber: ")
            var input = getInput()

            //Collect input times:
            mainInputs@while (true) {
                if (Pattern.matches(regex, input)) {
                    //tree of destructuring decision based on time format:
                    if (regex == hhCmmCss) { //if in hh:mm:ss format
                        val times = input.split(":".toRegex()).toTypedArray()
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
                        val times = input.split(":".toRegex()).toTypedArray()
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
                        val times = input.split(":".toRegex()).toTypedArray()
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
                        input = input.substring(1)
                        val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                            hour,
                            minute,
                            second,
                            0,
                            input.toInt(),
                            0
                        ).split(",")
                        hour = newHour.toInt()
                        minute = newMinute.toInt()
                        second = newSecond.toInt()
                    }
                    else if (regex == hhh) {
                        input = input.substring(1)
                        val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                            hour,
                            minute,
                            second,
                            input.toInt(),
                            0,
                            0
                        ).split(",")
                        hour = newHour.toInt()
                        minute = newMinute.toInt()
                        second = newSecond.toInt()
                    }
                    else if (regex == sss) {
                        input = input.substring(1)
                        val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                            hour,
                            minute,
                            second,
                            0,
                            0,
                            input.toInt()
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
                            input.toInt(),
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
                            input.toInt()
                        ).split(",")
                        hour = newHour.toInt()
                        minute = newMinute.toInt()
                        second = newSecond.toInt()
                    }
                } else {
                    if (!(input == "total" || input == "done" || input == "redo" || input == "main" || input == "excel")) {
                        println("Incorrectly Formatted Number. Try Again.")
                        print("Time$inputNumber: ")
                        input = getInput()
                        continue
                    } else if (input == "done") break
                    else if (input == "total") {
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
                        printTimeConcisely(hour, minute, second)
                        println()
                        print("Time$inputNumber: ")
                        input = getInput()
                        continue
                    } else if (input == "redo") {
                        println()
                        continue@entireProgram
                    } else if (input == "main") {
                        println()
                        throw RedoMainException()
                    } else if (input == "excel") {
                        println("Enter the file path to a .txt file with all of the times you would like to add with each time on a new line. To see an example file, type \"example\". To resume regular mode without entering a file path, enter \"resume\".")
                        var file: File
                        getExcelInput@ while (true) {
                            getFilePath@ while (true) {
                                val filename =
                                    try {
                                        val pathname = readLine() ?: ""
                                        if (File(pathname).exists()) File(pathname)
                                        else if (pathname.toLowerCase() == "example") {
                                            createAddTimeExampleFileAndOpenFile()
                                            println("Enter the file path to a .txt file which contains all of the times you would like to add, with each time on a new line. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                            continue@getFilePath
                                        }
                                        else if(pathname.toLowerCase() == "resume"|| pathname.toLowerCase() == "back") {
                                            print("Time$inputNumber: ")
                                            input = getInput()
                                            continue@mainInputs
                                        }
                                        else {
                                            print("Invalid input. Either the file was not found or the command you entered is not recognized. Please try again. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                            continue@getFilePath
                                        }
                                    } catch (e: SecurityException) {
                                        print("File access denied. Please either run this program as Administrator or grant read access to the user who ran this program. Once done, please enter the file path again to continue with the calculations: ")
                                        continue@getFilePath
                                    }
                                file = filename
                                break@getFilePath
                            }
                            val times = file.readLines()
                            val listofHourMinuteAndSecondTotalfromfile =
                                parseExcelTimes(times, regex, mode) ?: continue@getExcelInput
                            hour += listofHourMinuteAndSecondTotalfromfile[0]
                            minute += listofHourMinuteAndSecondTotalfromfile[1]
                            second += listofHourMinuteAndSecondTotalfromfile[2]
                            println("Would you like to resume adding times using regular mode, starting with Time$inputNumber, or continue using excel mode? Enter \"resume\" to resume counting with regular mode, or \"excel\" to continue entering file paths in the aforementioned format: ")
                            getAnswer@while(true) {
                                val answer = getInput()
                                if (answer == "resume") {
                                    print("Time$inputNumber: ")
                                    input = getInput()
                                    continue@mainInputs
                                }
                                else if (answer == "excel") {
                                    println("Enter the file path to a .txt file with all of the times you would like to add with each time on a new line. To see an example file, type \"example\".")
                                    continue@getExcelInput
                                } else {
                                    print("Invalid input. Please try again: ")
                                    continue@getAnswer
                                }
                            }
                        }
                    }
                }
                inputNumber += 1
                print("Time$inputNumber: ")
                input = getInput()
            }
            println()
            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(hour, minute, second, 0, 0, 0).split(",")
            hour = newHour.toInt()
            minute = newMinute.toInt()
            second = newSecond.toInt()
            printTimeConcisely(hour, minute, second)
            println()

            placeHolder1=false
        }
    }
    fun createAddTimeExampleFileAndOpenFile() {

        val batchFile = File(System.getProperty("user.dir")+"\\OpenAddTimeExample.bat")
        batchFile.writeText("notepad \'"+ System.getProperty("user.dir")+"\\OpenAddTimeExample\'")
        val textFile=File(System.getProperty("user.dir")+"\\OpenAddTimeExample.txt")
        textFile.writeText("15\n45\n162\n-115\n89\n6")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.delete()
        batchFile.delete()
    }
    fun createAddTimeMultiExampleFileAndOpenFile() {
        val batchFile = File(System.getProperty("user.dir")+"\\OpenAddMultiTimeExample.bat")
        batchFile.writeText("notepad \'"+ System.getProperty("user.dir")+"\\OpenAddMultiTimeExample\'")
        val textFile=File(System.getProperty("user.dir")+"\\OpenAddMultiTimeExample.txt")
        textFile.writeText("s15\nm45:-10\n-162:23:-18\nm-115\nh1:2\nh6\nm2:3")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.delete()
        batchFile.delete()
    }
    fun createStartEndTimeExampleFileAndOpenFile() {
        val batchFile = File(System.getProperty("user.dir")+"\\OpenStartEndTimeExample.bat")
        batchFile.writeText("notepad \'"+ System.getProperty("user.dir")+"\\OpenStartEndTimeExample\'")
        val textFile=File(System.getProperty("user.dir")+"\\OpenStartEndTimeExample.txt")
        textFile.writeText("12:42 pm\n9:16:45 AM\n8:23 am\n5:48:27 PM\n11:15 AM\n9:21 am\n8:16:21 Am\n9:28:15 pM\n9:28:15 Pm")//Create example file

        val run = Runtime.getRuntime()
        run.exec(batchFile.absolutePath)
        Thread.sleep(2000L)
        textFile.delete()
        batchFile.delete()
    }
    fun toSeconds(hour1: Int, minute1: Int, second1: Int) =  (hour1 * 3600) + (minute1 * 60) + second1
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
    fun timeAddMulti() {
        var placeHolder2 = true
        var alreadyPrintedMessage = false
        entireProgram@ while (placeHolder2) {
            var inputNumber = 1
            if(!alreadyPrintedMessage) {
                println("Enter numbers in any of the following formats:  15:22:31 (15 hr 22 min 31 sec), h15:22 (15 hr 22 min), h1:2 (1 hr 2 min), m22:31 (22 min 31 sec), m2:3 (2 min 3 sec), h15 (15 hr), h1 (1 hr), m22 (22 min), m2 (2 min), s31 (31 sec), or s3 (3 sec). Add a minus before any number, e.g. h-12:-14 to subtract that number from the total. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.")
                alreadyPrintedMessage=true
            }
            else {println();println("Time Reset.");println();println()}

            print("Time$inputNumber: ")
            var input = getInput()
            var hour = 0
            var minute = 0
            var second = 0
            //Collect input times:
            mainInputs@while (true) {
                if (Pattern.matches(hhCmmCss, input)) { //such as
                    val times = input.split(":".toRegex()).toTypedArray()
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
                else if (Pattern.matches(hhhCmm, input)) { //if in h:mm format
                    input = input.substring(1)
                    val times = input.split(":".toRegex()).toTypedArray()
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
                else if (Pattern.matches(mmmCss, input)) { //if in mmm(e.g. m42) format
                    input = input.substring(1)
                    val times = input.split(":".toRegex()).toTypedArray()
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
                else if (Pattern.matches(mmm, input)) { //if in mmm(e.g. m42) format
                    input = input.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        hour,
                        minute,
                        second,
                        0,
                        input.toInt(),
                        0
                    ).split(",")
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                }
                else if (Pattern.matches(hhh, input)) {
                    input = input.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        hour,
                        minute,
                        second,
                        input.toInt(),
                        0,
                        0
                    ).split(",")
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                }
                else if (Pattern.matches(sss, input)) { //if in sss(e.g. s42) format
                    input = input.substring(1)
                    val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                        hour,
                        minute,
                        second,
                        0,
                        0,
                        input.toInt()
                    ).split(",")
                    hour = newHour.toInt()
                    minute = newMinute.toInt()
                    second = newSecond.toInt()
                }
                else {
                    if (!(input == "total" || input == "done" || input == "redo" || input == "main" || input == "excel")) {
                        println("Incorrectly Formatted Number. Try Again.")
                        print("Time$inputNumber: ")
                        input = getInput()
                        continue
                    } else if (input == "done") {shouldIFinish=true;break}
                    else if (input == "total") {
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
                        printTimeConcisely(hour, minute, second)
                        println()
                        print("Time$inputNumber: ")
                        input = getInput()
                        continue
                    }
                    else if(input=="redo"){
                        println()
                        continue@entireProgram
                    }
                    else if (input=="main") {
                        println()
                        throw RedoMainException()
                    }
                    else if (input=="excel") {
                        println("Enter the file path to a .txt file with all of the times you would like to add with each time on a new line. To see an example file, type \"example\".")
                        var file: File
                        getExcelInput@ while (true) {
                            getFilePath@ while (true) {
                                val filename =
                                    try {
                                        val pathname = readLine() ?: ""
                                        if (File(pathname).exists()) File(pathname)
                                        else if (pathname.toLowerCase() == "example") {
                                            createAddTimeMultiExampleFileAndOpenFile()
                                            println("Enter the file path to a .txt file which contains all of the times you would like to add, with each time on a new line. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                            continue@getFilePath
                                        } else if(pathname.toLowerCase() == "resume"|| pathname.toLowerCase() == "back") {
                                                print("Time$inputNumber: ")
                                                input = getInput()
                                                continue@mainInputs
                                        } else {
                                            print("Invalid input. Either the file was not found or the command you entered is not recognized. Please try again. To resume regular mode without entering a file path, enter \"resume\" or \"back\": ")
                                            continue@getFilePath
                                        }
                                    } catch (e: SecurityException) {
                                        print("File access denied. Please either run this program as Administrator or grant read access to the user who ran this program. Once done, please enter the file path again to continue with the calculations: ")
                                        continue@getFilePath
                                    }
                                file = filename
                                break@getFilePath
                            }
                            val times = file.readLines()
                            val listofHourMinuteAndSecondTotalfromfile = parseExcelTimesMulti(times) ?: continue@getExcelInput
                            hour += listofHourMinuteAndSecondTotalfromfile[0]
                            minute += listofHourMinuteAndSecondTotalfromfile[1]
                            second += listofHourMinuteAndSecondTotalfromfile[2]
                            println("Would you like to resume adding times using regular mode, starting with Time$inputNumber, or continue using excel mode? Enter \"resume\" to resume counting with regular mode, or \"excel\" to continue entering file paths in the aforementioned format: ")
                            getAnswer@while(true) {
                                val answer = getInput()
                                if (answer == "resume") {
                                    print("Time$inputNumber: ")
                                    input = getInput()
                                    continue@mainInputs
                                }
                                else if (answer == "excel") {
                                    println("Enter the file path to a .txt file with all of the times you would like to add with each time on a new line. To see an example file, type \"example\".")
                                    continue@getExcelInput
                                } else {
                                    print("Invalid input. Please try again: ")
                                    continue@getAnswer
                                }
                            }
                        }
                    }
                }
                inputNumber += 1
                print("Time$inputNumber: ")
                input = getInput()
            }
            println()
            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(hour, minute, second, 0, 0, 0).split(",")
            hour = newHour.toInt()
            minute = newMinute.toInt()
            second = newSecond.toInt()

            printTimeConcisely(hour, minute, second)
            println()

            placeHolder2=false
        }
    }

    fun parseExcelTimesMulti(times: List<String>): List<Int>? {
        var hour = 0
        var minute = 0
        var second = 0
        for (time1 in times) {
            var time = time1
            if (Pattern.matches(hhCmmCss, time)) { //such as
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
            else if (Pattern.matches(hhhCmm, time)) { //if in h:mm format
                time = time.substring(1)
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
            else if (Pattern.matches(mmmCss, time)) { //if in mmm(e.g. m42) format
                time = time.substring(1)
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
            else if (Pattern.matches(mmm, time)) { //if in mmm(e.g. m42) format
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
            else if (Pattern.matches(hhh, time)) {
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
            else if (Pattern.matches(sss, time)) { //if in sss(e.g. s42) format
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
            else {
                println("The value \"$time1\" at position ${times.indexOf(time1)+1} was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
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
        printTimeConcisely(hour,minute,second)
        println(" added to total")
        return listOf(hour, minute, second)
    }


    fun printTimeConcisely(hour: Int, minute: Int, second: Int) {
        when {
            (second == 0) and (hour > 0) and (minute == 0) -> print("$hour hr")
            (second > 0) and (hour == 0) and (minute == 0) -> print("$second sec")
            (second == 0) and (hour == 0) and (minute > 0) -> print("$minute min")
            (second > 0) and (hour > 0) and (minute == 0) -> print("$hour hr $second sec")
            (second == 0) and (hour > 0) and (minute > 0) -> print("$hour hr $minute min")
            (second > 0) and (hour == 0) and (minute > 0) -> print("$minute min $second sec")
            (second > 0) and (hour > 0) and (minute > 0) -> print("$hour hr $minute min $second sec")
            (second == 0) and (hour == 0) and (minute == 0) -> print("$hour hr $minute min $second sec")
            else-> print("$hour hr $minute min $second sec")
        }
    }
    fun getInput() = readLine()?.toLowerCase() ?: ""
    fun Int.to24HourFormat(ampm: String): Int {
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
    fun formatAddAndReformatTimes(hour:Int,minute:Int,second:Int,inputHour:Int,inputMinute:Int,inputSecond:Int): String {
        var hour1=hour
        var minute1=minute
        var second1=second
        var previousTimeInSeconds=toSeconds(hour1, minute1, second1)
        val inputInSeconds=toSeconds(inputHour, inputMinute, inputSecond)
        previousTimeInSeconds+=inputInSeconds
        val newTimes=toHrMinSec(second=previousTimeInSeconds)
        hour1=newTimes.first
        minute1=newTimes.second
        second1=newTimes.third
        return "$hour1,$minute1,$second1"
    }
    @Throws(ParseException::class)
    fun startEndMode() {
        var placeHolder3 = true
        var alreadyPrintedMessage = false
        entireProgram@ while (placeHolder3) {
            if (!alreadyPrintedMessage) {
                println("Enter a start time for time A and an end time for time B to add to the total the time elapsed between them. Numbers can be in the format 3:22 PM, 03:22 PM, 3:22 am, 03:22:16 am, 03:22:16 AM, etc. If the start time is later than the end time, (e.g. TimeA: 4:22 PM, TimeB: 3:15 PM), the time elapsed between them will be subtracted from the total. Conversely, they will be added if the order is reversed, ut supra. ")
                alreadyPrintedMessage=true
            }
            else {println();println("Time Reset.");println();println()}

            var hourA: Int
            var minuteA: Int
            var secondA: Int
            var ampmA: String
            var hourB = 0
            var minuteB = 0
            var secondB = 0
            var ampmB = ""
            var i = 1
            var ab: String
            var timeB: String//I do not have to initialize timeA because If I want to break the loop to get input for timeA if the user types "total" the first time around when ampmA/B is not initialized, all i have to do is continue@getInputs to redo inputs so that ampmA is initialized by time I get
            var (totalSecondsElapsedBetweenTimes, totalHoursElapsedBetweenTimes, totalMinutesElapsedBetweenTimes) = listOf(
                0,
                0,
                0
            )

            getInputs@ while (true) {
                ab = "a"
                print("Enter Time $i$ab: ")
                var timeA = getInput()
                timeA@ while (true) {
                    if (Pattern.matches(hhCmmCssAMPM, timeA)) {
                        hourA = timeA.substring(0, timeA.indexOf(":")).toInt()
                        minuteA = timeA.substring(timeA.indexOf(":") + 1, timeA.indexOf(":") + 3).trimEnd().toInt()
                        secondA = if (timeA.split(":").size > 2) Integer.parseInt(timeA.split(":", " ")[2]) else 0
                        ampmA = timeA.substring(indexOfAOrP(timeA)).toLowerCase()
                        break@timeA
                    } else {
                        if (!(timeA == "total" || timeA == "done" || timeA == "redo" || timeA == "main" || timeA == "excel")) {
                            println("Incorrectly Formatted Number. Try Again.")
                            print("Enter Time $i$ab: ")
                            timeA = getInput()
                            continue@timeA
                        } else if (timeA == "done") break@getInputs
                        else if (timeA == "total") {
                            if (i == 1) {
                                println("No time calculated yet. No total to display.");continue@getInputs
                            }
                            val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                                totalHoursElapsedBetweenTimes,
                                totalMinutesElapsedBetweenTimes,
                                totalSecondsElapsedBetweenTimes,
                                0,
                                0,
                                0
                            ).split(",")
                            totalHoursElapsedBetweenTimes = newHour.toInt()
                            totalMinutesElapsedBetweenTimes = newMinute.toInt()
                            totalSecondsElapsedBetweenTimes = newSecond.toInt()
                            printTimeConcisely(
                                totalHoursElapsedBetweenTimes,
                                totalMinutesElapsedBetweenTimes,
                                totalSecondsElapsedBetweenTimes
                            )
                            println()
                            print("Enter Time $i$ab: ")
                            timeA = getInput()
                            continue@timeA
                        }
                        else if (timeA == "redo"){
                            println()
                            continue@entireProgram
                        }
                        else if (timeA == "main") {
                            println()
                            throw RedoMainException()
                        }
                        else if (timeA=="excel") {
                            TODO("unimplemented")
                        }
                    }
                }
                var x = false
                inputTimeB@ while (!x) {
                    ab = "b"
                    print("Enter Time $i$ab: ")
                    timeB = getInput()

                    timeB@ while (true) {
                        if (Pattern.matches(hhCmmCssAMPM, timeB)) {
                            hourB = timeB.substring(0, timeB.indexOf(":")).toInt()
                            minuteB = timeB.substring(timeB.indexOf(":") + 1, timeB.indexOf(":") + 3).trimEnd().toInt()
                            secondB = if (timeB.split(":").size > 2) Integer.parseInt(timeB.split(":", " ")[2]) else 0
                            ampmB = timeB.substring(indexOfAOrP(timeB)).toLowerCase()
                            break@timeB
                        } else {
                            if (!(timeB == "total" || timeB == "done" || timeB == "redo" || timeB == "main" || timeB == "excel")) {
                                println("Incorrectly Formatted Number. Try Again.")
                                print("Enter Time $i$ab: ")
                                timeB = getInput()
                                continue@timeB
                            } else if (timeB == "done") break@getInputs
                            else if (timeB == "total") {
                                if (i == 1) {
                                    println("No time calculated yet. No total to display.");continue@inputTimeB
                                }
                                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                                    totalHoursElapsedBetweenTimes,
                                    totalMinutesElapsedBetweenTimes,
                                    totalSecondsElapsedBetweenTimes,
                                    0,
                                    0,
                                    0
                                ).split(",")
                                totalHoursElapsedBetweenTimes = newHour.toInt()
                                totalMinutesElapsedBetweenTimes = newMinute.toInt()
                                totalSecondsElapsedBetweenTimes = newSecond.toInt()
                                printTimeConcisely(
                                    totalHoursElapsedBetweenTimes,
                                    totalMinutesElapsedBetweenTimes,
                                    totalSecondsElapsedBetweenTimes
                                )
                                println()
                                print("Enter Time $i$ab: ")
                                timeB = getInput()
                                continue@timeB
                            }
                            else if (timeB == "redo"){
                                println()
                                continue@entireProgram
                            }
                            else if (timeB == "main") {
                                println()
                                throw RedoMainException()
                            }
                            else if (timeB=="excel") {
                                println()
                                TODO("unimplemented")
                            }
                        }
                    }
                    x = true
                }

                //convert AM-PM to 24-hour format
                hourA = hourA.to24HourFormat(ampmA)
                hourB = hourB.to24HourFormat(ampmB)

                //Convert hh:mm:ss to seconds
                val totalSecondsA = toSeconds(hourA, minuteA, secondA)
                val totalSecondsB = toSeconds(hourB, minuteB, secondB)
                val secondsElapsedBetweenTimes = totalSecondsB - totalSecondsA

                val (newHour, newMinute, newSecond) = formatAddAndReformatTimes(
                    totalHoursElapsedBetweenTimes,
                    totalMinutesElapsedBetweenTimes,
                    totalSecondsElapsedBetweenTimes,
                    0,
                    0,
                    secondsElapsedBetweenTimes
                ).split(",")
                totalHoursElapsedBetweenTimes = newHour.toInt()
                totalMinutesElapsedBetweenTimes = newMinute.toInt()
                totalSecondsElapsedBetweenTimes = newSecond.toInt()
                i++
            }
            println()
            printTimeConcisely(
                totalHoursElapsedBetweenTimes,
                totalMinutesElapsedBetweenTimes,
                totalSecondsElapsedBetweenTimes
            )
        placeHolder3=false
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
                println("The value \"$time1\" at position ${times.indexOf(time1)+1} was incorrectly formatted. Please correct the value and enter the file path again (to see an example file enter \"example\"): ")
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
        printTimeConcisely(hour,minute,second)
        println(" added to total")
        return listOf(hour, minute, second)
    }
}