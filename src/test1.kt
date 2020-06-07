import java.io.File

fun main() {
    val batchFile = File(System.getProperty("user.dir")+"\\OpenStartEndTimeExample.bat")
    batchFile.writeText("notepad \'"+ System.getProperty("user.dir")+"\\OpenStartEndTimeExample\'")
    val textFile=File(System.getProperty("user.dir")+"\\OpenStartEndTimeExample.txt")
    textFile.writeText("12:42 pm\n9:16:45 AM\n8:23 am\n5:48:27 PM\n11:15 AM\n9:21 am\n8:16:21 Am\n9:28:15 pM\n9:28:15 Pm")//Create example file

    val run = Runtime.getRuntime()
    run.exec(batchFile.absolutePath)
    Thread.sleep(2000L)
    textFile.delete()
    batchFile.delete()
//    TODO("Create text file in the same directory as jar, write properly formatted times to it, and open the file for the user to see")

    /* val hhCmmCssAMPM = "-?[0-9]+:-?[0-9]+:?-?[0-9]+? [AaPpMm]{2}".toRegex() //such as "15:22:16"
for(i in 1..5)if(readLine()?.matches(hhCmmCssAMPM)!!) println("matches")*/
}