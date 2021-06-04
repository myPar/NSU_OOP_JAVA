package Factory;

import FactoryObjects.FactoryObject;
import FactoryObjects.ObjectsFactory;
import Storage.Storage;

public class Supplier extends Thread {
// fields:
    // supplier type
    private FactoryObject.Type type;
    // reference to storage
    private Storage storageReference;
    // delta time of supplying
    private int deltaTime;
// constructor:
    public Supplier(FactoryObject.Type type, Storage ref) {
        // storage type and supplier type should be equal
        assert ref.getType() == type;
        // supplier can only put Details not cars
        assert type.getValue() < 3;
        // init fields:
        this.type = type;
        this.storageReference = ref;
        deltaTime = 10;
    }
// put methods:
    // put detail to storage method
    private void putDetail() {
        switch (type) {
            case BODY:
                storageReference.put(ObjectsFactory.makeDetail(FactoryObject.Type.BODY));
                break;
            case MOTOR:
                storageReference.put(ObjectsFactory.makeDetail(FactoryObject.Type.MOTOR));
                break;
            case ACCESSORY:
                storageReference.put(ObjectsFactory.makeDetail(FactoryObject.Type.ACCESSORY));
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
                Thread.sleep(deltaTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
