package Factory;

import FactoryObjects.FactoryObject;
import FactoryObjects.ObjectsFactory;
import Log.FactoryLogger;
import Storage.Storage;

public class Supplier extends Thread {
// fields:
    // supplier type
    private FactoryObject.Type type;
    // reference to storage
    private Storage storageReference;
    // Factory logger
    FactoryLogger logger;
    // time manager
    GUI.DeltaTimeManager timeManager;
// constructor:
    public Supplier(FactoryObject.Type type, Storage ref, FactoryLogger logger, GUI.DeltaTimeManager timeManager) {
        // storage type and supplier type should be equal
        assert ref.getType() == type;
        // supplier can only put Details not cars
        assert type.getValue() < 3;
        // init fields:
        this.type = type;
        this.storageReference = ref;
        this.logger = logger;
        this.timeManager = timeManager;
    }
// put methods:
    // put detail to storage method
    private void putDetail() {
        switch (type) {
            case BODY:
                FactoryObject body = ObjectsFactory.makeDetail(FactoryObject.Type.BODY);
                storageReference.put(body);
                logger.log(body);
                break;
            case MOTOR:
                FactoryObject motor = ObjectsFactory.makeDetail(FactoryObject.Type.MOTOR);
                storageReference.put(motor);
                logger.log(motor);
                break;
            case ACCESSORY:
                FactoryObject accessory = ObjectsFactory.makeDetail(FactoryObject.Type.ACCESSORY);
                storageReference.put(accessory);
                logger.log(accessory);
                break;
            default:
                assert false;
        }
    }
// main run method:
    @Override
    public void run() {

        while (true) {
            putDetail();
            try {
                Thread.sleep(timeManager.getDeltaTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
