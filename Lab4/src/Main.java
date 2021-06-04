import Config.Configurator;
import Controller.Controller;
import Factory.Factory;
import FactoryObjects.FactoryObject;
import Shop.CarShop;
import Storage.Storage;
import Storage.CarStorage;
import ThreadPool.CarFactory;

public class Main {
    public static void main(String[] args) {
        // config classes
        Configurator.configAllClasses();
        // init storage
        Storage motorStorage = new Storage(FactoryObject.Type.MOTOR);
        Storage bodyStorage = new Storage(FactoryObject.Type.BODY);
        Storage accessoryStorage = new Storage(FactoryObject.Type.ACCESSORY);
        CarStorage carStorage = new CarStorage();
        // init factories
        Factory motorFactory = new Factory(FactoryObject.Type.MOTOR, motorStorage);
        Factory bodyFactory = new Factory(FactoryObject.Type.BODY, bodyStorage);
        Factory accessoryFactory = new Factory(FactoryObject.Type.ACCESSORY, accessoryStorage);
        CarFactory carFactory = new CarFactory(motorStorage, bodyStorage, accessoryStorage, carStorage);
        // init car shop
        CarShop shop = new CarShop(carStorage);
        // init Controller
        Controller controller = new Controller(carStorage, carFactory);
        // run all
        motorFactory.run();
        bodyFactory.run();
        accessoryFactory.run();
        shop.run();
        controller.run();
    }
}
