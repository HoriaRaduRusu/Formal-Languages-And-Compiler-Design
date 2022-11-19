package org.example;

import org.example.model.*;
import org.example.service.LexicalAnalyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static Map<Vertex, Map<String, List<Vertex>>> readTransitionsMap(Scanner scanner) {
        Map<Vertex, Map<String, List<Vertex>>> transitionsMap = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] lineElements = line.split(" ");
            String startState = lineElements[0];
            String alphabetElement = lineElements[1];
            List<String> endStates = Arrays.stream(lineElements).skip(2).toList();
            Vertex startVertex = new Vertex(startState);
            transitionsMap.putIfAbsent(startVertex, new HashMap<>());
            transitionsMap.get(startVertex).putIfAbsent(alphabetElement, new ArrayList<>());
            for (String endState : endStates) {
                transitionsMap.get(startVertex).get(alphabetElement).add(new Vertex(endState));
            }
        }
        return transitionsMap;
    }

    public static FiniteAutomaton readFiniteAutomatonDetails(String file) throws IOException {
        try (InputStream inputStream = FiniteAutomaton.class.getClassLoader().getResourceAsStream(file);
             Scanner scanner = new Scanner(inputStream)) {
            Set<String> states = Arrays.stream(scanner.nextLine().strip().split(" "))
                    .collect(Collectors.toSet());
            Set<String> alphabet = Arrays.stream(scanner.nextLine().strip().split(" "))
                    .collect(Collectors.toSet());
            String startingState = scanner.nextLine().trim();
            Set<String> endStates = Arrays.stream(scanner.nextLine().strip().split(" "))
                    .collect(Collectors.toSet());
            GraphWithLabeledAdjacency transitionsGraph = new GraphWithLabeledAdjacency(readTransitionsMap(scanner));
            return new FiniteAutomaton(states, alphabet, startingState, endStates, transitionsGraph);
        }
    }

    private static void populateLists(List<String> operators, List<String> separators, List<String> keywords) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("token.in");
        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    operators.add(line);
                }
                while (!(line = scanner.nextLine()).isEmpty()) {
                    separators.add(line);
                }
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    keywords.add(line);
                }
            }
        }
    }

    public static void writeToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void analyzeProgram(String program, LexicalAnalyzer lexicalAnalyzer) {
        String fileLocation = Main.class.getClassLoader().getResource(program).getFile();
        Pif pif = new Pif();
        SymbolTable symbolTable = new SymbolTable(new HashTable<>());
        String message = lexicalAnalyzer.analyzeProgram(fileLocation, symbolTable, pif);
        System.out.println(message);
        writeToFile("pif-"+program, pif.toString());
        writeToFile("st-"+program, symbolTable.toString());
    }

    public static void main(String[] args) throws IOException {
        List<String> operators = new ArrayList<>();
        List<String> separators = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        populateLists(operators, separators, keywords);
        FiniteAutomaton integerConstantFiniteAutomaton = readFiniteAutomatonDetails("integerConstant.in");
        FiniteAutomaton identifierFiniteAutomaton = readFiniteAutomatonDetails("identifier.in");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(separators, operators, keywords, identifierFiniteAutomaton, integerConstantFiniteAutomaton);
        analyzeProgram("p1.txt", lexicalAnalyzer);
        analyzeProgram("p2.txt", lexicalAnalyzer);
        analyzeProgram("p3.txt", lexicalAnalyzer);
        analyzeProgram("p1err.txt", lexicalAnalyzer);
    }
}