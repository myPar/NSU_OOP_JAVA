import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Statistic {
// fields:
    // map of pairs - word : it's frequency
    private HashMap<String, Integer> inputData;
    // map of pairs - frequency : list of words with such frequency
    private TreeMap<Integer, LinkedList<String>> outputData;
    // word count
    private int wordsCount;
// methods:
    // add new word to inputData field
    public void addWord(String word) {
        assert(word != null);
        // put new word in map
        Integer value = inputData.get(word);

        if (value != null) {
            inputData.put(word, value + 1);
        }
        else {
            inputData.put(word, 0);
        }
        // increment word count
        wordsCount++;
    }
    // convert inputData to outputData
    public void makeOutputData() {
        // iterate map
        for (HashMap.Entry<String, Integer> entry : inputData.entrySet()) {
            String word = entry.getKey();
            Integer frequency = entry.getValue();

            LinkedList<String> wordList = outputData.get(frequency);

            if (wordList == null) {
                // create new word list
                wordList = new LinkedList<>();
            }
            // add new word in list
            wordList.add(word);
            // update map
            outputData.put(frequency, wordList);
        }
    }
    // prints data to CSV output file
    public void printData(String fileName) throws ParserException {
        assert(fileName != null);

        File outputFile = new File(fileName);
        FileWriter writer;
        // create FileWriter
        try {
            writer = new FileWriter(outputFile);
        }
        catch (IOException e) {
            throw new ParserException("Statistics", "can't create a FileWriter - file is invalid");
        }
        // iterate outputMap
        for (Integer frequency : outputData.keySet()) {
            LinkedList<String> wordList = outputData.get(frequency);

            // print words in current word list
            for (String word : wordList) {
                printWord(word, frequency, writer);
            }
        }
    }
    // help method for formatting word printing
    private void printWord(String word, Integer frequency, FileWriter writer) throws ParserException {
        float percentFrequency = (float) frequency / wordsCount * 100;

        try {
            writer.write(word + "," + frequency + "," + percentFrequency + "\n");
        }
        catch (IOException e) {
            throw new ParserException("Statistics", "can't write data to file");
        }
    }
    // constructor
    Statistic() {
        inputData = new HashMap<>();
        outputData = new TreeMap<>();
        wordsCount = 0;
    }
}
