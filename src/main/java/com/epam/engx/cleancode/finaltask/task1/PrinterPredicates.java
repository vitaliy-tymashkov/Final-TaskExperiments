package com.epam.engx.cleancode.finaltask.task1;

import com.epam.engx.cleancode.finaltask.task1.thirdpartyjar.DataSet;

import java.util.List;

public class PrinterPredicates {
    private static final int ALLOWED_PARAMETERS_VALUE = 2;
    private static final int EMPTY_DATASET = 0;
    public static final int EMPTY = 0;


    protected boolean areNoColumns(int maxColumnSize) {
        return maxColumnSize == EMPTY;
    }

    protected boolean isNotValidCommand(String[] command) {
        return (command.length != ALLOWED_PARAMETERS_VALUE);
    }

    protected boolean isMoreThanMax2(int max, int max2) {
        return max2 > max;
    }

    protected boolean checkRow(int row, int rowsCount) {
        return row < (rowsCount - 1);
    }

    protected boolean isEvenColumnsSize(int size) {
        return (size % 2 == 0);
    }

    protected boolean isNotEmptyDataSet(List<DataSet> dataSets) {
        return dataSets.size() > EMPTY_DATASET;
    }
}
