package FactoryObjects;

public class ObjectsFactory {
    // id should be unique for each obj, so object creating methods are synchronized
    private static int maxId = 0;

    public static synchronized Detail makeDetail(FactoryObject.Type type) {
        Detail detail = new Detail(type, maxId++);
        return detail;
    }
    public static synchronized Car makeCar(Detail motor, Detail body, Detail accessory) {
        Car car = new Car(motor, body, accessory, maxId++);
        return car;
    }
}
