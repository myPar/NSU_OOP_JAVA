package ExecutionContext;

import UserException.CalculatorException;

import java.util.HashMap;
import java.util.Stack;

public class ExecutionContext {
// exception class
    public static class ContextException extends CalculatorException {
        private static String[] types;
        static {
            types = new String[2];
            types[0] = "stack operation";
            types[1] = "map operation";
        }
        // exception constructor
        public ContextException(int t, String m) {
            assert(t >= 0 && t < types.length);
            message = m;
            typeValue = types[t];
        }
    }
// fields
    // stack of numbers
    private Stack<Double> numberStack;
    // map: key - parameter name; value - parameter value
    private HashMap<String, Double> parameterMap;
// methods
    // get stack's size
    public int getStackSize() { return numberStack.size(); }
    // peek value from the stack
    public double peekValue() throws ContextException {
        if (numberStack.isEmpty()) {
            throw new ContextException(0, "can't peek the value from empty stack");
        }
        return numberStack.peek();
    }
    // push value to the Stack
    public void pushValue(Double value) {
        numberStack.push(value);
    }
    // pop value from stack
    public double popValue() throws ContextException {
        if (numberStack.isEmpty()) {
            throw new ContextException(0, "can't pop the value from empty stack");
        }
        return numberStack.pop();
    }
    // add new parameter to map
    public void addParameter(String name, Double value) throws ContextException {
        // check consistence of parameter with such name
        if (parameterMap.containsKey(name)) {
            throw new ContextException(1, "can't add the variable; the variable with such name: '" + name + "' is already consist");
        }
        // add new parameter to the map
        parameterMap.put(name, value);
    }
    // get value of the parameter with such name
    public double getParameter(String name) throws ContextException {
        if (!parameterMap.containsKey(name)) {
            throw new ContextException(1, "can't get the variable; no variable with such name: " + "'" + name + "'");
        }
        return parameterMap.get(name);
    }
// constructor
    public ExecutionContext() {
        numberStack = new Stack<>();
        parameterMap = new HashMap<>();
    }
// methods for testers
    // get value from the stack by idx
    public double getValue(int idx) {
        assert idx >= 0 && idx < numberStack.size();
        return numberStack.get(idx);
    }
    // get parameter value from the map by name
    public double getParameterUnhandled(String name) {
        assert parameterMap.containsKey(name);
        return parameterMap.get(name);
    }
}
