package Command;

import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;

public class Print extends Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        assert context != null;
        assert args != null;
        // check args count
        if (args.length != 0) {
            throw new CommandException(2, "No arguments should be in command 'Print'");
        }
        // print the top element from the execution context's stack
        System.out.println(context.peekValue());
    }
}
