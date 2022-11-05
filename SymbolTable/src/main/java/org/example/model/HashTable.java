package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTable<V> {
    private final static Integer HASH_VALUE = 389;

    private final Map<Integer, List<V>> table = new HashMap<>();

    public Integer add(V value) {
        Integer key = hash(value);
        table.putIfAbsent(key, new ArrayList<>());
        Integer arrayLen = table.get(key).size();
        table.get(key).add(value);
        return HASH_VALUE * arrayLen + key;
    }

    public V get(Integer key) {
        int tableKey = key % HASH_VALUE;
        int bucketPosition = key / HASH_VALUE;
        if (!table.containsKey(tableKey)) {
            return null;
        }
        if (table.get(tableKey).size() >= bucketPosition) {
            return null;
        }
        return table.get(tableKey).get(bucketPosition);
    }

    public Integer search(V value) {
        Integer key = hash(value);
        if (!table.containsKey(key)) {
            return -1;
        }
        int listIndex = table.get(key).indexOf(value);
        return listIndex == -1 ? listIndex : listIndex * HASH_VALUE + key;
    }

    private Integer hash(V value) {
        if (value instanceof Integer numberValue) {
            return numberValue % HASH_VALUE;
        }
        Integer sum = 0;
        for (Character character : value.toString().toCharArray()) {
            sum += character;
        }
        return sum % HASH_VALUE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Integer key : table.keySet()) {
            for (int i = 0; i < table.get(key).size(); i++){
                builder.append(HASH_VALUE * i + key).append(" - ").append(table.get(key).get(i).toString()).append("\n");
            }
        }
        return builder.toString();
    }
}
