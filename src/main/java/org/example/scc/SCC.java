package org.example.scc;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Implements Tarjan's algorithm for finding Strongly Connected Components in a directed graph.
 */
public class SCC {
    private final Graph graph;
    private int time;
    private final int[] low;
    private final int[] disc;
    private final boolean[] onStack;
    private final Stack<Integer> stack;
    private final List<List<Integer>> sccs;
    private final Metrics metrics;

    public SCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.low = new int[graph.getN()];
        this.disc = new int[graph.getN()];
        this.onStack = new boolean[graph.getN()];
        this.stack = new Stack<>();
        this.sccs = new ArrayList<>();
    }

    public List<List<Integer>> findSCCs() {
        metrics.start();
        time = 0;
        for (int i = 0; i < graph.getN(); i++) {
            if (disc[i] == 0) {
                dfs(i);
            }
        }
        metrics.stop();
        return sccs;
    }

    // Depth-First Search (DFS) used by Tarjan's algorithm.
    // Assigns discovery times and low-link values, and detects SCC roots.
    private void dfs(int u) {
        metrics.incrementCounter("DFS Visits");
        // Initialize discovery time and low-link value for the current vertex.
        disc[u] = low[u] = ++time;
        // Push the vertex onto the stack and mark it as active (currently in recursion).
        stack.push(u);
        onStack[u] = true;

        // Explore all adjacent vertices of the current vertex.
        for (Edge edge : graph.getAdj()[u]) {
            metrics.incrementCounter("DFS Edges");
            int v = edge.getV();
            // If the adjacent vertex hasn't been visited, perform DFS recursively.
            if (disc[v] == 0) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            }
            // If the adjacent vertex is still on the stack, update the low-link value.
            else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // If u is a root node of an SCC, pop the stack to extract the full component.
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == u) break;
            }
            sccs.add(scc);
        }
    }
}
