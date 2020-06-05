import static java.lang.System.nanoTime;

public class ConvertIntsLargerThan60ToSecondsMinutesHours {
    public static void main(String[] args) {
        Double second=70000000.0;
        Double minute=0.0;
        Double hour=0.0;
        Double second1=70000000.0;
        Double minute1=0.0;
        Double hour1;
        int approximationOfDesiredMinutes=116;
        long a = nanoTime();
        while (second >= 60) {
            second -= 60;
            minute ++;
            if (second - 60 < 0) break;
        }
        while (minute >= 60) {
            minute -= 60;
            hour ++;
            if (minute - 60 < 0) break;
        }
        long b = nanoTime();
        minute1 = (second1/60);
        hour1=(minute1/60);
        second1 =second1%60;
        minute1 =minute1%60;
        long c = nanoTime();
        System.out.println(hour+":"+minute+":"+second+","+(b-a));
        System.out.println(hour1+":"+minute1+":"+second1+","+(c-b));
        System.out.println(approximationOfDesiredMinutes);
    }
}
