package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void printProductionsForNonTerminal(Grammar grammar, BufferedReader reader) throws IOException {
        System.out.println("Give a terminal:");
        String terminal = reader.readLine().trim();
        System.out.println("The productions for the given terminal are: ");
        String productions = grammar.getProductionsForNonTerminalForPrinting(terminal);
        System.out.println(productions.isEmpty() ? "There are no productions for the given terminal" :  productions);
    }

    public static void readGrammarFromFile(Grammar grammar, BufferedReader reader) throws IOException {
        System.out.println("Give file name:");
        String fileName = reader.readLine().trim();
        grammar.loadFromFile(fileName);
    }


    public static void printMenu() {
        System.out.println("1. Display the set of nonterminals");
        System.out.println("2. Display the set of terminals");
        System.out.println("3. Display the productions");
        System.out.println("4. Display the productions for a given nonterminal");
        System.out.println("5. Verify if the grammar is CFG");
        System.out.println("6. Read a grammar from a file");
        System.out.println("7. Exit");
        System.out.println("Choose an option:");
    }

    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar();
        boolean done = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!done) {
            printMenu();
            String option = reader.readLine();
            switch (option) {
                case "1" -> System.out.println(grammar.getNonTerminalSetForPrinting());
                case "2" -> System.out.println(grammar.getTerminalSetForPrinting());
                case "3" -> System.out.println(grammar.getProductionSetForPrinting());
                case "4" -> printProductionsForNonTerminal(grammar, reader);
                case "5" -> System.out.println(grammar.checkIfCFG() ? "The grammar is CFG" : "The grammar is not CFG");
                case "6" -> readGrammarFromFile(grammar, reader);
                case "7" -> done = true;
                default -> System.out.println("Invalid option!");
            }
        }
    }

}