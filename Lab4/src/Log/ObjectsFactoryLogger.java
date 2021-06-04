package Log;

import FactoryObjects.FactoryObject;

import java.util.logging.Level;

public class ObjectsFactoryLogger extends BaseLogger {
    public ObjectsFactoryLogger(String name, String logFileName) {
        super(name, logFileName);
    }
    // log method is calling inside ObjectsFactory synchronized context so no need to declare it as synchronized
    public void log(FactoryObject obj) {
        String baseLogMessage = getObjLog(obj);
        logger.log(Level.INFO, baseLogMessage + " was created" + "\n");
    }
}
