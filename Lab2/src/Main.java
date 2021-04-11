import Calculator.Calculator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private static Logger logger;

    static {
        // config logger
        try {
            FileInputStream inputConfigStream = new FileInputStream("./src/logConfig.txt");
            LogManager.getLogManager().readConfiguration(inputConfigStream);
        }
        catch (Exception e) {
            System.err.println("Logger configuration failed");
            System.err.println("Fatal error");
        }
        logger = Logger.getLogger("logger.level1");
    }
    public static void main(String[] args) {
        int argNumber = args.length;
        logger.log(Level.INFO, "Args number: " + argNumber);

        Calculator calculator = new Calculator();
        Scanner sc;

        if (argNumber == 1) {
            logger.log(Level.INFO, "calculator's data source: file");
            // calculator reads data from file
            File file = new File(args[0]);

            try {
                sc = new Scanner(file);
            }
            catch (FileNotFoundException e) {
                logger.log(Level.WARNING, "invalid scanner creation");
                System.err.println("can't create Scanner, file is invalid");
                return;
            }
        }
        else if (argNumber == 0) {
            logger.log(Level.INFO, "calculator's data source: console");
            // calculator reads data from console
            sc = new Scanner(System.in);
        }
        else {
            logger.log(Level.WARNING, "you mark incorrect number of args");
            System.err.println("invalid args number");
            return;
        }
        logger.log(Level.INFO, "Scanner has been configured. calculate method call:\n");
        // main calculate method
        calculator.calculate(sc);
        logger.log(Level.INFO, "Calculator's work has been completed");
        sc.close();
    }
}
