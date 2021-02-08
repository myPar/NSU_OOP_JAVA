package ExecutionContext;

import java.util.HashMap;
import java.util.Stack;

public class ExecutionContext {
// fields
    // stack of numbers
    private Stack<Double> numberStack;
    // map: key - parameter name; value - parameter value
    private HashMap<String, Double> parameterMap;
// methods
    // push value to the Stack
    public void pushValue(Double value) {

    }
    // pop value from stack
    public void popValue() {

    }
    // add new parameter to map
    public void addParameter(String name, Double value) {

    }
    // check consistence of parameter with such name
    public boolean checkParameter(String name) {
        return true;
    }
// constructor
    public ExecutionContext() {
        numberStack = new Stack<>();
        parameterMap = new HashMap<>();
    }
}
