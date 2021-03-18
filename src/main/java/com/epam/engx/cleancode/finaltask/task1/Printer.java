package com.epam.engx.cleancode.finaltask.task1;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.Command;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DatabaseManager;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.View;

public class Printer extends AbstractPrinter implements Command {

    private static final int TABLE_NAME_INDEX = 1;

    private View view;
    private DatabaseManager manager;

    public Printer(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void process(String input) {
        String[] commandAndParameters = getCommandAndParameters(input);
        validateParameters(commandAndParameters);

        view.write(getTableString(manager, commandAndParameters[TABLE_NAME_INDEX]));
    }
}
