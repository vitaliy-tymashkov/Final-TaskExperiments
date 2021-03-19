package com.epam.engx.cleancode.finaltask.task1;

import static com.epam.engx.cleancode.finaltask.task1.PrintSymbolsConstants.*;
import static com.epam.engx.cleancode.finaltask.task1.PrintSymbolsConstants.LINE_BREAK;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DataSet;
import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DatabaseManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractPrinter extends AbstractCommandAndParametersValidator {
    private static final String NOT_EXISTING_TABLE_TEMPLATE = "%s Table '%s' is empty or does not exist %s";

    private static final int FIRST_ELEMENT = 0;
    private static final int NO_COLUMNS = 0;
    private static final int ALIGN_FOR_EVEN = 2;
    private static final int ALIGN_FOR_ODD = 3;

    private static PrinterPredicates checker = new PrinterPredicates();

    protected String getTableString(DatabaseManager manager, String tableName) {
        List<DataSet> data = manager.getTableData(tableName);

        return checker.isEmptyTable(getMaxColumnSize(data))
                ? getEmptyTable(tableName)
                : getHeaderOfTheTable(data) + getStringTableData(data);
    }

    private String getEmptyTable(String tableName) {
        StringBuilder result = new StringBuilder();
        String textEmptyTable = String.format(NOT_EXISTING_TABLE_TEMPLATE, SYMBOL_VERTICAL_LINE, tableName, SYMBOL_VERTICAL_LINE);
        int tableLength = getTableLength(textEmptyTable);

        appendSymbols(result, SYMBOL_LEFT_TOP_ANGLE);
        fillWithSymbols(result, tableLength, SYMBOL_HORIZONTAL_LINE);
        appendSymbols(result, SYMBOL_RIGHT_TOP_ANGLE, LINE_BREAK, textEmptyTable, LINE_BREAK, SYMBOL_LEFT_BOTTOM_ANGLE);
        fillWithSymbols(result, tableLength, SYMBOL_HORIZONTAL_LINE);
        appendSymbols(result, SYMBOL_RIGHT_BOTTOM_ANGLE, LINE_BREAK);

        return result.toString();
    }

    private String getHeaderOfTheTable(List<DataSet> dataSets) {
        StringBuilder result = new StringBuilder(SKIP_VERTICAL_LINE);
        int columnCount = getColumnCount(dataSets);
        int columnSize = getMaxColumnSize(dataSets);
        columnSize = fillHeaderWithColumnNames(dataSets, result, columnSize, columnCount);

        return checker.isNotEmptyDataSet(dataSets)
                ? getPartOfTable(result, columnSize, columnCount, SYMBOL_LEFT_T, SYMBOL_CROSS, SYMBOL_RIGHT_T).toString()
                : getBottomPart(result, columnSize, columnCount).toString();
    }

    private int fillHeaderWithColumnNames(List<DataSet> dataSets, StringBuilder result, int columnSize, int columnCount) {
        columnSize = correctMaxColumnSize(columnSize);

        getPartOfTable(result, columnSize, columnCount, SYMBOL_LEFT_TOP_ANGLE, SYMBOL_TOP_T, SYMBOL_RIGHT_TOP_ANGLE);
        List<String> columnNames = dataSets.get(FIRST_ELEMENT).getColumnNames();
        fillTableWithData(result, columnSize, columnCount, columnNames, SYMBOL_VERTICAL_LINE, SKIP_VERTICAL_LINE);
        appendSymbols(result, SYMBOL_VERTICAL_LINE, LINE_BREAK);

        return columnSize;
    }

    private void appendSymbols(StringBuilder result, String... symbols) {
        result.append(Arrays.stream(symbols).collect(Collectors.joining()));
    }

    private String getStringTableData(List<DataSet> dataSets) {
        StringBuilder result = new StringBuilder(SKIP_VERTICAL_LINE);
        int columnSize = correctMaxColumnSize(getMaxColumnSize(dataSets));
        int columnCount = getColumnCount(dataSets);
        int rowsCount = dataSets.size();

        fillTableWithValues(result, dataSets, columnSize, columnCount, rowsCount);
        getBottomPart(result, columnSize, columnCount);

        return result.toString();
    }

    private void fillTableWithValues(StringBuilder result, List<DataSet> dataSets,
                                     int columnSize, int columnCount, int rowsCount) {
        for (int i = 0; i < rowsCount; i++) {
            appendSymbols(result, SYMBOL_VERTICAL_LINE);
            List<Object> values = dataSets.get(i).getValues();
            fillTableWithData(result, columnSize, columnCount, values, SKIP_VERTICAL_LINE, SYMBOL_VERTICAL_LINE);
            getMiddlePart(result, rowsCount, columnSize, columnCount, i);
        }
    }

    private void fillTableWithData(StringBuilder result, int maxColumnSize, int columnCount,
                                   List<? super String> values,
                                   String symbolVerticalLineFromTheLeftSide,
                                   String symbolVerticalLineFromTheRightSide) {
        for (int column = 0; column < columnCount; column++) {
            result.append(symbolVerticalLineFromTheLeftSide);
            int valuesLength = String.valueOf(values.get(column)).length();
            int max = getMaxColumnLength(maxColumnSize, valuesLength);
            fillCellDependingOnSize(result, values, symbolVerticalLineFromTheRightSide, column, valuesLength, max);
        }
    }

    private void fillCellDependingOnSize(StringBuilder result, List<? super String> values,
                                         String symbolVerticalLineFromTheRightSide, int column, int valuesLength, int length) {
        if (checker.isEven(valuesLength)) {
            fillCell(result, values, symbolVerticalLineFromTheRightSide, column, length, length);
        } else {
            fillCell(result, values, symbolVerticalLineFromTheRightSide, column, length, getLengthForLastCell(length));
        }
    }

    private int getLengthForLastCell(int max) {
        return max + 1;
    }

    private void fillCell(StringBuilder result, List<? super String> values, String symbolVerticalLineFromTheRightSide,
                          int column, int firstCells, int lastCell) {
        fillWithSymbols(result, firstCells, SYMBOL_SPACE);
        result.append(values.get(column));
        fillWithSymbols(result, lastCell, SYMBOL_SPACE);
        appendSymbols(result, symbolVerticalLineFromTheRightSide);
    }

    private void fillWithSymbols(StringBuilder result, int max, String symbol) {
        for (int j = 0; j < max; j++) {
            result.append(symbol);
        }
    }

    private void getMiddlePart(StringBuilder result, int rowsCount, int maxColumnSize, int columnCount, int row) {
        appendSymbols(result, LINE_BREAK);
        if (checker.isNotLastRow(row, rowsCount)) {
            getPartOfTable(result, maxColumnSize, columnCount, SYMBOL_LEFT_T, SYMBOL_CROSS, SYMBOL_RIGHT_T);
        }
    }

    private void getChunkWithTsymbol(StringBuilder result, int maxColumnSize, int columnCount, String symbolT) {
        for (int j = 1; j < columnCount; j++) {
            fillWithSymbols(result, maxColumnSize, SYMBOL_HORIZONTAL_LINE);
            appendSymbols(result, symbolT);
        }
    }

    private StringBuilder getBottomPart(StringBuilder result, int maxColumnSize, int columnCount) {
        return getPartOfTable(result, maxColumnSize, columnCount,
                SYMBOL_LEFT_BOTTOM_ANGLE, SYMBOL_BOTTOM_T, SYMBOL_RIGHT_BOTTOM_ANGLE);
    }

    private StringBuilder getPartOfTable(StringBuilder result, int maxColumnSize, int columnCount,
                                         String symbolLeftAngle,
                                         String symbolT,
                                         String symbolRightAngle) {
        appendSymbols(result, symbolLeftAngle);
        getChunkWithTsymbol(result, maxColumnSize, columnCount, symbolT);
        fillWithSymbols(result, maxColumnSize, SYMBOL_HORIZONTAL_LINE);
        appendSymbols(result, symbolRightAngle, LINE_BREAK);

        return result;
    }

    private int getMaxColumnSize(List<DataSet> dataSets) {
        int maxLength = 0;
        if (checker.isNotEmptyDataSet(dataSets)) {
            maxLength = getMaxLengthFromHeader(dataSets, maxLength);
            maxLength = getMaxFromDataSet(dataSets, maxLength);
        }

        return maxLength;
    }

    private int getTableLength(String textEmptyTable) {
        return (textEmptyTable.length() - 2);
    }

    private int getMaxLengthFromHeader(List<DataSet> dataSets, int maxLength) {
        return checkAllElements(maxLength, dataSets.get(FIRST_ELEMENT).getColumnNames());
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
        return currentMax > max ? currentMax : max;
    }

    private int correctMaxColumnSize(int size) {
        return checker.isEven(size)
                ? size + ALIGN_FOR_EVEN
                : size + ALIGN_FOR_ODD;
    }

    private int getMaxColumnLength(int maxColumnSize, int valuesLength) {
        return (maxColumnSize - valuesLength) / 2;
    }

    private int getColumnCount(List<DataSet> dataSets) {
        return checker.isNotEmptyDataSet(dataSets) ? dataSets.get(FIRST_ELEMENT).getColumnNames().size() : NO_COLUMNS;
    }
}
