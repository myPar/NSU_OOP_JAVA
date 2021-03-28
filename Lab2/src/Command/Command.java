package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Command {
// Exception class
    public static class CommandException extends CalculatorException {
        // block of static initialization
        static {
            types = new String[3];
            types[0] = "factory config";
            types[1] = "command creation";
            types[2] = "command execution";
        }
        // exception constructor
        public CommandException(int t, String m) {
            assert(t >= 0 && t < types.length);
            message = m;
            typeValue = types[t];
        }
    }
// static fields
    // map of pairs: command name - full qualified class name
    private static HashMap<String, Class<?>> commandMap;
    private static boolean isConfigured;
    private static String resourceName;
// block of static initialization
static {
    // init static fields
    commandMap = new HashMap<>();
    // factory is not configured
    isConfigured = false;
    // name of the configuration file
    resourceName = "resources";
}
// methods
    // main abstract method (command execution)
    public abstract void execute(ExecutionContext context, String[] args) throws CommandException;
    // Command-factory configuration method
    public static void config() throws CommandException {
        assert(resourceName != null);
        // get config file data
        InputStream dataStream =  Command.class.getResourceAsStream(resourceName);
        // resource isn't found
        if (dataStream == null) {
            throw new CommandException(0, "can't load data from the configuration resource");
        }
        // create scanner for data parsing
        Scanner sc = new Scanner(dataStream);

        // parse config file
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] arr = line.split("");

            // check length
            if (arr.length != 2) {
                throw new CommandException(0, "invalid string, should be two words on each string");
            }
            String commandName = arr[0];
            String commandClassName = arr[1];

            // check that command was not already parsed
            if (commandMap.containsKey(commandName)) {
                throw new CommandException(0, "command with name: " + commandName + " already was loaded");
            }
            // check class name correctness
            Class<?> commandClass = null;

            try {
                // try to load class
                commandClass = Class.forName(commandClassName);
            }
            catch (ClassNotFoundException e) {
                throw new CommandException(0, "can't load the class with name: " + commandClassName);
            }
            // check superclass: should be 'Command'
            if (commandClass.getSuperclass() != Command.class) {
                throw new CommandException(0, "invalid class: " + Command.class + ", the class should be extended of class 'Command'");
            }
            // check class name value: should be equal to command name
            if (!commandName.equals(commandClass.getSimpleName())) {
                throw new CommandException(0, "command name should be equal to the class name");
            }

            // command name and class are correct, so add this pair to map
            commandMap.put(commandName, commandClass);
        }
        // configured successfully
        isConfigured = true;

        sc.close();
    }
    // factory method
    public static Command factoryMethod(String commandName) throws CommandException {
        // check that factory has been configured
        if (!isConfigured) {
            throw new CommandException(1, "factory is not configured");
        }
        // check command containing
        if (!commandMap.containsKey(commandName)) {
            throw new CommandException(1, "invalid command name");
        }
        Class<?> commandClass = commandMap.get(commandName);
        Command result;

        try {
            result = (Command) commandClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            // if any exception while command creation was thrown
            throw new CommandException(1, "can't create command instance");
        }
        return result;
    }
}
