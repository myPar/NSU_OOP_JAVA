package Log;

import FactoryObjects.FactoryObject;

import java.util.logging.Level;

public class StorageLogger extends BaseLogger {
    public enum LogMessageType{GET, PUT}

    public StorageLogger(String name, String logFileName) {
        super(name, logFileName);
    }
    // log method is calling inside Storage synchronized context so no need to declare it as synchronized
    public void log(LogMessageType messageType, FactoryObject obj) {
        String baseLogMessage = getObjLog(obj);

        switch (messageType) {
            case GET:
                logger.log(Level.INFO, baseLogMessage + " was got from storage by Thread " + Thread.currentThread().getId() + "\n");
                break;
            case PUT:
                logger.log(Level.INFO, baseLogMessage + " was put to storage by Thread " + Thread.currentThread().getId() + "\n");
                break;
            default:
                assert false;
        }
    }
}
