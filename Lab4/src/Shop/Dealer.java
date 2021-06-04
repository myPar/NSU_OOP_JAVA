package Shop;

import FactoryObjects.Car;
import Storage.CarStorage;

// Dealer class
public class Dealer extends Thread {
// fields:
    private CarStorage storage;
    private static int deltaTime = 1000;
// constructor
    public Dealer(CarStorage storage) {
        this.storage = storage;
    }
// main run method (get car from storage with deltaTime interval)
    @Override
    public void run() {
        while(true) {
            Car car = storage.get();
            try {
                Thread.sleep(deltaTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
