import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
// methods:
    // main method
    public void readFile(String fileName, Statistic statistic) throws ParserException {
        assert(fileName != null);
        assert(statistic != null);

        File input = new File(fileName);
        Scanner sc;
        // check file validity
        try {
            sc = new Scanner(input);
        }
        catch (FileNotFoundException e) {
            throw new ParserException("FileReader", "can't create a file scanner - file is invalid");
        }
        while (sc.hasNextLine()) {
            String str = sc.nextLine();

            for (String word : str.split("[^a-zA-Zа-яА-Я0-9]+")) {
                statistic.addWord(word);
            }
        }
    }
}
