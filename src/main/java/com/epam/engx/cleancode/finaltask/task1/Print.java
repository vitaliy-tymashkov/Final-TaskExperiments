package com.epam.engx.cleancode.finaltask.task1;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.Command;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DataSet;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DatabaseManager;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.View;

import java.util.List;

import static com.epam.engx.cleancode.finaltask.task1.PrintConstants.*;

public class Print implements Command {

    private static final int COMMAND_INDEX = 1;
    private static final String PRINT_COMMAND = "print ";
    private static final int ALLOWED_PARAMETERS_VALUE = 2;
    private static final String EXCEPTION_TEXT_TEMPLATE = "incorrect number of parameters. Expected 1, but is %s";

    private View view;
    private DatabaseManager manager;
    private String tableName;

    public Print(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public boolean canProcess(String command) {
        return command.startsWith(PRINT_COMMAND);
    }

    public void process(String input) {
        String[] command = input.split(SYMBOL_SPACE);
        validateParametersQuantity(command);
        tableName = command[COMMAND_INDEX];
        List<DataSet> data = manager.getTableData(tableName);
        view.write(getTableString(data));
    }

    private void validateParametersQuantity(String[] command) {
        if (isNotValidCommand(command)) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEXT_TEMPLATE, getActualParametersQuantity(command)));
        }
    }

    private int getActualParametersQuantity(String[] command) {
        return command.length - 1;
    }

    private boolean isNotValidCommand(String[] command) {
        return command.length != ALLOWED_PARAMETERS_VALUE;
    }

    private String getTableString(List<DataSet> data) {
        int maxColumnSize;
        maxColumnSize = getMaxColumnSize(data);
        if (maxColumnSize == 0) {
            return getEmptyTable(tableName);
        } else {
            return getHeaderOfTheTable(data) + getStringTableData(data);
        }
    }

    private String getEmptyTable(String tableName) {
        String textEmptyTable = SYMBOL_VERTICAL_LINE + " Table '" + tableName + "' is empty or does not exist " + SYMBOL_VERTICAL_LINE;
        String result = SYMBOL_LEFT_TOP_ANGLE;

        for (int i = 0; i < textEmptyTable.length() - 2; i++) {
            result += SYMBOL_HORIZONTAL_LINE;
        }
        result += SYMBOL_RIGHT_TOP_ANGLE + "\n";
        result += textEmptyTable + "\n";
        result += SYMBOL_LEFT_BOTTOM_ANGLE;
        for (int i = 0; i < textEmptyTable.length() - 2; i++) {
            result += SYMBOL_HORIZONTAL_LINE;
        }
        result += SYMBOL_RIGHT_BOTTOM_ANGLE + "\n";

        return result;
    }

    private int getMaxColumnSize(List<DataSet> dataSets) {
        int maxLength = 0;
        if (dataSets.size() > 0) {
            List<String> columnNames = dataSets.get(0).getColumnNames();
            for (String columnName : columnNames) {
                if (columnName.length() > maxLength) {
                    maxLength = columnName.length();
                }
            }
            for (DataSet dataSet : dataSets) {
                List<Object> values = dataSet.getValues();
                for (Object value : values) {
//                    if (value instanceof String)
                    if (String.valueOf(value).length() > maxLength) {
                        maxLength = String.valueOf(value).length();
                    }
                }
            }
        }

        return maxLength;
    }

    private String getStringTableData(List<DataSet> dataSets) {
        int rowsCount;
        rowsCount = dataSets.size();
        int maxColumnSize = getMaxColumnSize(dataSets);
        String result = "";

        if (maxColumnSize % 2 == 0) {
            maxColumnSize += 2;
        } else {
            maxColumnSize += 3;
        }
        int columnCount = getColumnCount(dataSets);

        for (int row = 0; row < rowsCount; row++) {
            List<Object> values = dataSets.get(row).getValues();
            result += SYMBOL_VERTICAL_LINE;
            for (int column = 0; column < columnCount; column++) {
                int valuesLength = String.valueOf(values.get(column)).length();
                if (valuesLength % 2 == 0) {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += SYMBOL_SPACE;
                    }
                    result += String.valueOf(values.get(column));
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += SYMBOL_SPACE;
                    }
                    result += SYMBOL_VERTICAL_LINE;
                } else {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += SYMBOL_SPACE;
                    }
                    result += String.valueOf(values.get(column));
                    for (int j = 0; j <= (maxColumnSize - valuesLength) / 2; j++) {
                        result += SYMBOL_SPACE;
                    }
                    result += SYMBOL_VERTICAL_LINE;
                }
            }
            result += "\n";
            if (row < rowsCount - 1) {
                result += SYMBOL_LEFT_T;
                for (int j = 1; j < columnCount; j++) {
                    for (int i = 0; i < maxColumnSize; i++) {
                        result += SYMBOL_HORIZONTAL_LINE;
                    }
                    result += SYMBOL_CROSS;
                }
                for (int i = 0; i < maxColumnSize; i++) {
                    result += SYMBOL_HORIZONTAL_LINE;
                }
                result += SYMBOL_RIGHT_T + "\n";
            }
        }
        result += SYMBOL_LEFT_BOTTOM_ANGLE;
        for (int j = 1; j < columnCount; j++) {
            for (int i = 0; i < maxColumnSize; i++) {
                result += SYMBOL_HORIZONTAL_LINE;
            }
            result += SYMBOL_BOTTOM_T;
        }
        for (int i = 0; i < maxColumnSize; i++) {
            result += SYMBOL_HORIZONTAL_LINE;
        }
        result += SYMBOL_RIGHT_BOTTOM_ANGLE + "\n";
        return result;
    }

    private int getColumnCount(List<DataSet> dataSets) {
        int result = 0;
        if (dataSets.size() > 0) {
            return dataSets.get(0).getColumnNames().size();
        }
        return result;
    }

    private String getHeaderOfTheTable(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        String result = "";
        int columnCount = getColumnCount(dataSets);

        if (maxColumnSize % 2 == 0) {
            maxColumnSize += 2;
        } else {
            maxColumnSize += 3;
        }
        result += SYMBOL_LEFT_TOP_ANGLE;
        for (int j = 1; j < columnCount; j++) {
            for (int i = 0; i < maxColumnSize; i++) {
                result += SYMBOL_HORIZONTAL_LINE;
            }
            result += SYMBOL_TOP_T;
        }
        for (int i = 0; i < maxColumnSize; i++) {
            result += SYMBOL_HORIZONTAL_LINE;
        }
        result += SYMBOL_RIGHT_TOP_ANGLE + "\n";

        List<String> columnNames = dataSets.get(0).getColumnNames();
        for (int column = 0; column < columnCount; column++) {
            result += SYMBOL_VERTICAL_LINE;
            int columnNamesLength = columnNames.get(column).length();
            if (columnNamesLength % 2 == 0) {
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += SYMBOL_SPACE;
                }
                result += columnNames.get(column);
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += SYMBOL_SPACE;
                }
            } else {
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += SYMBOL_SPACE;
                }
                result += columnNames.get(column);
                for (int j = 0; j <= (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += SYMBOL_SPACE;
                }
            }
        }
        result += SYMBOL_VERTICAL_LINE + "\n";

        //last string of the header
        if (dataSets.size() > 0) {
            result += SYMBOL_LEFT_T;
            for (int j = 1; j < columnCount; j++) {
                for (int i = 0; i < maxColumnSize; i++) {
                    result += SYMBOL_HORIZONTAL_LINE;
                }
                result += SYMBOL_CROSS;
            }
            for (int i = 0; i < maxColumnSize; i++) {
                result += SYMBOL_HORIZONTAL_LINE;
            }
            result += SYMBOL_RIGHT_T + "\n";
        } else {
            result += SYMBOL_LEFT_BOTTOM_ANGLE;
            for (int j = 1; j < columnCount; j++) {
                for (int i = 0; i < maxColumnSize; i++) {
                    result += SYMBOL_HORIZONTAL_LINE;
                }
                result += SYMBOL_BOTTOM_T;
            }
            for (int i = 0; i < maxColumnSize; i++) {
                result += SYMBOL_HORIZONTAL_LINE;
            }
            result += SYMBOL_RIGHT_BOTTOM_ANGLE + "\n";
        }
        return result;
    }
}
