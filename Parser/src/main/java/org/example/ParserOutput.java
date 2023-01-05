package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ParserOutput {
    private final Grammar grammar;
    private final Stack<Integer> resultStack;
    private final String result;
    private final TableOutput resultTable;
    public ParserOutput(Grammar grammar, Stack<Integer> resultStack, String result) {
        this.grammar = grammar;
        this.resultStack = resultStack;
        this.result = result;
        if (result.equals("success")) {
            this.resultTable = convertToTable();
        } else {
            this.resultTable = new TableOutput(new ArrayList<>());
        }
    }

    public void printTableToConsole() {
        System.out.println(result);
        System.out.println(resultTable);
    }

    public void printTableToFile(String fileName){
        try(FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(result);
            fileWriter.write("\n");
            fileWriter.write(resultTable.toString());
        } catch (IOException e) {
            System.out.println("File operation not accessible:" + e.getMessage());
        }
    }

    private TableOutput convertToTable() {
        List<TableRow> resultTable = new ArrayList<>();
        Node treeResult = convertToTree();
        Queue<TableConstructData> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(new TableConstructData(treeResult, -1, false));
        while (!nodeQueue.isEmpty()) {
            TableConstructData currentData = nodeQueue.poll();
            int position = resultTable.size();
            Node currentNode = currentData.getNode();
            TableRow tableRow = new TableRow(currentNode.getSymbol(), currentData.getParentIndex(), currentData.hasRightSibling() ? position + 1 : -1);
            resultTable.add(tableRow);
            for (int i = 0; i < currentNode.getChildren().size(); i++) {
                nodeQueue.add(new TableConstructData(currentNode.getChildren().get(i), position, i != currentNode.getChildren().size() - 1));
            }
        }
        return new TableOutput(resultTable);
    }

    private Node convertToTree() {
        Node rootNode = new Node(grammar.getStartingSymbol(), null, new ArrayList<>());
        Stack<?> resultStackCopy = (Stack<?>) resultStack.clone();
        while (!resultStackCopy.isEmpty()) {
            Integer productionNumber = (Integer) resultStackCopy.pop();
            Map.Entry<List<String>, List<String>> production = grammar.getProductionWithNumber(productionNumber);
            Node affectedNode = getLastNonDerivedNonTerminal(rootNode);
            for (String productionResult : production.getValue()) {
                Node childNode = new Node(productionResult, affectedNode, new ArrayList<>());
                affectedNode.addChild(childNode);
            }
        }
        return rootNode;
    }

    private Node getLastNonDerivedNonTerminal(Node rootNode) {
        Stack<Node> nonTerminalNodes = new Stack<>();
        nonTerminalNodes.push(rootNode);
        Node lastNonDerivedNonTerminalNode = null;
        boolean done = false;
        while (!done){
            done = true;
            lastNonDerivedNonTerminalNode = nonTerminalNodes.pop();
            if (!lastNonDerivedNonTerminalNode.getChildren().isEmpty()) {
                done = false;
                for (Node childNode : lastNonDerivedNonTerminalNode.getChildren()) {
                    if (grammar.isNonTerminal(childNode.getSymbol())) {
                        nonTerminalNodes.push(childNode);
                    }
                }
            }
        }
        return lastNonDerivedNonTerminalNode;
    }
}
