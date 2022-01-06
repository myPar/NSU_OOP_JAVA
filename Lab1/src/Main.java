import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        int argNumber = args.length;

        if (argNumber != 2) {
            System.err.println("invalid argument number, should be 2\n");
        }
        else {
            String inputFileName = args[0];
            String outputFileName = args[1];

            FileReader reader = new FileReader();
            Statistic statistic = new Statistic();

            try {
                reader.readFile(inputFileName, statistic);
                statistic.makeOutputData();
                statistic.printData(outputFileName);
            }
            catch (ParserException e) {
                e.print();
            }
        }
    }
}
