package org.example.model;

public class SymbolTable {
    private final HashTable<Object> hashTable;

    public SymbolTable(HashTable<Object> hashTable) {
        this.hashTable = hashTable;
    }

    public Integer addValue(Object value) {
        Integer existingKey = hashTable.search(value);
        return existingKey == -1 ? hashTable.add(value) : existingKey;
    }

    public Integer searchForValue(Object value) {
        return hashTable.search(value);
    }

    public Object getValue(Integer key) {
        return hashTable.get(key);
    }

    @Override
    public String toString() {
        return "Symbol table (stored as a hash table):\n" + hashTable.toString();
    }
}
