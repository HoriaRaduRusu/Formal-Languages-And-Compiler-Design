package org.example;

import org.example.model.FiniteAutomaton;
import org.example.model.GraphWithLabeledAdjacency;
import org.example.model.Vertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static void printMenu() {
        System.out.println("1. Display the set of states");
        System.out.println("2. Display the alphabet");
        System.out.println("3. Display the transitions");
        System.out.println("4. Display the initial state");
        System.out.println("5. Display the final states");
        System.out.println("6. Verify if a sequence is accepted");
        System.out.println("7. Exit");
        System.out.println("Choose an option:");
    }

    public static void main(String[] args) throws IOException {
        FiniteAutomaton finiteAutomaton = readFiniteAutomatonDetails("org/example/model/FA.in");
        boolean done = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (! done) {
            printMenu();
            String option = reader.readLine();
            switch(option){
                case "1":
                    System.out.println(finiteAutomaton.getStates());
                    break;
                case "2":
                    System.out.println(finiteAutomaton.getAlphabet());
                    break;
                case "3":
                    System.out.println(finiteAutomaton.getTransitionsForPrinting());
                    break;
                case "4":
                    System.out.println(finiteAutomaton.getStartingState());
                    break;
                case "5":
                    System.out.println(finiteAutomaton.getEndStates());
                    break;
                case "6":
                    if (finiteAutomaton.isDFA()) {
                        System.out.println("Give a sequence:");
                        String sequence = reader.readLine();
                        System.out.println(finiteAutomaton.isSequenceAccepted(sequence) ? "The sequence is accepted" : "The sequence is not accepted");
                    } else {
                        System.out.println("The FA is not a DFA!");
                    }
                    break;
                case "7":
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }
}