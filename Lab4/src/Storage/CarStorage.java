package Storage;

import FactoryObjects.Car;
import Log.StorageLogger;

import java.util.LinkedList;
import java.util.Queue;

public class CarStorage {
// sub classes:
    // Car storage state class (need for controller)
    public static class CarStorageSate {
        private int carsInStorage;
        private int carsGot;
        private int capacity;
        // constructor
        public CarStorageSate(int carsGot, int carsInStorage, int capacity) {
            this.carsGot = carsGot;
            this.carsInStorage = carsInStorage;
            this.capacity = capacity;
        }
        // getters:
        public int getCarsGot() {
            return carsGot;
        }
        public int getCarsInStorage() {
            return carsInStorage;
        }
        public int getCapacity() {
            return capacity;
        }
}
// static fields:
    // storage config flag
    private static boolean isConfigured = false;
    private static int carStorageCapacity;
// other fields:
    // current storage capacity
    private int capacity;
    // car queue
    private Queue<Car> storage;
    // count of cars got from storage buy dealers
    private int carGotCount;
    // CarStorage logger
    private StorageLogger logger;
    private final String logFileName = "./src/Log/logfiles/CarStorageLog.txt";
// config method
    public static void config(int capacity) {
        assert !isConfigured;
        isConfigured = true;
        carStorageCapacity = capacity;
    }
// constructor
    public CarStorage() {
        assert isConfigured;
        // based linked list storage
        storage = new LinkedList<>();
        // copy static field value to class instance field
        capacity = carStorageCapacity;
        // init logger
        logger = new StorageLogger("CarStorageLogger", logFileName);
        carGotCount = 0;
    }
// public methods:
    // synchronized method of getting Storage state (for Controller)
    public synchronized CarStorageSate getState() {
        try {
            // controller thread is waiting till car will be put/get to/from storage
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CarStorageSate(carGotCount, storage.size(), capacity);
    }
    // put new car in storage method (for CarFactory)
    public synchronized void put(Car car) {
        assert car != null;
        // storage size should be less or equal to capacity
        assert storage.size() <= capacity;

        while (true) {
            // if queue is full wait
            if (storage.size() >= capacity) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                // add new car to queue is queue is not full
                storage.add(car);
                break;
            }
        }
        logger.log(StorageLogger.LogMessageType.PUT, car);
        // queue is not empty so car can be taken and controller can check storage state
        notifyAll();
    }
    // get car from storage method (for Dealers)
    public synchronized Car get() {
        assert storage.size() <= capacity;
        Car result;

        while (true) {
            // if queue is empty - wait
            if (storage.size() <= 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                result = storage.remove();
                break;
            }
        }
        carGotCount++;
        logger.log(StorageLogger.LogMessageType.GET, result);
        // queue is not full so car can be added and controller can check storage state
        notifyAll();

        return result;
    }
}
