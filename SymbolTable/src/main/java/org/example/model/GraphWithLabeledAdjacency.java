package org.example.model;

import java.util.List;
import java.util.Map;

public class GraphWithLabeledAdjacency {
    private final Map<Vertex, Map<String, List<Vertex>>> adjMap;

    public GraphWithLabeledAdjacency(Map<Vertex, Map<String, List<Vertex>>> adjMap) {
        this.adjMap = adjMap;
    }

    public List<Vertex> getNeighborsThroughElement(Vertex starter, String element) {
        Map<String, List<Vertex>> vertexAdjacent = adjMap.get(starter);
        if (vertexAdjacent != null) {
            return vertexAdjacent.get(element);
        }
        return null;
    }

    public Map<Vertex, Map<String, List<Vertex>>> getAdjMap() {
        return adjMap;
    }
}
