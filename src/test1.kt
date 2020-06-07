import java.io.File

fun main() {
    createAddTimeMultiExampleFileAndOpenFile()
//    TODO("Create text file in the same directory as jar, write properly formatted times to it, and open the file for the user to see")

    /* val hhCmmCssAMPM = "-?[0-9]+:-?[0-9]+:?-?[0-9]+? [AaPpMm]{2}".toRegex() //such as "15:22:16"
for(i in 1..5)if(readLine()?.matches(hhCmmCssAMPM)!!) println("matches")*/
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
