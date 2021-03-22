package Command;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Command {
// static fields
    // map of pairs: command name - full qualified class name
    private static HashMap<String, Class<?>> commandMap;
    private static boolean isConfigured;
// block of static initialization
static {
    // init static fields
    commandMap = new HashMap<>();
    // factory is not configured
    isConfigured = false;
}
// methods
    // main abstract method (command execution)
    public abstract void execute();
    // Command-factory configuration method
    public static void config(String resourceName) {
        assert(resourceName != null);
        // get config file data
        InputStream dataStream =  Command.class.getResourceAsStream(resourceName);
        // resource isn't found
        if (dataStream == null) {
            // TODO add exception throw
            assert(false);
        }
        // create scanner for data parsing
        Scanner sc = new Scanner(dataStream);

        // parse config file
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] arr = line.split("");

            // check length
            if (arr.length != 2) {
                // TODO add exception throw
                assert(false);
            }
            String commandName = arr[0];
            String commandClassName = arr[1];

            // check that command was not already parsed
            if (commandMap.containsKey(commandName)) {
                // TODO add exception throw
                assert(false);
            }
            // check class name correctness
            Class<?> commandClass = null;

            try {
                // try to load class
                commandClass = Class.forName(commandClassName);
            }
            catch (ClassNotFoundException e) {
                // TODO add exception throw
                assert(false);
            }
            // check superclass: should be 'Command'
            if (commandClass.getSuperclass() != Command.class) {
                // TODO add exception throw
                assert(false);
            }
            // check class name value: should be equal to command name
            if (!commandName.equals(commandClass.getSimpleName())) {
                // TODO add exception throw
                assert(false);
            }

            // command name and class are correct, so add this pair to map
            commandMap.put(commandName, commandClass);
        }
        // configured successfully
        isConfigured = true;

        sc.close();
    }
    // factory method
    public static Command factoryMethod(String commandName) {
        // check that factory has been configured
        if (!isConfigured) {
            // TODO add exception throw
            assert(false);
        }
        // check command containing
        if (!commandMap.containsKey(commandName)) {
            // TODO add exception throw
            assert(false);
        }
        Class<?> commandClass = commandMap.get(commandName);
        Command result = null;

        try {
            result = (Command) commandClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            // if any exception while command creation was thrown
            //TODO add exception thrown
        }
        return result;
    }
}
