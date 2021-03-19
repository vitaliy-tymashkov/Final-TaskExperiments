package com.epam.engx.cleancode.finaltask.task1;

import static com.epam.engx.cleancode.finaltask.task1.PrintSymbolsConstants.SYMBOL_SPACE;

public abstract class AbstractCommandAndParametersValidator {
    private static final int COMMAND_INDEX = 0;
    private static final String PRINT_COMMAND = "print";
    private static final String EXCEPTION_TEXT_TEMPLATE = "incorrect number of parameters. Expected 1, but is %s";

    private static PrinterPredicates checker = new PrinterPredicates();

    public boolean canProcess(String command) {
        return getCommandAndParameters(command)[COMMAND_INDEX].equals(PRINT_COMMAND);
    }

    protected String[] getCommandAndParameters(String input) {
        return input.split(SYMBOL_SPACE);
    }

    protected void validateParameters(String[] command) {
        if (checker.isNotValidCommand(command)) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEXT_TEMPLATE, getActualParametersQuantity(command)));
        }
    }

    private int getActualParametersQuantity(String[] command) {
        return (command.length - 1);
    }
}
