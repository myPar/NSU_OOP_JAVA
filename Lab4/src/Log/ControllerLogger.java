package Log;

import java.util.logging.Level;

public class ControllerLogger extends BaseLogger {
    public ControllerLogger(String name, String logFileName) {
        super(name, logFileName);
    }
    // called just by one thread at time so no need to declare in synchronized
    public void log(int taskToDo, int totalSandedTasks) {
        logger.log(Level.INFO, "Controller sends " + taskToDo + " tasks to threadPool; Total: " + totalSandedTasks + "\n");
    }
}
