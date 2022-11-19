package org.example.model;

import org.example.exceptions.InvalidFiniteAutomatonException;

import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomaton {
    private final Set<String> states;
    private final Set<String> alphabet;
    private final String startingState;
    private final Set<String> endStates;
    private final GraphWithLabeledAdjacency transitionsGraph;

    public FiniteAutomaton(Set<String> states, Set<String> alphabet, String startingState, Set<String> endStates, GraphWithLabeledAdjacency transitionsGraph) {
        this.states = states;
        this.alphabet = alphabet;
        this.startingState = startingState;
        this.endStates = endStates;
        this.transitionsGraph = transitionsGraph;
        validateFiniteAutomaton();
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public String getStartingState() {
        return startingState;
    }

    public Set<String> getEndStates() {
        return endStates;
    }

    public String getTransitionsForPrinting() {
        StringBuilder stringBuilder = new StringBuilder();
        transitionsGraph.getAdjMap().forEach((vertex, stringListMap) ->
                stringListMap.forEach((s, vertices) -> {
                    stringBuilder.append("delta(").append(vertex.getLabel()).append(",").append(s).append(")=");
                    if (vertices.size() == 1) {
                        stringBuilder.append(vertices.get(0).getLabel());
                    } else {
                        stringBuilder.append("{").append(vertices.stream().map(Vertex::getLabel).collect(Collectors.joining(","))).append("}");
                    }
                    stringBuilder.append("\n");
                })
        );

        return stringBuilder.toString();
    }

    public boolean isDFA() {
        return transitionsGraph.getAdjMap().values().stream()
                .flatMap(stringSetMap -> stringSetMap.values().stream())
                .allMatch(vertices -> vertices.size() == 1);
    }

    public boolean isSequenceAccepted(String sequence) {
        int currentPosition = 0;
        String currentState = startingState;
        while (currentState != null && currentPosition < sequence.length()) {
            List<Vertex> neighbours = transitionsGraph.getNeighborsThroughElement(new Vertex(currentState), String.valueOf(sequence.charAt(currentPosition)));
            if (neighbours == null) {
                currentState = null;
            } else {
                currentState = neighbours.get(0).getLabel();
                currentPosition += 1;
            }
        }
        return currentPosition == sequence.length() && currentState != null && endStates.contains(currentState);
    }

    private void validateFiniteAutomaton() {
        if (!states.contains(startingState)) {
            throw new InvalidFiniteAutomatonException("The starting state cannot be found in the declared states!");
        }
        endStates.stream()
                .filter(state -> !states.contains(state))
                .forEach(state -> {
                    throw new InvalidFiniteAutomatonException("The finite state " + state + " cannot be found in the declared states!");
                });
        transitionsGraph.getAdjMap().forEach((startVertex, adjMap) -> {
            String startState = startVertex.getLabel();
            if (!states.contains(startState)) {
                throw new InvalidFiniteAutomatonException("State " + startState + " was not declared!");
            }
            adjMap.forEach((alphabetElement, adjVertices) -> {
                if (!alphabet.contains(alphabetElement)) {
                    throw new InvalidFiniteAutomatonException("Alphabet term " + alphabetElement + " was not declared!");
                }
                adjVertices.stream()
                        .map(Vertex::getLabel)
                        .filter(state -> !states.contains(state))
                        .forEach(state -> {
                            throw new InvalidFiniteAutomatonException("State " + state + " was not declared!");
                        });
            });
        });
    }
}
