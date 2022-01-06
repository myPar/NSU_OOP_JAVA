package Calculator;

import Command.Command;
import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {
// logger
    private static Logger logger = Logger.getLogger("logger.level2");
// main method
    public void calculate(Scanner sc) {
        assert(sc != null);
        // try to config Command (set names for commands)
        try {
            logger.log(Level.INFO, "factory configuration:\n");
            Command.config();
            logger.log(Level.INFO, "factory has been configured\n\n");
        }
        catch (Command.CommandException e) {
            logger.log(Level.WARNING, "factory configuration failed");
            // fatal exception
            e.printExceptionMessage();
            System.err.println("fatal exception");
            System.exit(1);
        }
        // create execution context (Stack is empty)
        ExecutionContext context = new ExecutionContext();
        logger.log(Level.INFO, "parsing input data:");

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] commandData = line.split(" ");

            String commandName = commandData[0];
            // check comment case
            if (commandName.charAt(0) == '#') {
                logger.log(Level.INFO, "comment string");
                // continue reading
                continue;
            }
            String[] args = Arrays.copyOfRange(commandData, 1, commandData.length);
            logger.log(Level.INFO, "command string");

            Command command = null;
            // try to create command
            try {
                command = Command.factoryMethod(commandName);
            }
            catch (CalculatorException e) {
                logger.log(Level.WARNING, "factory didn't create command properly");
                // fatal exception
                e.printExceptionMessage();
                System.err.println("fatal exception");
                System.exit(1);
            }
            String commandClassName = command.getClass().getSimpleName();
            logger.log(Level.INFO, "command: " + commandName + ". class name: " + commandClassName);
            // try to execute command
            try {
                logger.log(Level.INFO, "command " + commandName + " execution");
                command.execute(context, args);
                logger.log(Level.INFO, "command " + commandName + " execution succeeded\n");
            }
            catch (CalculatorException e) {
                logger.log(Level.WARNING, "command " + commandName + " execution failed\n");
                // not fatal command execution exception
                e.printExceptionMessage();
            }
        }
    }
    // Debug method for tests
    public void debugCommandExecution(String commandName, ExecutionContext context, String[] args) throws CalculatorException {
        Command command = Command.factoryMethod(commandName);
        command.execute(context, args);
    }
}
