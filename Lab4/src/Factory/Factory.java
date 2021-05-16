package Factory;

import Detail.Detail;

public class Factory {
// static fields:
    // count of suppliers for each Factory type
    private static int[] suppliersCounts = {0, 0, 0};
    // factory configuration flag
    private static boolean isConfigured = false;
// fields:
    // suppliers array
    private Supplier[] suppliers;
    // suppliers count
    private int suppliersCount;
    // supplying detail type
    private Detail.Type type;
// constructor
    public Factory(Detail.Type type) {
        assert isConfigured;
        // init fields:
        this.type = type;
        suppliersCount = suppliersCounts[type.getValue()];
        suppliers = new Supplier[suppliersCount];
        // create suppliers
        for (int i = 0; i < suppliersCount; i++) {
            suppliers[i] = new Supplier();
        }
    }
// Factory config method
    public static void config(int count1, int count2, int count3) {
        assert !isConfigured;
        assert count1 > 0;
        assert count2 > 0;
        assert count3 > 0;
        // init suppliers count for each type of factories:
        suppliersCounts[0] = count1;
        suppliersCounts[1] = count2;
        suppliersCounts[2] = count3;
        // Factory class has been configured
        isConfigured = true;
    }
// main run method
    public void run() {
        // start executing of all threads
        for (Supplier supplier: suppliers) {
            supplier.start();
        }
    }
}
