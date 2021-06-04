package FactoryObjects;

import Log.ObjectsFactoryLogger;

public class ObjectsFactory {
    // id should be unique for each obj, so object creating methods are synchronized
    private static int maxId;
    // ObjectsFactory logger
    private static ObjectsFactoryLogger logger;
    private static final String logFileName = "./src/Log/logfiles/ObjectFactoryLog.txt";
    // static init block
    static {
        maxId = 0;
        logger = new ObjectsFactoryLogger("ObjectsFactoryLogger", logFileName);
    }

    public static synchronized Detail makeDetail(FactoryObject.Type type) {
        Detail detail = new Detail(type, maxId++);
        logger.log(detail);

        return detail;
    }
    public static synchronized Car makeCar(Detail motor, Detail body, Detail accessory) {
        Car car = new Car(motor, body, accessory, maxId++);
        logger.log(car);

        return car;
    }
}
