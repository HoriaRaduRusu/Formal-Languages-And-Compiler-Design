package org.example;

import org.example.model.HashTable;
import org.example.model.Pif;
import org.example.model.SymbolTable;
import org.example.service.LexicalAnalyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

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

    public static void main(String[] args) {
        List<String> operators = new ArrayList<>();
        List<String> separators = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        populateLists(operators, separators, keywords);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(separators, operators, keywords);
        analyzeProgram("p1.txt", lexicalAnalyzer);
        analyzeProgram("p2.txt", lexicalAnalyzer);
        analyzeProgram("p3.txt", lexicalAnalyzer);
        analyzeProgram("p1err.txt", lexicalAnalyzer);
    }
}