package Storage;

import FactoryObjects.FactoryObject;
import Log.StorageLogger;

import java.util.LinkedList;
import java.util.Queue;

public class Storage {
// static fields:
    // storage config flag
    private static boolean isConfigured = false;
    // capacities for all storage types (Motor, Body, Accessory)
    private static int[] capacities = {0, 0, 0};
    // log files names array
    private static final String[] logFilesNames = {"./src/Log/logfiles/MotorStorageLog.txt", "./src/Log/logfiles/BodyStorageLog.txt", "./src/Log/logfiles/AccessoryStorageLog.txt"};
// fields:
    // max obj count in storage
    private int capacity;
    // type of Factory objects in the storage
    private FactoryObject.Type type;
    // queue of details objects
    Queue<FactoryObject> storage;
    // Storage logger
    StorageLogger logger;
// config method
    public static void config(int cap1, int cap2, int cap3) {
        assert !isConfigured;
        assert  cap1 > 0;
        assert  cap2 > 0;
        assert  cap3 > 0;

        isConfigured = true;
        capacities[0] = cap1;
        capacities[1] = cap2;
        capacities[2] = cap3;
    }
// constructor
    public Storage(FactoryObject.Type type) {
        // storage class should be configured
        assert isConfigured;
        // Detail storage can't contain cars
        assert type != FactoryObject.Type.CAR;

        storage = new LinkedList<>();
        // init capacity for storage of such type
        capacity = capacities[type.getValue()];
        this.type = type;
        // init logger
        logger = new StorageLogger(type.toString() + "StorageLogger", logFilesNames[type.getValue()]);
    }
// put detail in the storage method
    public synchronized void put(FactoryObject obj) {
        assert obj != null;
        // obj type should be equal to storage type
        assert obj.getType() == type;
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
                // add new detail to queue is queue is not full
                storage.add(obj);
                break;
            }
        }
        logger.log(StorageLogger.LogMessageType.PUT, obj);
        // queue is not empty so detail can be taken
        notify();
    }
// get detail from storage method
    public synchronized FactoryObject get() {
        assert storage.size() <= capacity;
        FactoryObject result;

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
        logger.log(StorageLogger.LogMessageType.GET, result);
        // queue is not full so detail can be added
        notifyAll();

        return result;
    }
// getters:
    public FactoryObject.Type getType() {
        return type;
    }
}
