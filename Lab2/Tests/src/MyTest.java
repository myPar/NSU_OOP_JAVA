import Calculator.Calculator;
import Command.Command;
import ExecutionContext.ExecutionContext;
import UserException.CalculatorException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MyTest {
    private static Calculator calculator;
    private static final int largeTest1Size = 100000;
    private static final int largeTest2Size = 1001;
    private static final int largeTest3Size = 1000000;

    private void nonFailedExecution(String commandName, ExecutionContext context, String[] args) {
        try {
            calculator.debugCommandExecution(commandName, context, args);
        }
        catch (CalculatorException e) {
            fail("command execution shouldn't be failed");
        }
    }
    private void failedExecution(String commandName, ExecutionContext context, String[] args,
                                 String failMessage, Class expectedExceptionClass)
    {
        // stack size before command execution
        int stStackSize = context.getStackSize();
        try {
            calculator.debugCommandExecution(commandName, context, args);
            fail(failMessage);
        }
        catch (CalculatorException e) {
            // check exception class
            assertEquals(expectedExceptionClass, e.getClass());
            // check that stack size was not changed
            assertEquals(stStackSize, context.getStackSize());
        }
    }
    @BeforeAll
    static void init() {
        calculator = new Calculator();
        try {
            Command.config();
        }
        catch (CalculatorException e) {
            e.printExceptionMessage();
            System.err.println("fatal error: factory configuration failed");
            System.exit(1);
        }
    }
    @Test
    @Order(1)
    @DisplayName("SimpleTest1")
    void addCommandTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];

        // check push command
        pushArgs[0] = "10";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(1, context.getStackSize());
        assertEquals(10, context.getValue(0));
        // check push command
        pushArgs[0] = "25.333";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(25.333, context.getValue(1));
        // check Add command
        nonFailedExecution("Add", context, new String[]{});
        assertEquals(1, context.getStackSize());
        assertEquals(35.333, context.getValue(0));
        // init exception class
        Class exceptionClass = Command.CommandException.class;

        // check exception: arguments existence
        failedExecution("Add", context, new String[]{"arg"},
                "Add execution should be failed: there is command argument", exceptionClass);
        // check exception: not enough elements in the stack
        failedExecution("Add", context, new String[]{},
                "'Add' command execution should be failed: stack size is not enough for command execution", exceptionClass);
    }
    @Test
    @Order(2)
    @DisplayName("SimpleTest2")
    void subCommandTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];

        // check push command
        pushArgs[0] = "39";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(1, context.getStackSize());
        assertEquals(39, context.getValue(0));
        // check push command
        pushArgs[0] = "150";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(150, context.getValue(1));
        // check Sub command
        nonFailedExecution("Sub", context, new String[]{});
        assertEquals(1, context.getStackSize());
        assertEquals(111, context.getValue(0));
        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: arguments existence
        failedExecution("Sub", context, new String[]{"arg"},
                "'Sub' execution should be failed: there is command argument", exceptionClass);
        // check exception: not enough elements in the stack
        failedExecution("Sub", context, new String[]{},
                "'Sub' execution should be failed: stack size is not enough for command execution", exceptionClass);
    }
    @Test
    @Order(3)
    @DisplayName("SimpleTest3")
    void MulCommandTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];

        // check push command
        pushArgs[0] = "100";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(1, context.getStackSize());
        assertEquals(100, context.getValue(0));
        // check push command
        pushArgs[0] = "-5";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(-5, context.getValue(1));
        // check Mul command
        nonFailedExecution("Mul", context, new String[]{});
        assertEquals(1, context.getStackSize());
        assertEquals(-500, context.getValue(0));
        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: arguments existence
        failedExecution("Mul", context, new String[]{"arg"},
                "'Mul' execution should be failed: there is command argument", exceptionClass);
        // check exception: not enough elements in the stack
        failedExecution("Mul", context, new String[]{}, "'Mul' execution should be failed: stack size is not enough for command execution", exceptionClass);
    }
    @Test
    @Order(4)
    @DisplayName("SimpleTest4")
    void DivCommandTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];
        // check push command
        pushArgs[0] = "100";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(1, context.getStackSize());
        assertEquals(100, context.getValue(0));
        // check push command
        pushArgs[0] = "25";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(25, context.getValue(1));
        // check Div command
        nonFailedExecution("Div", context, new String[]{});
        assertEquals(1, context.getStackSize());
        assertEquals(0.25, context.getValue(0));
        // init exception class
        Class exceptionClass = Command.CommandException.class;

        // check exception: arguments existence
        failedExecution("Div", context, new String[]{"arg"},
                "'Div' execution should be failed: there is command argument", exceptionClass);
        // check exception: not enough elements in the stack
        failedExecution("Div", context, new String[]{},
                "'Div' execution should be failed: stack size is not enough for command execution", exceptionClass);

        pushArgs[0] = "0";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(0, context.getValue(1));

        pushArgs[0] = "2355.4";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(3, context.getStackSize());
        assertEquals(2355.4, context.getValue(2));

        // check exception: division by zero
        failedExecution("Div", context, new String[]{},
                "'Div' execution should be failed: division by zero", exceptionClass);
    }
    @Test
    @Order(5)
    @DisplayName("SimpleTest5")
    void SqrtCommandTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];

        // check push command
        pushArgs[0] = "-5";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(1, context.getStackSize());
        assertEquals(-5, context.getValue(0));
        // check push command
        pushArgs[0] = "100";
        nonFailedExecution("Push", context, pushArgs);
        assertEquals(2, context.getStackSize());
        assertEquals(100, context.getValue(1));
        // check Sqrt command
        nonFailedExecution("Sqrt", context, new String[]{});
        assertEquals(2, context.getStackSize());
        assertEquals(10, context.getValue(1));

        nonFailedExecution("Pop", context, new String[]{});
        assertEquals(1, context.getStackSize());
        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: sqrt from negative value
        failedExecution("Sqrt", context, new String[]{},
                "'Sqrt' execution should be failed: sqrt from negative value", exceptionClass);
        // check exception: args consistence
        failedExecution("Sqrt", context, new String[]{"arg"},
                "'Sqrt' execution should be failed: there is command argument", exceptionClass);

        // change exception class
        exceptionClass = ExecutionContext.ContextException.class;
        // empty stack command execution
        nonFailedExecution("Pop", context, new String[]{});
        assertEquals(0, context.getStackSize());
        failedExecution("Sqrt", context, new String[]{},
                "'Sqrt' execution should be failed: stack is empty", exceptionClass);
    }
    @Test
    @Order(6)
    @DisplayName("SimpleTest6")
    void printCommandTest() {
        ExecutionContext context = new ExecutionContext();

        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: arg consistence
        failedExecution("Print", context, new String[]{"arg"},
                "'Print' execution should be failed: there is command argument", exceptionClass);
        // change exception class
        exceptionClass = ExecutionContext.ContextException.class;
        // check exception: empty stack command execution
        failedExecution("Print", context, new String[]{},
                "'Print' execution should be failed: the stack is empty", exceptionClass);
    }
    @Test
    @Order(7)
    @DisplayName("SimpleTest7")
    void defineCommandTest() {
        ExecutionContext context = new ExecutionContext();

        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: arg consistence
        failedExecution("Define", context, new String[]{"arg"},
                "'Define' execution should be failed: invalid command arguments count", exceptionClass);
        // check exception: invalid second arg
        failedExecution("Define", context, new String[]{"arg", "aa10"},
                "'Define' execution should be failed: second arg is not a number", exceptionClass);

        // check correct parameter definition
        nonFailedExecution("Define", context, new String[]{"arg", "10"});
        assertEquals(10, context.getParameterUnhandled("arg"));
        assertEquals(0, context.getStackSize());

        // change exception class
        exceptionClass = ExecutionContext.ContextException.class;
        // check exception: empty stack command execution
        failedExecution("Define", context, new String[]{"arg", "11"},
                "'Define' execution should be failed: parameter with such name already exist", exceptionClass);
    }
    @Test
    @Order(8)
    @DisplayName("SimpleTest8")
    void pushCommandTest() {
        ExecutionContext context = new ExecutionContext();
        // init exception class
        Class exceptionClass = Command.CommandException.class;
        // check exception: invalid args count
        failedExecution("Push", context, new String[]{},
                "'Push' execution should be failed: invalid args count", exceptionClass);

        // change exception class
        exceptionClass = ExecutionContext.ContextException.class;
        // check exception: no parameter with such name
        failedExecution("Push", context, new String[]{"arg"},
                "'Push' execution should be failed: no parameter with such name", exceptionClass);
        // define parameter
        nonFailedExecution("Define", context, new String[]{"arg", "99"});
        assertEquals(0, context.getStackSize());
        // check parameter push
        nonFailedExecution("Push", context, new String[]{"arg"});
        assertEquals(1, context.getStackSize());
        assertEquals(99, context.getValue(0));
    }
    @Test
    @Order(9)
    @DisplayName("LargeTest1")
    void popLargeTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = {"10"};
        String[] popArgs = {};
        // push data
        for (int i = 0; i < largeTest1Size; i++) {
            nonFailedExecution("Push", context, pushArgs);
            assertEquals(i + 1, context.getStackSize(), "'Push': exception on iteration: " + i);
            assertEquals(10, context.getValue(i), "'Push': exception on iteration: " + i);
        }
        assertEquals(largeTest1Size, context.getStackSize());
        // pop data
        for (int i = largeTest1Size - 1; i > 0; i--) {
            nonFailedExecution("Pop", context, popArgs);
            assertEquals(i, context.getStackSize(), "'Pop': exception on iteration: " + i);
            assertEquals(10, context.getValue(i - 1),"'Pop': exception on iteration: " + i);
        }
        // pop last element
        nonFailedExecution("Pop", context, popArgs);
        assertEquals(0, context.getStackSize());
    }
    @Test
    @Order(10)
    @DisplayName("LargeTest2")
    void sqrtLargeTest() {
        ExecutionContext context = new ExecutionContext();
        String[] pushArgs = new String[1];
        String[] emptyArgs = {};
        // push data
        for (int i = 0; i < largeTest2Size; i++) {
            pushArgs[0] = String.valueOf(i*i);
            nonFailedExecution("Push", context, pushArgs);
            assertEquals(i + 1, context.getStackSize(), "'Push': exception on iteration: " + i);
            assertEquals(i * i, context.getValue(i), "'Push': exception on iteration: " + i);
        }
        assertEquals(largeTest2Size, context.getStackSize());
        // pop data
        for (int i = largeTest2Size - 1; i > 0; i--) {
            nonFailedExecution("Sqrt",  context, emptyArgs);
            assertEquals(i, context.getValue(i), "'Sqrt': exception on iteration: " + i);
            nonFailedExecution("Pop", context, emptyArgs);
            assertEquals(i, context.getStackSize(), "'Pop': exception on iteration: " + i);
        }
        // pop last element
        nonFailedExecution("Sqrt", context, emptyArgs);
        assertEquals(0, context.getValue(0));
    }

    @ParameterizedTest
    @CsvSource({"Add,2.71828", "Sub,0.36788"})
    @Order(11)
    @DisplayName("CombinedLargeTest")
    void combinedLargeTest(String operation, double expectedResult) {
        ExecutionContext context = new ExecutionContext();
        String[] emptyArgs = new String[]{};
        String[] pushArgs = new String[1];
        // init first element
        nonFailedExecution("Push", context, new String[]{String.valueOf(1 + (double)1 / largeTest3Size)});

        // calculation of the exp: to test operations term calculated on each iteration
        for (int i = 0; i < largeTest3Size; i++) {
            pushArgs[0] = String.valueOf(1);
            nonFailedExecution("Push", context, pushArgs);

            pushArgs[0] = String.valueOf(largeTest3Size);
            nonFailedExecution("Push", context, pushArgs);

            pushArgs[0] = String.valueOf(1);
            nonFailedExecution("Push", context, pushArgs);

            nonFailedExecution("Div", context, emptyArgs);
            nonFailedExecution(operation, context, emptyArgs);
            nonFailedExecution("Mul", context, emptyArgs);
        }
        String result = String.format("%.5f", context.getValue(0));
        assertEquals(expectedResult, Double.valueOf(result));
    }
}
