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
        numberStack.push(value);
    }
    // pop value from stack
    public double popValue() {
        return numberStack.pop();
    }
    // add new parameter to map
    public void addParameter(String name, Double value) throws Exception {
        // check consistence of parameter with such name
        if (parameterMap.containsKey(name)) {
            // TODO add user exception
            throw new Exception("");
        }
        // add new parameter to the map
        parameterMap.put(name, value);
    }
    // get value of the parameter with such name
    public double getParameter(String name) throws Exception {
        if (!parameterMap.containsKey(name)) {
            // TODO add user exception
            throw new Exception("");
        }
        return parameterMap.get(name);
    }
// constructor
    public ExecutionContext() {
        numberStack = new Stack<>();
        parameterMap = new HashMap<>();
    }
}
