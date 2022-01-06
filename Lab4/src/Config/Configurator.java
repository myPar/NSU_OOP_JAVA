package Config;

import Factory.Factory;
import Shop.CarShop;
import Storage.Storage;
import Storage.CarStorage;
import ThreadPool.CarFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configurator {
    private static final String configFileName = "./src/Config/config.txt";
    private static final int factoriesCount = 3;
    private static final int storageCount = 4;
    private static int tokensPerLine = 3;

    // raise config file error method
    private static void raiseError(String message, int pos) {
        System.err.println("Error at pos: " + pos);
        System.err.println(message);
        System.exit(1);
    }
    // get int value from config file string (with errors handling)
    private static int getValue(Scanner sc, int pos) {
        // read next line
        String line = sc.nextLine();

        if (line == null) {
            raiseError("not enough lines in config file.", pos);
        }
        // get tokens array
        String[] tokens = line.split(" ");

        // check tokens count, should be value of 'tokensPerLine'
        if (tokens.length != tokensPerLine) {
            raiseError("invalid tokens count.", pos);
        }
        int lastIdx = tokens.length - 1;

        // get storage size
        int size = 0;
        try {
            size = Integer.valueOf(tokens[lastIdx]);
        }
        catch (NumberFormatException e) {
            raiseError("invalid token, should be an integer value.", pos);
        }
        if (size <= 0) {
            raiseError("invalid integer: should be a positive value.", pos);
        }
        return size;
    }
    // config all classes method
    public static void configAllClasses() {
        File input = new File(configFileName);
        Scanner sc = null;
        // try to create s scanner
        try {
            sc = new Scanner(input);
        } catch (FileNotFoundException e) {
            assert false;
        }
        int[] storageSizes = {0, 0, 0, 0};
        int[] suppliersCounts = {0, 0, 0};
        int currentLinePos = 0;

        // parse storage sizes
        for (int i = 0 ; i < storageCount; i++) {
            int size = getValue(sc, currentLinePos);
            storageSizes[i] = size;

            currentLinePos++;
        }
        // parse suppliers
        for (int i = 0; i < factoriesCount; i++) {
            int count = getValue(sc, currentLinePos);
            suppliersCounts[i] = count;
            currentLinePos++;
        }
        // parse workers
        int workersCount = getValue(sc, currentLinePos);
        currentLinePos++;
        // parse dealers
        int dealersCount = getValue(sc, currentLinePos);

        // config classes:
        Storage.config(storageSizes[0], storageSizes[1], storageSizes[2]);         // config Storage class  (sizes)
        CarStorage.config(storageSizes[3]);                                        // config CarStorage class (size)
        Factory.config(suppliersCounts[0], suppliersCounts[1], suppliersCounts[2]);// config Factory class (suppliers count)
        CarFactory.config(workersCount, storageSizes[3] + 1);              // config CarFactory class (workers count)
        CarShop.config(dealersCount);                                              // config CarShop class  (dealers count)
    }
}
