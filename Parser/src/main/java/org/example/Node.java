package org.example;

import java.util.List;

public class Node {
    private String symbol;
    private Node parent;
    private List<Node> children;

    public Node(String symbol, Node parent, List<Node> children) {
        this.symbol = symbol;
        this.parent = parent;
        this.children = children;
    }

    public String getSymbol() {
        return symbol;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        children.add(node);
    }
}
