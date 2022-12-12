package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Item {
    private final String leftSide;
    private final List<String> beforeDot;
    private final List<String> afterDot;

    public Item(String leftSide, List<String> beforeDot, List<String> afterDot) {
        this.leftSide = leftSide;
        this.beforeDot = beforeDot;
        this.afterDot = afterDot;
    }

    public String getFirstAfterDot() {
        return afterDot.isEmpty() ? "" : afterDot.get(0);
    }

    public Item shiftLeft() {
        String firstAfterDot = getFirstAfterDot();
        if (firstAfterDot.isEmpty()) {
            return this;
        }
        List<String> shiftedBeforeDot = new ArrayList<>(beforeDot.stream().toList());
        shiftedBeforeDot.add(firstAfterDot);
        List<String> shiftedAfterDot = new ArrayList<>(afterDot.stream().skip(1).toList());
        return new Item(leftSide, shiftedBeforeDot, shiftedAfterDot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(leftSide, item.leftSide) && Objects.equals(beforeDot, item.beforeDot) && Objects.equals(afterDot, item.afterDot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftSide, beforeDot, afterDot);
    }

    @Override
    public String toString() {
        String beforeDotString = String.join(" ", beforeDot);
        String afterDotString = String.join(" ", afterDot);
        return "[" + leftSide + " -> " + beforeDotString + " . " + afterDotString + "]";
    }
}
