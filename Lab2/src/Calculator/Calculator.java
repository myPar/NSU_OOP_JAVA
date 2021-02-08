package Calculator;

import Command.Command;
import java.util.Scanner;

public class Calculator {
// inner static class for command parsing
    private static class CommandParser {
        Command parseCommand(String string) {
            return null;
        }
    }
// main method
    public void calculate(Scanner sc) {
        // create command parser object
        CommandParser parser = new CommandParser();
        // config Command (set names for commands)
        Command.config();
    }
}
