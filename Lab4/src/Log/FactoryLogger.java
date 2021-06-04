package Log;

import FactoryObjects.FactoryObject;

import java.util.logging.Level;

public class FactoryLogger extends BaseLogger {
    public FactoryLogger(String name, String logFileName) {
        super(name, logFileName);
    }
    // only one thread can write to log file at the same time
    public synchronized void log(FactoryObject obj) {
        String baseLogMessage = getObjLog(obj);
        logger.log(Level.INFO, baseLogMessage + " was put to storage by Thread " + Thread.currentThread().getId() + "\n");
    }
}
