package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Sqrt extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        if (args.length != 0) {
            throw new CommandException(2, "No arguments should be in command 'Sqrt'");
        }
        // context exception may be thrown
        double arg = context.popValue();
        if (arg < 0) {
            // push value back
            context.pushValue(arg);
            throw new CommandException(2, "Command 'Sqrt': can't get a square root from negative value");
        }
        context.pushValue(Math.sqrt(arg));
    }
}
