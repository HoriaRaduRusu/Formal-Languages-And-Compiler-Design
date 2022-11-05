package org.example.service;

import org.example.model.Pair;
import org.example.model.Pif;
import org.example.model.SymbolTable;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LexicalAnalyzer {

    private static final String REGEX_WITH_DELIMITER = "(?<=%1$s)|(?=%1$s)";

    private final List<String> separators;
    private final List<String> operators;
    private final List<String> keywords;
    private final String operatorsRegex;
    private final String separatorsRegex;

    public LexicalAnalyzer(List<String> separators, List<String> operators, List<String> keywords) {
        this.separators = separators;
        this.operators = operators;
        this.keywords = keywords;
        this.operatorsRegex = listToRegex(operators);
        this.separatorsRegex = listToRegex(separators);
    }

    public String analyzeProgram(String programLocation, SymbolTable symbolTable, Pif pif) {
        int lineCount = 1;
        StringBuilder message = new StringBuilder();
        try (Scanner scanner = new Scanner(new FileReader(programLocation))) {
            while (scanner.hasNextLine()) {
                List<String> nextTokens = detectTokens(scanner.nextLine().trim());
                for (String token : nextTokens) {
                    if (isTokenAKeywordOperatorOrSeparator(token)) {
                        pif.addElement(new Pair<>(token, -1));
                    } else {
                        try {
                            String type = isTokenIdentifierOrConstant(token);
                            Integer position = symbolTable.addValue(token);
                            pif.addElement(new Pair<>(type, position));
                        } catch (RuntimeException e) {
                            message.append("Lexical error line ").append(lineCount).append(", token ").append(token).append("\n");
                        }
                    }
                }
                lineCount++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String returnMessage = message.toString();
        return returnMessage.isEmpty() ? "lexically correct" : returnMessage;
    }

    private List<String> detectTokens(String line) {
        List<String> initialTokens = List.of(line.split("\s+"));
        return initialTokens.stream()
                .flatMap(token -> Arrays.stream(token.split(separatorsRegex)))
                .flatMap(token -> Arrays.stream(token.split(operatorsRegex)))
                .collect(Collectors.toList());
    }

    private boolean isTokenAKeywordOperatorOrSeparator(String token) {
        return separators.contains(token) || keywords.contains(token) || operators.contains(token);
    }

    private String isTokenIdentifierOrConstant(String token) {
        if (token.matches("^[a-zA-Z][a-zA-Z\\d]*$")) {
            return "id";
        }
        if (token.matches("^(([+-][1-9]\\d*)|([1-9]\\d*)|0)$") ||
                token.matches("^'[a-zA-Z\\d]'$") ||
                token.matches("^\"[a-zA-Z\\d]\"$")) {
            return "const";
        }
        throw new RuntimeException();
    }

    private String listToRegex(List<String> list) {
        return "(" + list.stream()
                .sorted((s1, s2) -> s2.length() - s1.length())
                .map(Pattern::quote)
                .map(regex -> String.format(REGEX_WITH_DELIMITER, regex))
                .collect(Collectors.joining("|")) +
                ")";
    }
}
