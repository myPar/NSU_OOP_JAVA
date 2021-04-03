package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Div extends  Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        if (args.length != 0) {
            throw new CommandException(2, "No arguments should be in command 'Div'");
        }
        // check stack size
        if (context.getStackSize() < 2) {
            throw new CommandException(2, "Command 'Div': not enough elements in the stack");
        }
        double arg1 = context.popValue();
        double arg2 = context.popValue();

        context.pushValue(arg1 / arg2);
    }
}
