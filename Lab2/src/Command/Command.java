package Command;

public abstract class Command {
// static fields

// methods
    // main abstract method (command execution)
    public abstract void execute();
    // Command configuration method
    public static void config() {

    }
    // factory method
    public static Command factoryMethod() {
        return null;
    }
}
