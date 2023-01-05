package org.example;

public class TableRow {
   private final String info;
   private final Integer parentIndex;
   private final Integer rightSiblingIndex;

    public TableRow(String info, Integer parentIndex, Integer rightSiblingIndex) {
        this.info = info;
        this.parentIndex = parentIndex;
        this.rightSiblingIndex = rightSiblingIndex;
    }

    public String getInfo() {
        return info;
    }

    public Integer getParentIndex() {
        return parentIndex;
    }

    public Integer getRightSiblingIndex() {
        return rightSiblingIndex;
    }


    @Override
    public String toString() {
        return info + " parent: " + parentIndex + ", right sibling: " + rightSiblingIndex;
    }
}
