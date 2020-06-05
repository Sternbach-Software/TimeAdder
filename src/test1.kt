import java.io.File
import java.io.FileNotFoundException

fun main() {
    getFilepath@while(true) {
        /*val filename =
            try {
                val pathname=readLine() ?: ""
                if(File(pathname).exists()){
                    File(pathname)}else{}
            } catch (e: FileNotFoundException) {
                println("File not found. Please Try Again")
                continue@getFilepath
            } catch(e: SecurityException){
                println("File access denied. Please either run this file as Administrator or grant read access to current user.")
            }*/
//        println({if(filename is File) filename.readLines() else "Not a file"})
        val pathname=readLine() ?: ""
        println(File(pathname).exists())
    }
    /* val hhCmmCssAMPM = "-?[0-9]+:-?[0-9]+:?-?[0-9]+? [AaPpMm]{2}".toRegex() //such as "15:22:16"
for(i in 1..5)if(readLine()?.matches(hhCmmCssAMPM)!!) println("matches")*/
}