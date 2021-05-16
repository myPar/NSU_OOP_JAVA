package Detail;

public class Detail {
// detail type enum class
    public enum Type{MOTOR(0), BODY(1), ACCESSORY(2);
        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

// detail type
    private Type type;
//  unique detail id field
    private int id;
// constructor (package private)
    Detail(Type type, int id) {
        this.type = type;
        this.id = id;
    }
// getters:
    public Type getType() {return type;}
    public int getId() {return id;}
}
