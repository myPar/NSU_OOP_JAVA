package FactoryObjects;

public class Car extends FactoryObject {
    private Detail motor;
    private Detail body;
    private Detail accessory;

    Car(Detail motor, Detail body, Detail accessory, int id) {
        // check details type correctness
        assert motor.type == Type.MOTOR;
        assert body.type == Type.BODY;
        assert accessory.type == Type.ACCESSORY;
        // init fields (details, id and type):
        this.motor = motor;
        this.body = body;
        this.accessory = accessory;
        this.id = id;
        this.type = Type.CAR;
    }
    // getters:
    public Detail getAccessory() {
        return accessory;
    }
    public Detail getBody() {
        return body;
    }
    public Detail getMotor() {
        return motor;
    }
}
