package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

// LR(0)
public class Grammar {

    private Set<String> nonTerminalSet;
    private Set<String> terminalSet;
    private Map<List<String>, List<List<String>>> productions;
    private String startingSymbol;

    public Grammar() {
        nonTerminalSet = new HashSet<>();
        terminalSet = new HashSet<>();
        productions = new HashMap<>();
    }

    public void loadFromFile(String file) throws IOException {
        try (InputStream inputStream = Grammar.class.getClassLoader().getResourceAsStream(file)) {
            Scanner scanner = new Scanner(inputStream);
            nonTerminalSet = Arrays.stream(scanner.nextLine().strip().split(" ")).collect(Collectors.toSet());
            terminalSet = Arrays.stream(scanner.nextLine().strip().split(" ")).collect(Collectors.toSet());
            startingSymbol = scanner.nextLine().strip();
            productions = new HashMap<>();
            while (scanner.hasNextLine()){
                String production = scanner.nextLine();
                String[] productionSides = production.split(" -> ");
                String leftSide = productionSides[0];
                String rightSide = productionSides[1];
                List<String> leftSideProduction = Arrays.stream(leftSide.split(" ")).toList();
                List<String> rightSideProduction = Arrays.stream(rightSide.split(" ")).toList();
                productions.putIfAbsent(leftSideProduction, new ArrayList<>());
                productions.get(leftSideProduction).add(rightSideProduction);
            }
        }
    }

    public String getNonTerminalSetForPrinting() {
        return String.join(", ", nonTerminalSet);
    }

    public String getTerminalSetForPrinting() {
        return String.join(", ", terminalSet);
    }

    public String getProductionSetForPrinting() {
        return productions.entrySet().stream()
                .map(entry -> {
                    String leftSide = String.join(" ", entry.getKey());
                    return entry.getValue().stream()
                            .map(rightSide -> leftSide + " -> " + String.join(" ", rightSide))
                            .collect(Collectors.joining("\n"));
                }).collect(Collectors.joining("\n"));
    }

    public String getProductionsForNonTerminalForPrinting(String nonTerminal) {
        List<List<String>> rightSides = productions.getOrDefault(List.of(nonTerminal), List.of());
        return rightSides.stream()
                .map(rightSide -> nonTerminal + " -> " + String.join(" ", rightSide))
                .collect(Collectors.joining("\n"));
    }

    public boolean checkIfCFG() {
        return productions.keySet().stream().allMatch(leftSide -> leftSide.size() == 1);
    }
}
