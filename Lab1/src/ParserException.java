public class ParserException extends Exception {
    private String type;
    private String message;
    // constructor
    ParserException(String t, String m) {
        type = t;
        message = m;
    }
    // print exception message
    public void print() {
        // check exception type validity
        assert(type.equals("FileReader") || type.equals("Statistics"));
        // print exception description
        System.out.println("Exception of type: " + type + "\n" + "description: "+ message);
        // print stack trace
        printStackTrace();
    }
}
