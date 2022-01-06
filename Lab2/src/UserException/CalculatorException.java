package UserException;

// Parent class of user exception
// other exception classes are extended from this
public class CalculatorException extends Exception {
    // Exception message
    protected String message;
    protected String typeValue;

    public void printExceptionMessage() {
        System.err.println(this.getClass().getSimpleName());
        System.err.println("Exception type - " + typeValue);
        System.err.println(message);
    }
}
