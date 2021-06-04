package Controller;

import Storage.CarStorage;
import ThreadPool.CarFactory;
import ThreadPool.Task;

// Controller class checks Storage state and send tasks to thread pool
public class Controller extends Thread {
    // tasks count sent to thread pool (for all time)
    private int tasksSent;
    private CarStorage storage;
    private CarFactory factory;
// constructor
    public Controller(CarStorage storage, CarFactory factory) {
        this.storage = storage;
        this.factory = factory;
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
            }
        }
    }
}
