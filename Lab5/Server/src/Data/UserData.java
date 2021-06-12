package Data;

import Server.Connector;

public class UserData {
// main client fields (id, user name, connector obj)
    private Connector connector;
    private int id;
    private String name;
// constructor:
    public UserData(Connector connector, int id, String name) {
        this.name = name;
        this.id = id;
        this.connector = connector;
    }
// getters:
    public Connector getConnector() {
        return connector;
    }

    public int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }
}
