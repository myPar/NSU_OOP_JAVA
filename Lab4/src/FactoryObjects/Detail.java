package FactoryObjects;

public class Detail extends FactoryObject {
// constructor (package private)
    Detail(Type type, int id) {
        assert type.getValue() < 3; // can't be a 'CAR' type
        this.type = type;
        this.id = id;
    }
}
