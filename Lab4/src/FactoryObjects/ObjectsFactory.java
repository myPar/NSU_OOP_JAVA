package FactoryObjects;

import Log.ObjectsFactoryLogger;

public class ObjectsFactory {
    // id should be unique for each obj, so object creating methods are synchronized
    private static int maxId;
    // counts of created objects (need for GUI)
    private static int[] detailCreatedCounts = {0, 0, 0};
    private static int carCreatedCount;
    // ObjectsFactory logger
    private static ObjectsFactoryLogger logger;
    private static final String logFileName = "./src/Log/logfiles/ObjectFactoryLog.txt";
    // static init block
    static {
        maxId = 0;
        carCreatedCount = 0;
        logger = new ObjectsFactoryLogger("ObjectsFactoryLogger", logFileName);
    }

    public static synchronized Detail makeDetail(FactoryObject.Type type) {
        Detail detail = new Detail(type, maxId++);
        logger.log(detail);
        // increment created details count
        detailCreatedCounts[type.getValue()]++;

        return detail;
    }
    public static synchronized Car makeCar(Detail motor, Detail body, Detail accessory) {
        Car car = new Car(motor, body, accessory, maxId++);
        logger.log(car);
        carCreatedCount++;

        return car;
    }
    public static synchronized int[] getCounts() {
        int[] result = new int[4];
        result[0] = detailCreatedCounts[0];
        result[1] = detailCreatedCounts[1];
        result[2] = detailCreatedCounts[2];

        result[3] = carCreatedCount;

        return result;
    }
}
