package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Define extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        int len = args.length;
        // command has two arguments
        if (len != 2) {
            throw new CommandException(2, "invalid arguments count in command 'Define': " +
                                        len + ", but should be: 2");
        }
        double value;

        // try convert string parameter's value to double
        try {
            value = Double.parseDouble(args[1]);
        }
        catch (NumberFormatException e) {
            throw new CommandException(2, "invalid parameter value in command 'Define': " +
                                        args[1] + ", but should be a double value");
        }
        // add new parameter to execution context
        context.addParameter(args[0], value);
    }
}
