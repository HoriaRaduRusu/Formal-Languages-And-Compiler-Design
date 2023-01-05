package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void printProductionsForNonTerminal(Grammar grammar, BufferedReader reader) throws IOException {
        System.out.println("Give a non terminal:");
        String terminal = reader.readLine().trim();
        System.out.println("The productions for the given non terminal are: ");
        String productions = grammar.getProductionsForNonTerminalForPrinting(terminal);
        System.out.println(productions.isEmpty() ? "There are no productions for the given terminal" :  productions);
    }

    public static void readGrammarFromFile(Grammar grammar, BufferedReader reader) throws IOException {
        System.out.println("Give file name:");
        String fileName = reader.readLine().trim();
        grammar.loadFromFile(fileName);
    }

    public static void parseSequence(Grammar grammar, BufferedReader reader) throws  IOException {
        System.out.println("Give the name of the file containing the sequence: ");
        String sequenceFile = reader.readLine().trim();
        try(InputStream inputStream = Grammar.class.getClassLoader().getResourceAsStream(sequenceFile)) {
            Scanner scanner = new Scanner(inputStream);
            List<String> sequence = Arrays.stream(scanner.nextLine().strip().split(" ")).toList();
            System.out.println("Give output file name: ");
            String fileName = reader.readLine().trim();
            Parser parser = new Parser(grammar);
            ParserOutput parserOutput = parser.parse(sequence);
            parserOutput.printTableToConsole();
            parserOutput.printTableToFile(fileName);
        }
    }


    public static void printMenu() {
        System.out.println("1. Display the set of nonterminals");
        System.out.println("2. Display the set of terminals");
        System.out.println("3. Display the productions");
        System.out.println("4. Display the productions for a given nonterminal");
        System.out.println("5. Verify if the grammar is CFG");
        System.out.println("6. Read a grammar from a file");
        System.out.println("7. Parse a sequence using the grammar and print result to screen and file");
        System.out.println("8. Exit");
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
                case "7" -> parseSequence(grammar, reader);
                case "8" -> done = true;
                default -> System.out.println("Invalid option!");
            }
        }
    }

}