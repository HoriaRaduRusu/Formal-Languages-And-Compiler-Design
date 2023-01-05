package org.example;

import java.util.List;

public class TableOutput {
    private final List<TableRow> tableResult;

    public TableOutput(List<TableRow> tableResult) {
        this.tableResult = tableResult;
    }

    @Override
    public String toString() {
        String tableFormat = "%15s%15s%15s%15s%n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(tableFormat, "Index", "Value", "Parent", "Right sibling"));
        for (int i = 0; i < tableResult.size(); i++) {
            TableRow tableRow = tableResult.get(i);
            stringBuilder.append(String.format(tableFormat, i, tableRow.getInfo(), tableRow.getParentIndex(), tableRow.getRightSiblingIndex()));
        }
        return stringBuilder.toString();
    }
}
