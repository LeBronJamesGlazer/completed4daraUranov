package org.example.dagsp;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;
import org.example.topo.TopologicalSort;

import java.util.Arrays;
import java.util.List;

/**
 * Calculates the shortest and longest paths in a Directed Acyclic Graph (DAG).
 * This is done by processing the nodes in topological order.
 */
public class DAGSP {
    private final Graph graph;
    private final int source;
    private final Metrics metrics;

    public DAGSP(Graph graph, int source, Metrics metrics) {
        this.graph = graph;
        this.source = source;
        this.metrics = metrics;
    }

    public int[] shortestPaths() {
        metrics.start();
        TopologicalSort ts = new TopologicalSort(graph, new org.example.metrics.MetricsImpl()); 
        List<Integer> topOrder = ts.sort();

        int[] dist = new int[graph.getN()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        for (int u : topOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge edge : graph.getAdj()[u]) {
                    int v = edge.getV();
                    int weight = edge.getW();
                    if (dist[v] > dist[u] + weight) {
                        dist[v] = dist[u] + weight;
                        // Relaxation step: if a shorter path to vertex v is found through u,
                        // update the distance of v and record the relaxation operation in metrics.
                        metrics.incrementCounter("Relaxations");
                    }
                }
            }
        }
        metrics.stop();
        return dist;
    }

    public int[] longestPaths() {
        metrics.start();
        // Perform a topological sort of the DAG to ensure that we process vertices in
        // a valid order. This guarantees that when evaluating the longest path,
        // all incoming edges of a vertex are already considered.
        TopologicalSort ts = new TopologicalSort(graph, new org.example.metrics.MetricsImpl());
        List<Integer> topOrder = ts.sort();

        int[] dist = new int[graph.getN()];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;

        for (int u : topOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Edge edge : graph.getAdj()[u]) {
                    int v = edge.getV();
                    int weight = edge.getW();
                    if (dist[v] < dist[u] + weight) {
                        dist[v] = dist[u] + weight;
                        metrics.incrementCounter("Relaxations");
                    }
                }
            }
        }
        metrics.stop();
        return dist;
    }
}
