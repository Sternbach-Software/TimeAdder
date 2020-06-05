import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.*;

import static java.lang.Double.parseDouble;


public class Main {
    static String multi = "[hm]?[0-9]?[0-9]?[0-9]?:?[hms][0-9]?[0-9]?[0-9]";

    static String hhCmmCssAMPM = "-?[0-9]+:-?[0-9]+:?-?[0-9]+? [AaPpMm]{2}"; //such as "15:22:16"
    static String hhCmmCss = "-?[0-9]+:-?[0-9]+:-?[0-9]+"; //such as "15:22:16"
    static String hhhCmm = "h-?[0-9]+:-?[0-9]+"; //such as "h15:22"
    static String hhh = "h-?[0-9]+"; //such as "h15"
    static String hhCmm = "-?[0-9]+:-?[0-9]+"; //such as "15:22"
    static String mmmCss = "m-?[0-9]+:-?[0-9]+"; //such as "m25:16"
    static String mmm = "m-?[0-9]+"; //such as "m25"
    static String mmCss = "-?[0-9]+:-?[0-9]+";//such as "22:26"
    static String mm = "-?[0-9]+"; //only to be used in Minute Mode(2)
    static String sss = "s-?[0-9]+"; //such as "s25"
    static String ss = "-?[0-9]+?"; //only to be used in Second Mode(1)


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
    public static void main(String[] args) throws ParseException {
        System.out.println("Would you like to use the Second Mode(1), Minute Mode(2), Minute/Second Mode(3), Hour/Minute Mode(4),\nHour/Minute/Second Mode(5), Multi Mode(6), or Start/End Mode(7)? Once started, type \"done\" to see the final result or \"total\" to see the current total:");
        String mode = getInput();
        if (mode.equals("1"))
            timeAdd(ss, 1, "Enter numbers in the following format: \"16\" (i.e. 16 sec), up to \"9999\"");
        else if (mode.equals("2"))
            timeAdd(mm, 2, "Enter numbers in the following format: \"22\" (i.e. 22 min), up to \"9999\"");
        else if (mode.equals("3"))
            timeAdd(mmCss, 3, "Enter numbers in the following format: \"22:16\" (i.e. 22 min 16 sec), up to \"9999:59\"");
        else if (mode.equals("4"))
            timeAdd(hhCmm, 4, "Enter numbers in the following format: \"15:22\" (i.e. 15 hr 22 min), up to \"9999:59\"");
        else if (mode.equals("5"))
            timeAdd(hhCmmCss, 5, "Enter numbers in the following format: \"15:22:16\" (i.e. 15 hr 22 min 16 sec), up to \"9999:59:59\"");
        else if (mode.equals("6")) timeAddMulti();
        else if (mode.equals("7")) startEndMode();
    }

    private static void timeAdd(String regex, int mode, String message) {
        long inputNumber = 1;
        System.out.println(message/*"Enter numbers in any of the following formats:  15:22 (i.e. 15 hr 22 min), 4:22 (4 hr 22 min), m42 (42 min), or h12 (12 hr)"*/);
        System.out.print("Time" + inputNumber + ": ");

        String input = getInput();
        double hour = 0.0;
        double minute = 0.0;
        double second = 0.0;
        //Collect input times:
        while (true) {
            if (Pattern.matches(regex, input)) {
                //tree of destructuring decision base on time format:
                if (regex.equals(hhCmmCss)) {//if in h:mm format
                    String[] times = input.split(":");
                    hour += parseDouble(times[0]);
                    if (minute + parseDouble(times[1]) > 60) {
                        minute = ((minute + parseDouble(times[1])) - 60);
                        hour += 1;
                    } else minute += parseDouble(times[1]);
                    if (second + parseDouble(times[2]) > 60) {
                        second = ((second + parseDouble(times[2])) - 60);
                        minute += 1;
                    } else second += parseDouble(times[2]);
                } else if (regex.equals(hhCmm) && mode == 4) {//if in h:mm format
                    String[] times = input.split(":");
                    hour += parseDouble(times[0]);
                    if (minute + parseDouble(times[1]) > 60) {
                        minute = ((minute + parseDouble(times[1])) - 60);
                        hour += 1;
                    } else minute += parseDouble(times[1]);
                } else if (regex.equals(mmCss) && mode == 3) {//if in mm:ss format
                    String[] times = input.split(":");
                    if ((minute + parseDouble(times[0])) > 60) {
                        minute = ((minute + parseDouble(times[0])) - 60);
                        hour += 1;
                    } else minute = minute + parseDouble(times[0]);

                    if (second + parseDouble(times[1]) > 60) {
                        second = ((second + parseDouble(times[1])) - 60);
                        minute += 1;
                    } else second = second + parseDouble(times[1]);
                } else if (regex.equals(mmm)) {//if in mmm(e.g. m42) format
                    input = input.substring(1);
                    if (minute + parseDouble(input) > 60) {
                        minute = ((minute + parseDouble(input)) - 60);
                        hour += 1;
                    } else minute += parseDouble(input);
                } else if (regex.equals(hhh)) {
                    input = input.substring(1);
                    hour += parseDouble(input);
                } else if (regex.equals(mm) && mode == 2) {
                    if (minute + parseDouble(input) > 60) {
                        minute = ((minute + parseDouble(input)) - 60);
                        hour += 1;
                    } else minute += parseDouble(input);
                } else if (regex.equals(ss) && mode == 1) {
                    if (second + parseDouble(input) > 60) {
                        second = ((second + parseDouble(input)) - 60);
                        minute += 1;
                    } else second += parseDouble(input);
                }
            } else {
                if (!((input.equals("total")) || (input.equals("done")))) {
                    System.out.println("Incorrectly Formatted Number. Try Again.");
                    System.out.println("Time" + inputNumber + ": ");
                    input = getInput();
                    continue;
                } else if (input.equals("done")) break;
            }

            inputNumber += 1;
            System.out.print("Time" + inputNumber + ": ");
            input = getInput();
            if (input.equals("total")) {
                minute += (second/60);
                hour+=(minute/60);
                second =second%60;
                minute =minute%60;
                String secondString = Double.toString(second);
                int sintegerPlaces = secondString.indexOf('.');
                int sdecimalPlaces = secondString.length() - sintegerPlaces - 1;
                DecimalFormat dfs;
                if (sdecimalPlaces > 3) {
                    dfs = new DecimalFormat("#." + "#");
                } else {
                    dfs = new DecimalFormat("#.#" + "#");
                }
                String minuteString = Double.toString(minute);
                int mintegerPlaces = minuteString.indexOf('.');
                int mdecimalPlaces = minuteString.length() - mintegerPlaces - 1;
                DecimalFormat dfm = new DecimalFormat("#.#" + "#");
                String hourString = Double.toString(hour);
                int hintegerPlaces = hourString.indexOf('.');
                int hdecimalPlaces = hourString.length() - hintegerPlaces - 1;
                DecimalFormat dfh = new DecimalFormat("#.#" + "#");
                if (second > 0)
                    System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
                else System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min");
                System.out.println();
            } else if (input.equals("done")) break;
        }
        System.out.println();
        minute += (second/60);
        hour+=(minute/60);
        second =second%60;
        minute =minute%60;
        String secondString = Double.toString(second);
        int sintegerPlaces = secondString.indexOf('.');
        int sdecimalPlaces = secondString.length() - sintegerPlaces - 1;
        DecimalFormat dfs = new DecimalFormat("#.#" + "#");
        String minuteString = Double.toString(minute);
        int mintegerPlaces = minuteString.indexOf('.');
        int mdecimalPlaces = minuteString.length() - mintegerPlaces - 1;
        DecimalFormat dfm = new DecimalFormat("#.#" + "#");
        String hourString = Double.toString(hour);
        int hintegerPlaces = hourString.indexOf('.');
        int hdecimalPlaces = hourString.length() - hintegerPlaces - 1;
        DecimalFormat dfh = new DecimalFormat("#.#" + "#");
        if (second > 0)
            System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
        else System.out.println(dfh.format(hour) + " hr " + dfm.format(minute) + " min");
        System.out.println();
    }

    private static void startEndMode() throws ParseException {
        int i=1;
        String ab="a";
        System.out.println("Enter Time "+ i+ab+":");
        String input1=getInput();
        ab="b";
        System.out.println("Enter Time "+ i+ab+":");
        String input2=getInput();
        while(!input1.equals("done")) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            i++;
            ab="a";
            System.out.println("Enter Time "+ i+ab+":");
            input1=getInput();
            ab="b";
            System.out.println("Enter Time "+ i+ab+":");
            input2=getInput();


            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            String dateInString = "11:30:15 AM";

            try {

                Date date = formatter.parse(dateInString);
                System.out.println(date);
                System.out.println(formatter.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void timeAddMulti() {
        long inputNumber = 1;
        System.out.println("Enter numbers in any of the following formats:  15:22:31 (15 hr 22 min 31 sec), h15:22 (15 hr 22 min),\nm12:31 (12 min 31 sec), h4:22 (4 hr 22 min), m1:31 (1 min 31 sec), h12 (12 hr), m12 (12 min), or s12 (12 sec)");
        System.out.print("Time" + inputNumber + ": ");
        String input = getInput();
        double hour = 0;
        double minute = 0;
        double second = 0;
        //Collect input times:
        while (true) {
            if (Pattern.matches(hhCmmCss, input)) {//such as
                String[] times = input.split(":");
                hour += parseDouble(times[0]);
                if (minute + parseDouble(times[1]) > 60) {
                    minute = ((minute + parseDouble(times[1])) - 60);
                    hour += 1;
                } else minute += parseDouble(times[1]);
                if (second + parseDouble(times[2]) > 60) {
                    second = ((second + parseDouble(times[2])) - 60);
                    minute += 1;
                } else second += parseDouble(times[2]);
            } else if (Pattern.matches(hhhCmm, input)) {//if in h:mm format
                input = input.substring(1);
                String[] times = input.split(":");
                hour += parseDouble(times[0]);
                if (minute + parseDouble(times[1]) > 60) {
                    minute = ((minute + parseDouble(times[1])) - 60);
                    hour += 1;
                } else minute += parseDouble(times[1]);
            } else if (Pattern.matches(mmmCss, input)) {//if in mmm(e.g. m42) format
                input = input.substring(1);
                String[] times = input.split(":");
                if (minute + parseDouble(times[0]) > 60) {
                    minute = ((minute + parseDouble(times[0])) - 60);
                    hour += 1;
                } else minute += parseDouble(times[0]);
                if (second + parseDouble(times[1]) > 60) {
                    second = ((second + parseDouble(times[1])) - 60);
                    minute += 1;
                } else second += parseDouble(times[1]);
            } else if (Pattern.matches(mmm, input)) {//if in mmm(e.g. m42) format
                input = input.substring(1);
                if (minute + parseDouble(input) > 60) {
                    minute = ((minute + parseDouble(input)) - 60);
                    hour += 1;
                } else minute += parseDouble(input);
            } else if (Pattern.matches(hhh, input)) {
                input = input.substring(1);
                hour += parseDouble(input);
            } else if (Pattern.matches(sss, input)) {//if in sss(e.g. s42) format
                input = input.substring(1);
                if (second + parseDouble(input) > 60) {
                    second = ((second + parseDouble(input)) - 60);
                    minute += 1;
                } else second += parseDouble(input);
            } else {
                if (!((input.equals("total")) || (input.equals("done")))) {
                    System.out.println("Incorrectly Formatted Number. Try Again.");
                    System.out.print("Time" + inputNumber + ": ");
                    input = getInput();
                    continue;
                } else if (input.equals("done")) break;
            }
            inputNumber += 1;
            System.out.print("Time" + inputNumber + ": ");
            input = getInput();
            if (input.equals("total")) {
                minute += (second/60);
                hour+=(minute/60);
                second =second%60;
                minute =minute%60;
                String secondString = Double.toString(second);
                int sintegerPlaces = secondString.indexOf('.');
                int sdecimalPlaces = secondString.length() - sintegerPlaces - 1;
                DecimalFormat dfs = new DecimalFormat("#.#" + "#");
                String minuteString = Double.toString(minute);
                int mintegerPlaces = minuteString.indexOf('.');
                int mdecimalPlaces = minuteString.length() - mintegerPlaces - 1;
                DecimalFormat dfm = new DecimalFormat("#.#" + "#");
                String hourString = Double.toString(hour);
                int hintegerPlaces = hourString.indexOf('.');
                int hdecimalPlaces = hourString.length() - hintegerPlaces - 1;
                DecimalFormat dfh = new DecimalFormat("#.#" + "#");
                if (second > 0 & hour > 0 & minute > 0)
                    System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
                else if (second == 0 & hour == 0 & minute == 0)
                    System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
                else if (second > 0 & hour > 0 & minute == 0)
                    System.out.print(dfh.format(hour) + " hr " + dfs.format(second) + " sec");
                else if (second > 0 & hour == 0 & minute > 0)
                    System.out.print(dfm.format(minute) + " min " + dfs.format(second) + " sec");
                else if (second == 0 & hour > 0 & minute > 0)
                    System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min");
                else if (second == 0 & hour == 0 & minute > 0) System.out.print(dfm.format(minute) + " min");
                else if (second > 0 & hour == 0 & minute == 0) System.out.print(dfs.format(second) + " sec");
                else if (second == 0 & hour > 0 & minute == 0) System.out.print(dfh.format(hour) + " hr");
                System.out.println();
            } else if (input.equals("done")) break;
        }
        System.out.println();
        minute += (second/60);
        hour+=(minute/60);
        second =second%60;
        minute =minute%60;
        String secondString = Double.toString(second);
        int sintegerPlaces = secondString.indexOf('.');
        int sdecimalPlaces = secondString.length() - sintegerPlaces - 1;
        DecimalFormat dfs = new DecimalFormat("#.#" + "#");
        String minuteString = Double.toString(minute);
        int mintegerPlaces = minuteString.indexOf('.');
        int mdecimalPlaces = minuteString.length() - mintegerPlaces - 1;
        DecimalFormat dfm = new DecimalFormat("#.#" + "#");
        String hourString = Double.toString(hour);
        int hintegerPlaces = hourString.indexOf('.');
        int hdecimalPlaces = hourString.length() - hintegerPlaces - 1;
        DecimalFormat dfh = new DecimalFormat("#.#" + "#");
        if (second > 0 & hour > 0 & minute > 0)
            System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
        else if (second == 0 & hour == 0 & minute == 0)
            System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min " + dfs.format(second) + " sec");
        else if (second > 0 & hour > 0 & minute == 0)
            System.out.print(dfh.format(hour) + " hr " + dfs.format(second) + " sec");
        else if (second > 0 & hour == 0 & minute > 0)
            System.out.print(dfm.format(minute) + " min " + dfs.format(second) + " sec");
        else if (second == 0 & hour > 0 & minute > 0)
            System.out.print(dfh.format(hour) + " hr " + dfm.format(minute) + " min");
        else if (second == 0 & hour == 0 & minute > 0) System.out.print(dfm.format(minute) + " min");
        else if (second > 0 & hour == 0 & minute == 0) System.out.print(dfs.format(second) + " sec");
        else if (second == 0 & hour > 0 & minute == 0) System.out.print(dfh.format(hour) + " hr");
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

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
