package ThreadPool;

import FactoryObjects.Car;
import FactoryObjects.Detail;
import FactoryObjects.FactoryObject;
import FactoryObjects.ObjectsFactory;
import Log.CarFactoryLogger;
import Storage.Storage;
import Storage.CarStorage;

// Task class of car making
public class Task implements Runnable {
// Storage's fields:
    private Storage motorStorage;
    private Storage bodyStorage;
    private Storage accessoryStorage;
    private CarStorage carStorage;
    private CarFactoryLogger logger;
// task constructor (need to init references to detail's storage)
    public Task(Storage mStorage, Storage bStorage, Storage aStorage, CarStorage cStorage, CarFactoryLogger logger) {
        // init fields (all fields has been checked in CarFactory constructor)
        motorStorage = mStorage;
        bodyStorage = bStorage;
        accessoryStorage = aStorage;
        carStorage = cStorage;
        this.logger = logger;
    }
// Task run method (car making and putting it to car storage)
    @Override
    public void run() {
        // get all details
        FactoryObject motor = motorStorage.get();
        FactoryObject body = bodyStorage.get();
        FactoryObject accessory = accessoryStorage.get();
        // make Car
        Car car = ObjectsFactory.makeCar((Detail) motor, (Detail) body, (Detail) accessory);
        logger.log(CarFactoryLogger.LogMessageType.CREATE, car);
        // put car in car storage
        carStorage.put(car);
        logger.log(CarFactoryLogger.LogMessageType.PUT, car);
    }
}
