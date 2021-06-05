package Shop;

import FactoryObjects.Car;
import Storage.CarStorage;

// Dealer class
public class Dealer extends Thread {
// fields:
    private CarStorage storage;
    private GUI.DeltaTimeManager timeManager;
// constructor
    public Dealer(CarStorage storage, GUI.DeltaTimeManager timeManager) {
        this.storage = storage;
        this.timeManager = timeManager;
    }
// main run method (get car from storage with deltaTime interval)
    @Override
    public void run() {
        while(true) {
            Car car = storage.get();
            try {
                Thread.sleep(timeManager.getDeltaTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
