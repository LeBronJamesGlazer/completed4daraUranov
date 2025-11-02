package org.example.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a directed weighted graph using adjacency lists.
 * This class supports storing all edges and provides access
 * to adjacency lists for each vertex.
 * Used for algorithms such as shortest and longest path calculations.
 */
public class Graph {
    private final int n;
    private final List<Edge>[] adj;
    private final List<Edge> allEdges;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        this.allEdges = new ArrayList<>();
    }

    public void addEdge(Edge edge) {
        adj[edge.getU()].add(edge);
        allEdges.add(edge);
    }

    public int getN() {
        return n;
    }

    public List<Edge>[] getAdj() {
        return adj;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }
}
