import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;

public class RepeatHashCommaDelimitedFromLeft {
    public static void main(String[] args) {

        //TODO CODE NEEDED TO BE COMMENTED OUT BECAUSE OF A JDK CHANGE. MAY COME BACK TO THIS LATER

        /*
double second =59;
double minute= 20.578;
double hour =276666666666666666666666666666666666666.0;
        String secondString = Double.toString(second);
        int sintegerPlaces = secondString.indexOf('.');
        int sdecimalPlaces = secondString.length() - sintegerPlaces - 1;
        DecimalFormat dfs;
        if (sdecimalPlaces > 3) {
            dfs = new DecimalFormat("#." + "#".repeat(sdecimalPlaces));
        } else {
            dfs = new DecimalFormat("#.#" + "#".repeat(sdecimalPlaces));
        }
        String minuteString = Double.toString(minute);
        int mintegerPlaces = minuteString.indexOf('.');
        int mdecimalPlaces = minuteString.length() - mintegerPlaces - 1;
        DecimalFormat dfm = new DecimalFormat("#.#" + "#".repeat(mdecimalPlaces));
        String hourString = Double.toString(hour);
        int hintegerPlaces = hourString.indexOf('.');
        int hdecimalPlaces = hourString.length() - hintegerPlaces - 1;
        DecimalFormat dfh = new DecimalFormat("#.#" + "#".repeat(hdecimalPlaces));










        repeatHash(25);//the code will now successfull generate left delimited(comma) hash signs: e.g. ##,###,###. remember to put this back in the DecimalFormat df variable and pass decimalPlaces as int to repeatHash, and make sire commas after original . in string provide dersired result
*/
    }

//    fun repeatHash1(i: Int): String { //make this extension of string so that i can repeat "#" making them comma delimited
//        val hashes = StringBuilder()
//        for (x in 1..i) {
//            hashes.append("#")
//        }
//        println(hashes.toString())
//        //        System.out.println(hashes.toString().length()/*this is how many hashes there will be*/);
////        hashes.append(".");
////        System.out.println(hashes.indexOf("."));
//        var x = hashes.toString().length - 3
//        while (x > 0) {
//            hashes.insert(x, ',')
//            if (x == 2 || x == 1) break
//            x -= 3
//        }
//        return hashes.toString()
//    }

    public static String repeatHash(int i) {//make this extension of string so that i can repeat "#" making them comma delimited
        StringBuilder hashes= new StringBuilder();
        for (double x = 1; x <= i; x++) {
            hashes.append("#");
        }
        System.out.println(hashes.toString());
//        System.out.println(hashes.toString().length()/*this is how many hashes there will be*/);
//        hashes.append(".");
//        System.out.println(hashes.indexOf("."));
        for(int x=hashes.toString().length()-3;x>0;x-=3){
        hashes.insert(x,',');
        if(x==2||x==1) break;
        }
        return(hashes.toString());
    }

}
