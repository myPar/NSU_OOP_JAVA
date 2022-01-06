package Log;

import FactoryObjects.Car;
import FactoryObjects.FactoryObject;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BaseLogger {
    protected Logger logger;

    // base logger configuration constructor
    protected BaseLogger(String name, String logFileName) {
        logger = Logger.getLogger(name);
        FileHandler logFileHandler = null;
        try {
            // init handler for log file
            logFileHandler = new FileHandler(logFileName);
        }
        catch (IOException e) {
            System.err.println("BaseLogger creation error: can't create file handler with file name - " + logFileName);
            System.exit(1);
        }
        // set default log file format
        logFileHandler.setFormatter(new SimpleFormatter());
        // add file handler
        logger.addHandler(logFileHandler);
        // don't use parent handlers
        logger.setUseParentHandlers(false);
    }
    // get FactoryObject log method
    protected String getObjLog(FactoryObject obj) {
        String baseMessage = obj.getType().toString() + "<id-" + obj.getId() + ">";

        switch (obj.getType()) {
            case MOTOR:
            case BODY:
            case ACCESSORY:
                return baseMessage;
            case CAR:
                Car car = (Car) obj;
                return baseMessage + "(" + getObjLog(car.getMotor()) + ", " + getObjLog(car.getBody()) + ", " + getObjLog(car.getAccessory()) + ")" + "\n";
            default:
                assert false;
        }
        // this statement can't be executed
        return null;
    }
}
