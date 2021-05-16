package Storage;

import Detail.Detail;

import java.util.LinkedList;
import java.util.Queue;

public class Storage {
// static fields
    // storage config flag
    private static boolean isConfigured = false;
    // capacities for all storage types
    private static int[] capacities = {0, 0, 0};
// fields:
    // max details count in storage
    private int capacity;
    // type of details in the storage
    private Detail.Type type;
    // queue of details objects
    Queue<Detail> details;

// config method
    public static void config() {
        assert !isConfigured;

    }
// constructor
    public Storage() {
        assert isConfigured;

        details = new LinkedList<Detail>();
    }

// put detail in the storage method
    synchronized void put(Detail detail) {
        assert detail != null;
        assert detail.getType() == type;
        assert details.size() <= capacity;

        while (true) {
            // if queue is full wait
            if (details.size() >= capacity) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                // add new detail to queue is queue is not full
                details.add(detail);
                break;
            }
        }
        // queue is not empty so detail can be taken
        notify();
    }
// get detail from storage method
    synchronized Detail get() {
        assert details.size() <= capacity;
        Detail result;

        while (true) {
            // if queue is empty - wait
            if (details.size() <= 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                result = details.remove();
                break;
            }
        }
        // queue is not full so detail can be added
        notify();
        return result;
    }
}
