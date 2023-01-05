package org.example;

public class TableConstructData {
    private final Node node;
    private final Integer parentIndex;
    private final boolean hasRightSibling;

    public TableConstructData(Node node, Integer parentIndex, boolean hasRightSibling) {
        this.node = node;
        this.parentIndex = parentIndex;
        this.hasRightSibling = hasRightSibling;
    }

    public Node getNode() {
        return node;
    }

    public Integer getParentIndex() {
        return parentIndex;
    }

    public boolean hasRightSibling() {
        return hasRightSibling;
    }
}
