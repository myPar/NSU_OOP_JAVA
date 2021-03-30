package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Add extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        if (args.length != 0) {
            throw new CommandException(2, "No arguments should be in command 'Add'");
        }
        // context exception may be thrown
        double arg1 = context.popValue();
        double arg2 = context.popValue();

        context.pushValue(arg1 + arg2);
    }
}
