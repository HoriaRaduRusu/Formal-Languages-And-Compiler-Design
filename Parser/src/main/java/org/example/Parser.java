package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    private static final String ACTION_FIELD = "ACTION";
    private static final String REDUCE_ACTION = "reduce";
    private static final String SHIFT_ACTION = "shift";
    private static final String ERROR = "error";
    private static final String ACCEPT_ACTION = "acc";
    private static final String SUCCESS = "success";
    private final Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    public Set<Item> closure(Set<Item> argument) {
        Set<Item> result ;
        Set<Item> currentResult = new HashSet<>(argument);
        do {
            result = new HashSet<>(currentResult);
            for (Item item: result) {
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

    public Map<String, Set<Item>> getCanonicalCollection() {
        Map<String, Set<Item>> canonicalCollectionMap = new HashMap<>();
        int currentIndex = 1;
        Set<Set<Item>> canonicalCollection;
        Set<Set<Item>> currentCanonicalCollection = new HashSet<>();
        Set<Item> firstArgument = new HashSet<>();
        firstArgument.add(new Item("S'", new ArrayList<>(), new ArrayList<>(List.of(grammar.getStartingSymbol()))));
        Set<Item> s0 = closure(firstArgument);
        canonicalCollectionMap.put("s0", s0);
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
                    if (!goToResult.isEmpty() && !currentCanonicalCollection.contains(goToResult)){
                        currentCanonicalCollection.add(goToResult);
                        canonicalCollectionMap.put("s" + currentIndex, goToResult);
                        currentIndex += 1;
                    }
                }
            }
        } while (currentCanonicalCollection.size() != canonicalCollection.size());
        return canonicalCollectionMap;
    }

    public Map<String, Map<String, String>> createTable() {
        Map<String, Map<String, String>> table = new HashMap<>();
        Map<String, Set<Item>> canonicalCollection = getCanonicalCollection();
        for (String state: canonicalCollection.keySet()) {
            Set<Item> stateSet = canonicalCollection.get(state);
            table.put(state, new HashMap<>());
            for (Item item : stateSet) {
                String action = determineAction(item);
                String currentAction = table.get(state).get(ACTION_FIELD);
                if (currentAction != null) {
                    if (currentAction.startsWith(REDUCE_ACTION) && action.equals(SHIFT_ACTION)) {
                        throw new RuntimeException("shift - reduce conflict in table at state " + state + ": " + stateSet);
                    } else if (action.startsWith(REDUCE_ACTION) && currentAction.equals(SHIFT_ACTION)) {
                        throw new RuntimeException("shift - reduce conflict in table at state " + state + ": " + stateSet);
                    } else if (action.startsWith(REDUCE_ACTION) && currentAction.startsWith(REDUCE_ACTION) && !action.equals(currentAction)) {
                        throw new RuntimeException("reduce - reduce conflict in table at state " + state + ": " + stateSet);
                    }
                }
                if (!action.equals(ERROR)) {
                    table.get(state).put(ACTION_FIELD, action);
                }
            }
            for (String terminal: grammar.getTerminalSet()) {
                table.get(state).put(terminal, determineGoTo(canonicalCollection.get(state), terminal, canonicalCollection));
            }
            for (String nonTerminal: grammar.getNonTerminalSet()) {
                table.get(state).put(nonTerminal, determineGoTo(canonicalCollection.get(state), nonTerminal, canonicalCollection));
            }
        }
        return table;
    }

    public ParserOutput parse(List<String> sequence) {
        Stack<String> workingStack = new Stack<>();
        Stack<String> inputStack = new Stack<>();
        Stack<Integer> outputStack = new Stack<>();
        String result = SUCCESS;
        Map<String, Map<String, String>> table = createTable();
        String state = "s0";
        boolean end = false;

        workingStack.push("$");
        workingStack.push("s0");
        inputStack.push("$");
        for (int i = sequence.size() - 1; i >= 0; i--) {
            inputStack.push(sequence.get(i));
        }

        do {
            String action = table.get(state).get(ACTION_FIELD);
            if (action.equals(SHIFT_ACTION)) {
                shift(workingStack, inputStack, table);
            } else if (action.startsWith(REDUCE_ACTION)) {
                int productionNumber = Integer.parseInt(action.split(" ")[1]);
                reduce(workingStack, outputStack, productionNumber, table);
            } else if (action.equals(ACCEPT_ACTION)) {
                end = true;
            } else {
                result = ERROR;
                end = true;
            }
            state = workingStack.peek();
        } while (!end);

        return new ParserOutput(grammar, outputStack, result);

    }

    private void shift(Stack<String> workingStack, Stack<String> inputStack, Map<String, Map<String, String>> table) {
        String inputStackHead = inputStack.pop();
        String workingStackHead = workingStack.peek();
        workingStack.push(inputStackHead);
        workingStack.push(table.get(workingStackHead).get(inputStackHead));
    }

    private void reduce(Stack<String> workingStack, Stack<Integer> outputStack, int productionNumber, Map<String, Map<String, String>> table) {
        Map.Entry<List<String>, List<String>> production = grammar.getProductionWithNumber(productionNumber);
        int productionLength = production.getValue().size();
        for (int i = 0; i < productionLength; i++) {
            workingStack.pop();
            workingStack.pop();
        }
        String workingStackHead = workingStack.peek();
        String leftSideOfProduction = production.getKey().get(0);
        workingStack.push(leftSideOfProduction);
        workingStack.push(table.get(workingStackHead).get(leftSideOfProduction));
        outputStack.push(productionNumber);
    }

    private String determineAction(Item item) {
        String action = ERROR;
        if (item.getFirstAfterDot().isEmpty()) {
            if(item.getLeftSide().equals("S'") && item.getBeforeDot().equals(List.of(grammar.getStartingSymbol()))) {
                action = ACCEPT_ACTION;
            } else {
                action = REDUCE_ACTION + " " + grammar.getProductionNumber(List.of(item.getLeftSide()), item.getBeforeDot());
            }
        } else {
            action = SHIFT_ACTION;
        }
        return action;
    }

    private String determineGoTo(Set<Item> startingState, String startingToken, Map<String, Set<Item>> states) {
        Set<Item> goToResult = goTo(startingState, startingToken);
        if (goToResult.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Set<Item>> state: states.entrySet()) {
            if (state.getValue().equals(goToResult)) {
                return state.getKey();
            }
        }
        return null;
    }
}
