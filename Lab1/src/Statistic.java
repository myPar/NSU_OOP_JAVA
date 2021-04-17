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
        inputData.merge(word, 1, Integer::sum);
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
        // write data (using try-with-resources)
        try (writer) {
            writer.write("word,count,percent frequency\n");
            // iterate outputMap
            for (Integer frequency : outputData.descendingKeySet()) {
                LinkedList<String> wordList = outputData.get(frequency);
                // print words in current word list
                for (String word : wordList) {
                    printWord(word, frequency, writer);
                }
            }
        }
        catch (IOException e) {
            throw new ParserException("Statistics", "can't write data to file");
        }
    }
    // help method for formatting word printing
    private void printWord(String word, Integer frequency, FileWriter writer) throws IOException {
        float percentFrequency = (float) frequency / wordsCount * 100;

        writer.write(word + "," + frequency + "," + percentFrequency + "\n");
    }
    // constructor
    Statistic() {
        inputData = new HashMap<>();
        outputData = new TreeMap<>();
        wordsCount = 0;
    }
}
