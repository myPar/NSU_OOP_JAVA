package Log;

import FactoryObjects.Car;
import java.util.logging.Level;

public class CarFactoryLogger extends BaseLogger {
    public enum LogMessageType{CREATE, PUT};

    public CarFactoryLogger(String name, String logFileName) {
        super(name, logFileName);
    }
    // only one thread can write to log file at the same time
    public synchronized void log(LogMessageType type, Car car) {
        String baseMessage = getObjLog(car);

        switch (type) {
            case CREATE:
                logger.log(Level.INFO, baseMessage + " was created by Thread " + Thread.currentThread().getId() + "\n");
                break;
            case PUT:
                logger.log(Level.INFO, baseMessage + " was put to car storage by Thread " + Thread.currentThread().getId() + "\n");
                break;
            default:
                assert false;
        }
    }
}
