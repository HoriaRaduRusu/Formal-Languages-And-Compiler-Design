package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Pif {

    private final List<Pair<String, Integer>> pifList = new ArrayList<>();

    public void addElement(Pair<String, Integer> element) {
        pifList.add(element);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("PIF:\n");
        pifList.stream()
                .map(pair -> pair.getL() + " - " + pair.getR() + "\n")
                .forEach(builder::append);
        return builder.toString();
    }

}
