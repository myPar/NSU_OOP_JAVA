import Calculator.Calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int argNumber = args.length;
        Calculator calculator = new Calculator();
        Scanner sc;

        if (argNumber == 1) {
            // calculator reads data from file
            File file = new File(args[0]);

            try {
                sc = new Scanner(file);
            }
            catch (FileNotFoundException e) {
                System.err.println("can't create Scanner, file is invalid");
                return;
            }
        }
        else if (argNumber == 0) {
            // calculator reads data from console
            sc = new Scanner(System.in);
        }
        else {
            System.err.println("invalid args number");
            return;
        }
        calculator.calculate(sc);
    }
}
