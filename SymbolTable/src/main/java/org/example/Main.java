package org.example;

import org.example.model.HashTable;
import org.example.model.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(new HashTable<>());

        Integer key1 = symbolTable.addValue(1);
        Integer key2 = symbolTable.addValue("Ana are mere");

        System.out.println(symbolTable.getValue(key1));
        System.out.println(symbolTable.getValue(key2));

        System.out.println(symbolTable.searchForValue(1));
        System.out.println(symbolTable.searchForValue("Ana are mere"));
        System.out.println(symbolTable.searchForValue(2));
    }
}