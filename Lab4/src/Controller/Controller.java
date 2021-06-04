package Controller;

import Log.ControllerLogger;
import Storage.CarStorage;
import ThreadPool.CarFactory;
import ThreadPool.Task;

// Controller class checks Storage state and send tasks to thread pool
public class Controller extends Thread {
    // tasks count sent to thread pool (for all time)
    private int tasksSent;
    // car storage
    private CarStorage storage;
    // car factory
    private CarFactory factory;
    // logger
    private ControllerLogger logger;
    private final String logFileName = "./src/Log/logfiles/ControllerLog.txt";
// constructor
    public Controller(CarStorage storage, CarFactory factory) {
        this.storage = storage;
        this.factory = factory;
        this.logger = new ControllerLogger("ControllerLogger", logFileName);
        tasksSent = 0;
    }
// main run method
    @Override
    public void run() {
        while (true) {
            CarStorage.CarStorageSate state = storage.getState();
            int tasksInPool = tasksSent - state.getCarsGot() - state.getCarsInStorage();
            int freePlacesInStorage = state.getCapacity() - state.getCarsInStorage();
            int tasksToDo = freePlacesInStorage - tasksInPool;

            if (tasksToDo > 0) {
                // add new tasks in thread pool
                for (int i = 0; i < tasksToDo; i++) {
                    factory.addTask();
                    tasksSent++;
                }
                logger.log(tasksToDo, tasksSent);
            }
        }
    }
}
