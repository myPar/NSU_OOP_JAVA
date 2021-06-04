package ThreadPool;

import FactoryObjects.FactoryObject;
import Storage.Storage;
import Storage.CarStorage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CarFactory {
// static fields calculates in config method:
    private static int workersCount;
    private static int maxTaskQueueSize;
    static boolean isConfigured;
// other fields:
    // thread pool
    private ThreadPoolExecutor threadPool;
    // Detail storage's
    private Storage motorStorage;
    private Storage bodyStorage;
    private Storage accessoryStorage;
    private CarStorage carStorage;
// config method
    public static void config(int wCount, int maxSize) {
        assert !isConfigured;
        assert wCount > 0;
        assert maxSize > 0;

        isConfigured = true;
        workersCount = wCount;
        maxTaskQueueSize = maxSize;
    }
// constructor
    public CarFactory(Storage mStorage, Storage bStorage, Storage aStorage, CarStorage cStorage) {
        assert isConfigured;
        // create fixed size blocking queue
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(maxTaskQueueSize);
        // create fixed-size thread pool
        threadPool = new ThreadPoolExecutor(workersCount, workersCount, 10, TimeUnit.MICROSECONDS, taskQueue);
        // check storage's:
        assert mStorage != null;
        assert bStorage != null;
        assert aStorage != null;
        assert cStorage != null;
        // check storage's types
        assert mStorage.getType() == FactoryObject.Type.MOTOR;
        assert bStorage.getType() == FactoryObject.Type.BODY;
        assert aStorage.getType() == FactoryObject.Type.ACCESSORY;
        // init storage's:
        motorStorage = mStorage;
        bodyStorage = bStorage;
        accessoryStorage = aStorage;
        carStorage = cStorage;
    }
// put new task in pool method
    public void addTask() {
        threadPool.execute(new Task(motorStorage, bodyStorage, accessoryStorage, carStorage));
    }
}
