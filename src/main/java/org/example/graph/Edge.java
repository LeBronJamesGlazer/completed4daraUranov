package org.example.graph;

/**
 * Represents a directed edge in a weighted graph.
 * Each edge connects two vertices (u -> v) with an associated weight (w).
 * Used in graph algorithms such as shortest path and topological sorting.
 */
public class Edge {
    // The starting vertex of the edge
    private final int u;
    // The ending vertex of the edge
    private final int v;
    // The weight or cost associated with the edge
    private final int w;

    /**
     * Constructs an Edge with a start vertex, end vertex, and weight.
     *
     * @param u the starting vertex
     * @param v the ending vertex
     * @param w the weight or cost of the edge
     */
    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    /**
     * @return the starting vertex of this edge
     */
    public int getU() {
        return u;
    }

    /**
     * @return the ending vertex of this edge
     */
    public int getV() {
        return v;
    }

    /**
     * @return the weight of this edge
     */
    public int getW() {
        return w;
    }
}
