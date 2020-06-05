import java.io.BufferedInputStream;
import java.util.Scanner;

public class RandomTests3 {
    public static void main(String[] args) {

        Scanner stdin = new Scanner(new BufferedInputStream(System.in));
        while (stdin.hasNext()) {
            System.out.println(Math.abs(stdin.nextLong() - stdin.nextLong()));
        }
    }
}