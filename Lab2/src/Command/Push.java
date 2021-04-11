package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Push extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        int len = args.length;
        // check args number
        if (len != 1) {
            throw new CommandException(2, "invalid number of arguments in command 'Push': " +
                                        len + ", but should be 1");
        }
        String parameterName = args[0];
        double value;
        // arg can be a double value or a parameter's name
        try {
            // check is parameter a double value
            value = Double.parseDouble(parameterName);
        }
        catch (NumberFormatException e) {
            // try to get parameter's value by name
            value = context.getParameter(parameterName);
        }
        // add parameter value to execution context's stack
        context.pushValue(value);
    }
}
