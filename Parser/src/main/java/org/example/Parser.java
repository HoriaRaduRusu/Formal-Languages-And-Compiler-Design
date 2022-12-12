package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Parser {

    public Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    public Set<Item> closure(Set<Item> argument) {
        Set<Item> result ;
        Set<Item> currentResult = new HashSet<>(argument);
        do {
            result = new HashSet<>(currentResult);
            for (Item item: currentResult) {
                String firstAfterDot = item.getFirstAfterDot();
                if (grammar.isNonTerminal(firstAfterDot)) {
                    List<List<String>> productionsStartingWithFirstAfterDot = grammar.getProductionsForNonTerminal(firstAfterDot);
                    productionsStartingWithFirstAfterDot.stream()
                            .map(rightSide -> new Item(firstAfterDot, new ArrayList<>(), rightSide))
                            .forEach(currentResult::add);
                }
            }
        } while (currentResult.size() != result.size());
        return result;
    }

    public Set<Item> goTo(Set<Item> state, String symbol) {
        Set<Item> closureArgument = state.stream()
                .filter(item -> item.getFirstAfterDot().equals(symbol))
                .map(Item::shiftLeft)
                .collect(Collectors.toSet());
        return closure(closureArgument);
    }

    public Set<Set<Item>> getCanonicalCollection() {
        Set<Set<Item>> canonicalCollection;
        Set<Set<Item>> currentCanonicalCollection = new HashSet<>();
        Set<Item> firstArgument = new HashSet<>();
        firstArgument.add(new Item("S'", new ArrayList<>(), new ArrayList<>(List.of(grammar.getStartingSymbol()))));
        Set<Item> s0 = closure(firstArgument);
        currentCanonicalCollection.add(s0);
        do {
            canonicalCollection = new HashSet<>(currentCanonicalCollection);
            for (Set<Item> state : canonicalCollection) {
                Set<String> firstSymbolsAfterDot = state.stream()
                        .map(Item::getFirstAfterDot)
                        .filter(symbol -> !symbol.isEmpty())
                        .collect(Collectors.toSet());
                for (String symbol : firstSymbolsAfterDot) {
                    Set<Item> goToResult = goTo(state, symbol);
                    if (!goToResult.isEmpty()){
                        currentCanonicalCollection.add(goToResult);
                    }
                }
            }
        } while (currentCanonicalCollection.size() != canonicalCollection.size());
        return canonicalCollection;
    }
}
