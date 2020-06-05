import java.lang.Integer.parseInt
import java.lang.Long.parseLong

object StartEndModeV1 {
    fun getInput() = readLine()

    @JvmStatic
    fun main(args: Array<String>) {
        /*   String time1=getInput();
    String time2=getInput();*/
        val test1a = "11:30 AM"
        val test1b = "4:30 PM"
        val test1c = "4:31 PM"
//        val test2a = "4:30PM" //will deal with later
//        val test2b = "4:31PM" //will deal with later
        val test3a = "4:30:15 PM"
        val test3b = "4:30:30 PM"
        val test3c = "4:31:00 PM"
        val test4 = "1:25:20 PM"
        val test5 = "2:12:15 PM"

        println("Please only insert times in ascending order. That is, Time b should be later than Time a. ")
        println("Tests:")
        println("Should be 5 hr 0 min 0 sec:  \"${startAndEndMode(test1a, test1b)}\"")
        println("Should be 0 hr 1 min 0 sec:  \"${startAndEndMode(test1b, test1c)}\"")
//        println("Should be 0 hr 0 min 0 sec:  \"${startAndEndMode(test1b, test2a)}\"")
//        println("Should be 0 hr 1 min 0 sec:  \"${startAndEndMode(test2a, test2b)}\"")
        println("Should be 0 hr 0 min 15 sec:  \"${startAndEndMode(test3a, test3b)}\"")
        println("Should be 0 hr 0 min 15 sec:  \"${startAndEndMode(test3a, test3b)}\"")
        println("Should be 0 hr 0 min 45 sec:  \"${startAndEndMode(test3a, test3c)}\"")
        println("Should be ? hr ? min ? sec:  \"${startAndEndMode(test4, test5)}\"")

    }

    private fun indexOfAOrP(time1: String) =
        if (time1.contains("a", ignoreCase = true)) time1.indexOf("a", ignoreCase = true) else time1.indexOf(
            "p",
            ignoreCase = true
        )

    private fun startAndEndMode(time1: String, time2: String): String {

        //replace in a minuteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        var minute1: Long = time1.substring(time1.indexOf(":") + 1, time1.indexOf(":") + 3).trimEnd().toLong()
        var minute2: Long = time2.substring(time2.indexOf(":") + 1, time2.indexOf(":") + 3).trimEnd().toLong()

        val second1: Long = if (time1.split(":").size > 2) parseLong(time1.split(":", " ")[2]) else 0
        val second2: Long = if (time2.split(":").size > 2) parseLong(time2.split(":", " ")[2]) else 0

        var hour1 = time1.substring(0, time1.indexOf(":")).toLong()
        var hour2 = time2.substring(0, time2.indexOf(":")).toLong()


        val ampm1 = time1.substring(indexOfAOrP(time1)).toLowerCase()
        val ampm2 = time2.substring(indexOfAOrP(time2)).toLowerCase()


        var secondsElapsedBetweenTimes: Long

        //convert AM-PM to 24-hour format
        hour1 = hour1.to24HourFormat(ampm1)
        hour2 = hour2.to24HourFormat(ampm2)

        //13:25:20
        //14:12:15

        //Convert hh:mm:ss to seconds
        val totalSeconds1 = (hour1 * 3600) + (minute1 * 60) + second1
        val totalSeconds2 = (hour2 * 3600) + (minute2 * 60) + second2
        println(totalSeconds1)
        println(totalSeconds2)
        secondsElapsedBetweenTimes = totalSeconds2 - totalSeconds1
        println(secondsElapsedBetweenTimes)
        //convert seconds to hh:mm:ss
        var minutesElapsedBetweenTimes: Long = 0
        var hoursElapsedBetweenTimes: Long = 0
        minutesElapsedBetweenTimes += (secondsElapsedBetweenTimes / 60)
        hoursElapsedBetweenTimes += (minutesElapsedBetweenTimes / 60)
        secondsElapsedBetweenTimes %= 60
        minutesElapsedBetweenTimes %= 60

        return ("$hoursElapsedBetweenTimes hr $minutesElapsedBetweenTimes min $secondsElapsedBetweenTimes sec")
    }

    private fun Long.to24HourFormat(ampm: String): Long {
        var hour21: Long = this
        if (this == 12.toLong() && ampm == "am") hour21 =
            0.toLong() //if ampm1 is "pm" (i.e. it is 12:00pm going into 1:00pm/13:00) it should be 12:00
        when (hour21) {
            1.toLong() -> hour21 = if (ampm == "pm") {
                13
            } else 1
            2.toLong() -> hour21 = if (ampm == "pm") {
                14
            } else 2
            3.toLong() -> hour21 = if (ampm == "pm") {
                15
            } else 3
            4.toLong() -> hour21 = if (ampm == "pm") {
                16
            } else 4
            5.toLong() -> hour21 = if (ampm == "pm") {
                17
            } else 5
            6.toLong() -> hour21 = if (ampm == "pm") {
                18
            } else 6
            7.toLong() -> hour21 = if (ampm == "pm") {
                19
            } else 7
            8.toLong() -> hour21 = if (ampm == "pm") {
                20
            } else 8
            9.toLong() -> hour21 = if (ampm == "pm") {
                21
            } else 9
            10.toLong() -> hour21 = if (ampm == "pm") {
                22
            } else 10
            11.toLong() -> hour21 = if (ampm == "pm") {
                23
            } else 11
        }
        return hour21
    }
}
