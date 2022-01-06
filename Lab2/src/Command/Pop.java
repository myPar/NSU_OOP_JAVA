package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Pop extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;

        if (args.length != 0) {
            throw new CommandException(2, "No arguments should be in command 'Pop'");
        }
        // pop the top value from the execution context's stack
        context.popValue();
    }
}
