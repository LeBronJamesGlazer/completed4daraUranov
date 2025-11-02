package org.example.topo;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implements Kahn's algorithm for topological sorting of a directed graph.
 */
// Performs topological sorting using Kahn’s algorithm (BFS-based approach).
// Works only on Directed Acyclic Graphs (DAGs).
public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public TopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    // Executes Kahn's algorithm to return a topological order of vertices.
    // If the graph contains a cycle, returns an empty list.
    public List<Integer> sort() {
        metrics.start();
        // Compute in-degrees (number of incoming edges) for all vertices.
        int[] inDegree = new int[graph.getN()];
        // Traverse adjacency lists to count in-degrees.
        for (int i = 0; i < graph.getN(); i++) {
            for (Edge edge : graph.getAdj()[i]) {
                inDegree[edge.getV()]++;
            }
        }

        // Initialize a queue and add all vertices with zero in-degree.
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < graph.getN(); i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
                metrics.incrementCounter("Pushes");
            }
        }

        List<Integer> topOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            // Remove vertex with zero in-degree and append it to topological order.
            int u = queue.poll();
            metrics.incrementCounter("Pops");
            topOrder.add(u);

            // Decrease in-degree of all adjacent vertices.
            // If any vertex reaches zero in-degree, enqueue it.
            for (Edge edge : graph.getAdj()[u]) {
                int v = edge.getV();
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                    metrics.incrementCounter("Pushes");
                }
            }
        }
        metrics.stop();

        // If not all vertices are processed, the graph has at least one cycle.
        if (topOrder.size() != graph.getN()) {
            // Graph has a cycle
            return new ArrayList<>();
        }

        return topOrder;
    }
}
