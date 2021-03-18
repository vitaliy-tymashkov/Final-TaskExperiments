package com.epam.engx.cleancode.finaltask.task1;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.Command;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DatabaseManager;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.View;

public class Printer extends AbstractPrinter implements Command {

    private static final int COMMAND_INDEX = 0;
    private static final int TABLE_NAME_INDEX = 1;
    private static final String PRINT_COMMAND = "print";

    private View view;
    private DatabaseManager manager;
    private String tableName;

    public Printer(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public boolean canProcess(String command) {
        return getCommandAndParameters(command)[COMMAND_INDEX].equals(PRINT_COMMAND);

    }

    public void process(String input) {
        String[] commandAndParameters = getCommandAndParameters(input);
        validateParameters(commandAndParameters);

        view.write(getTableString(manager, commandAndParameters[TABLE_NAME_INDEX]));
    }
}
