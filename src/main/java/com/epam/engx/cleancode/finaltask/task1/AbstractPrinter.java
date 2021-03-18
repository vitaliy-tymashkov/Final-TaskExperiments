package com.epam.engx.cleancode.finaltask.task1;

import static com.epam.engx.cleancode.finaltask.task1.PrintConstants.*;
import static com.epam.engx.cleancode.finaltask.task1.PrintConstants.LINE_BREAK;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DataSet;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DatabaseManager;

import java.util.List;
import java.util.Optional;


public abstract class AbstractPrinter {

    private static final String NOT_EXISTING_TABLE_TEMPLATE = "%s Table '%s' is empty or does not exist %s";
    private static final String EXCEPTION_TEXT_TEMPLATE = "incorrect number of parameters. Expected 1, but is %s";
    private static final int ALLOWED_PARAMETERS_VALUE = 2;
    private static final int FIRST_ELEMENT = 0;
    private static final int ADD_2 = 2;
    private static final int ADD_3 = 3;


    protected String getTableString(DatabaseManager manager, String tableName) {
        List<DataSet> data = manager.getTableData(tableName);
        return areNoColumns(getMaxColumnSize(data))
                ? getEmptyTable(tableName)
                : getHeaderOfTheTable(data) + getStringTableData(data);
    }

    protected String getEmptyTable(String tableName) {
        String textEmptyTable = String.format(NOT_EXISTING_TABLE_TEMPLATE, SYMBOL_VERTICAL_LINE, tableName, SYMBOL_VERTICAL_LINE);
        int max = getTableLength(textEmptyTable);

        StringBuilder result = new StringBuilder(SYMBOL_LEFT_TOP_ANGLE);
        fillWithSymbols(result, max, SYMBOL_HORIZONTAL_LINE);
        result.append(SYMBOL_RIGHT_TOP_ANGLE);
        result.append(LINE_BREAK);
        result.append(textEmptyTable);
        result.append(LINE_BREAK);
        result.append(SYMBOL_LEFT_BOTTOM_ANGLE);
        fillWithSymbols(result, max, SYMBOL_HORIZONTAL_LINE);
        result.append(SYMBOL_RIGHT_BOTTOM_ANGLE);
        result.append(LINE_BREAK);

        return result.toString();
    }

    protected int getMaxColumnSize(List<DataSet> dataSets) {
        int maxLength = 0;
        if (isNotDataSetEmpty(dataSets)) {
            maxLength = getMaxLengthFromHeader(dataSets, maxLength);
            maxLength = getMaxFromDataSet(dataSets, maxLength);
        }

        return maxLength;
    }

    protected boolean areNoColumns(int maxColumnSize) {
        return maxColumnSize == 0;
    }

    protected void validateParameters(String[] command) {
        if (isNotValidCommand(command)) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEXT_TEMPLATE, getActualParametersQuantity(command)));
        }
    }

    protected String[] getCommandAndParameters(String input) {
        return input.split(SYMBOL_SPACE);
    }

    protected String getHeaderOfTheTable(List<DataSet> dataSets) {
        StringBuilder result = new StringBuilder(SKIP_VERTICAL_LINE);
        int columnSize = getMaxColumnSize(dataSets);
        int columnCount = getColumnCount(dataSets);

        columnSize = correctMaxColumnSize(columnSize);
        getTopPart(result, columnSize, columnCount, SYMBOL_LEFT_TOP_ANGLE, SYMBOL_TOP_T, SYMBOL_RIGHT_TOP_ANGLE);
        List<String> columnNames = dataSets.get(FIRST_ELEMENT).getColumnNames();
        fillTableWithData(result, columnSize, columnCount, columnNames, SKIP_VERTICAL_LINE, SYMBOL_VERTICAL_LINE);
        result.append(SYMBOL_VERTICAL_LINE);
        result.append(LINE_BREAK);

        return isNotDataSetEmpty(dataSets)
                ? getTopPart(result, columnSize, columnCount, SYMBOL_LEFT_T, SYMBOL_CROSS, SYMBOL_RIGHT_T).toString()
                : getBottomPart(result, columnSize, columnCount).toString();
    }

    protected String getStringTableData(List<DataSet> dataSets) {
        StringBuilder result = new StringBuilder(SKIP_VERTICAL_LINE);
        int columnSize = correctMaxColumnSize(getMaxColumnSize(dataSets));
        int columnCount = getColumnCount(dataSets);
        int rowsCount = dataSets.size();

        for (int row = 0; row < rowsCount; row++) {
            List<Object> values = dataSets.get(row).getValues();
            result.append(SYMBOL_VERTICAL_LINE);
            fillTableWithData(result, columnSize, columnCount, values, SYMBOL_VERTICAL_LINE, SKIP_VERTICAL_LINE);
            getMiddlePart(result, rowsCount, columnSize, columnCount, row);
        }
        result = getBottomPart(result, columnSize, columnCount);

        return result.toString();
    }

    private void fillTableWithData(StringBuilder result, int maxColumnSize, int columnCount,
                                   List<? super String> values,
                                   String symbolVerticalLineFromTheRightSide,
                                   String symbolVerticalLineFromTheLeftSide) {
        for (int column = 0; column < columnCount; column++) {
            result.append(symbolVerticalLineFromTheLeftSide);
            int valuesLength = String.valueOf(values.get(column)).length();
            int max = getMaxColumnLength(maxColumnSize, valuesLength);
            if (isEvenColumnsSize(valuesLength)) {
                fillWithSymbols(result, max, SYMBOL_SPACE);
                result.append(values.get(column));
                fillWithSymbols(result, max, SYMBOL_SPACE);
                result.append(symbolVerticalLineFromTheRightSide);
            } else {
                fillWithSymbols(result, max, SYMBOL_SPACE);
                result.append(values.get(column));
                fillWithSymbols(result, (max + 1), SYMBOL_SPACE);
                result.append(symbolVerticalLineFromTheRightSide);
            }
        }
    }

    private int getTableLength(String textEmptyTable) {
        return (textEmptyTable.length() - 2);
    }

    private void fillWithSymbols(StringBuilder result, int max, String symbol) {
        for (int j = 0; j < max; j++) {
            result.append(symbol);
        }
    }

    private int getActualParametersQuantity(String[] command) {
        return (command.length - 1);
    }

    private boolean isNotValidCommand(String[] command) {
        return (command.length != ALLOWED_PARAMETERS_VALUE);
    }

    private int getMaxLengthFromHeader(List<DataSet> dataSets, int maxLength) {
        maxLength = checkAllElements(maxLength, dataSets.get(FIRST_ELEMENT).getColumnNames());
        return maxLength;
    }

    private int getMaxFromDataSet(List<DataSet> dataSets, int maxLength) {
        for (DataSet dataSet : dataSets) {
            maxLength = checkAllElements(maxLength, dataSet.getValues());
        }
        return maxLength;
    }

    private int checkAllElements(int max, List<? super String> values) {
        Optional<Integer> maxValue = values.stream().map(value -> String.valueOf(value).length()).max(Integer::compareTo);
        return pickMaxValue(max, maxValue.orElse(max));
    }

    private int pickMaxValue(int max, int currentMax) {
        return isMoreThanMax2(max, currentMax) ? currentMax : max;
    }

    private boolean isMoreThanMax2(int max, int max2) {
        return max2 > max;
    }

    private int correctMaxColumnSize(int size) {
        return isEvenColumnsSize(size)
                ? size + ADD_2
                : size + ADD_3;
    }

    private boolean isEvenColumnsSize(int size) {
        return (size % 2 == 0);
    }

    private void getMiddlePart(StringBuilder result, int rowsCount, int maxColumnSize, int columnCount, int row) {
        result.append(LINE_BREAK);
        if (checkRow(row, rowsCount)) {
            getTopPart(result, maxColumnSize, columnCount, SYMBOL_LEFT_T, SYMBOL_CROSS, SYMBOL_RIGHT_T);
        }
    }

    private boolean checkRow(int row, int rowsCount) {
        return row < (rowsCount - 1);
    }

    private StringBuilder getBottomPart(StringBuilder result, int maxColumnSize, int columnCount) {
        return getTopPart(result, maxColumnSize, columnCount, SYMBOL_LEFT_BOTTOM_ANGLE, SYMBOL_BOTTOM_T, SYMBOL_RIGHT_BOTTOM_ANGLE);
    }

    private StringBuilder getTopPart(StringBuilder result, int maxColumnSize, int columnCount, String symbolLeftAngle, String symbolT, String symbolRightAngle) {
        result.append(symbolLeftAngle);
        for (int j = 1; j < columnCount; j++) {
            fillWithSymbols(result, maxColumnSize, SYMBOL_HORIZONTAL_LINE);
            result.append(symbolT);
        }
        fillWithSymbols(result, maxColumnSize, SYMBOL_HORIZONTAL_LINE);
        result.append(symbolRightAngle);
        result.append(LINE_BREAK);
        return result;
    }

    private int getMaxColumnLength(int maxColumnSize, int valuesLength) {
        return (maxColumnSize - valuesLength) / 2;
    }

    private int getColumnCount(List<DataSet> dataSets) {
        return isNotDataSetEmpty(dataSets) ? dataSets.get(0).getColumnNames().size() : 0;
    }

    private boolean isNotDataSetEmpty(List<DataSet> dataSets) {
        return dataSets.size() > 0;
    }
}
