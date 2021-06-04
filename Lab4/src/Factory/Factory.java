package Factory;

import FactoryObjects.FactoryObject;
import Log.FactoryLogger;
import Storage.Storage;

public class Factory {
// static fields:
    // count of suppliers for each Factory type
    private static int[] suppliersCounts = {0, 0, 0};
    // log files names array
    private static final String[] logFilesNames = {"./src/Log/logfiles/MotorFactoryLog.txt", "./src/Log/logfiles/BodyFactoryLog.txt", "./src/Log/logfiles/AccessoryFactoryLog.txt"};
    // factory configuration flag
    private static boolean isConfigured = false;
// fields:
    // suppliers array
    private Supplier[] suppliers;
    // suppliers count
    private int suppliersCount;
    // supplying detail type
    private FactoryObject.Type type;
    // reference to storage
    private Storage storageReference;
    // Factory logger
    private FactoryLogger logger;
// constructor
    public Factory(FactoryObject.Type type, Storage ref) {
        // factory should be configured
        assert isConfigured;
        assert ref != null;
        // storage type should be equal to factory type
        assert ref.getType() == type;
        // obj should be a detail, not a car
        assert type.getValue() > 3;

        // init fields:
        this.type = type;
        suppliersCount = suppliersCounts[type.getValue()];
        suppliers = new Supplier[suppliersCount];
        storageReference = ref;
        // init logger
        logger = new FactoryLogger(type.toString() + "FactoryLogger", logFilesNames[type.getValue()]);
        // create suppliers
        for (int i = 0; i < suppliersCount; i++) {
            suppliers[i] = new Supplier(type, storageReference, logger);
        }
    }
// Factory config method
    public static void config(int count1, int count2, int count3) {
        assert !isConfigured;
        assert count1 > 0;
        assert count2 > 0;
        assert count3 > 0;
        // init suppliers count for each type of factories:
        suppliersCounts[0] = count1;
        suppliersCounts[1] = count2;
        suppliersCounts[2] = count3;
        // Factory class has been configured
        isConfigured = true;
    }
// main run method
    public void run() {
        // start executing of all threads
        for (Supplier supplier: suppliers) {
            supplier.start();
        }
    }
}
