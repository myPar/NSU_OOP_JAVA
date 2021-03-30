package Calculator;

import Command.Command;
import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

import java.util.Arrays;
import java.util.Scanner;

public class Calculator {
// main method
    public void calculate(Scanner sc) {
        assert(sc != null);
        // try to config Command (set names for commands)
        try {
            Command.config();
        }
        catch (Command.CommandException e) {
            // fatal exception
            e.printExceptionMessage();
            System.err.println("fatal exception");
            System.exit(1);
        }
        // create execution context (Stack is empty)
        ExecutionContext context = new ExecutionContext();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] commandData = line.split(" ");

            String commandName = commandData[0];
            // check comment case
            if (commandName.charAt(0) == '#') {
                // continue reading
                continue;
            }
            String[] args = Arrays.copyOfRange(commandData, 1, commandData.length);

            Command command = null;
            // try to create command
            try {
                command = Command.factoryMethod(commandName);
            }
            catch (CalculatorException e) {
                // fatal exception
                e.printExceptionMessage();
                System.err.println("fatal exception");
                System.exit(1);
            }
            // try to execute command
            try {
                command.execute(context, args);
            }
            catch (CalculatorException e) {
                // not fatal command execution exception
                e.printExceptionMessage();
            }
        }
    }
}
