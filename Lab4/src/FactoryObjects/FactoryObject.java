package FactoryObjects;

// Factory object class, describes Detail(motor, accessory, body) or Car
public class FactoryObject {
    // Factory object type enum class
    public enum Type{MOTOR(0), BODY(1), ACCESSORY(2), CAR(3);
        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
    // unique id of created factory obj
    protected int id;
    // object type
    protected Type type;
// getters:
    public Type getType() {return type;}
    public int getId() {return id;}
}
