package Shop;

import GUI.DeltaTimeManager;
import Storage.CarStorage;

// Car shop class
public class CarShop {
// fields:
    private static int dealersCount;
    private static boolean isConfigured = false;
    private Dealer dealers[];
    private CarStorage storage;
    private DeltaTimeManager timeManager;
// configure method
    public static void config(int dCount) {
        assert !isConfigured;
        assert dealersCount > 0;
        isConfigured = true;
        dealersCount = dCount;
    }
// constructor
    public CarShop(CarStorage storage) {
        assert isConfigured;
        assert storage != null;
        // create dealers array
        dealers = new Dealer[dealersCount];
        // init car storage field
        this.storage = storage;
        // init time manager
        timeManager = new DeltaTimeManager();
        // create dealers:
        for (int i = 0; i < dealersCount; i++) {
            dealers[i] = new Dealer(storage, timeManager);
        }
    }
// get time manager method
    public DeltaTimeManager getTimeManager() {
        return timeManager;
    }
// run all dealers method
    public void run() {
        for (Dealer dealer:dealers) {
            dealer.start();
        }
    }
}
