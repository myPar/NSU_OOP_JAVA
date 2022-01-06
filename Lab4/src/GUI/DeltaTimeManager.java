package GUI;

// Delta time manager subclass
public class DeltaTimeManager {
    int deltaTime;
    public DeltaTimeManager() {deltaTime = 1000;}

    public synchronized void setDeltaTime(int deltaTime) {this.deltaTime = deltaTime;}
    public synchronized int getDeltaTime() {return this.deltaTime;}
}
