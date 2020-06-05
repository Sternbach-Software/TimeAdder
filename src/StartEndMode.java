import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class StartEndMode {
    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    public static void main(String[] args) {
 /*   String time1=getInput();
    String time2=getInput();*/
        String test1a = "11:30 AM";
        String test1b = "4:30 PM";
        String test1c = "4:31 PM";
        String test2 = "4:30PM";//will deal with later

        //convert AM-PM to 24-hour format
        int hoursElapsedBetwenTimes = 0;
        int minutesElapsedBetweenTimes=0;
        int hour1 = parseInt(test1a.substring(0, test1a.indexOf(":")));
        int minute1 = parseInt(test1a.substring(test1a.indexOf(":") + 1, test1a.indexOf(" ")));
        String ampm1 = test1a.substring(test1a.indexOf(" ") + 1).toLowerCase();
        int hour2 = parseInt(test1b.substring(0, test1b.indexOf(":")));
        int minute2 = parseInt(test1b.substring(test1b.indexOf(":") + 1, test1b.indexOf(" ")));
        String ampm2 = test1b.substring(test1b.indexOf(" ") + 1).toLowerCase();
        while (hour2 > hour1) {
            hour1++;
            hoursElapsedBetwenTimes++;
        }

    }
}
