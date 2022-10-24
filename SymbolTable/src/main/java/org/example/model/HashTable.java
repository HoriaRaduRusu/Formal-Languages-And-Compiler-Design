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
        return table.get(key % HASH_VALUE).get(key / HASH_VALUE);
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
}
