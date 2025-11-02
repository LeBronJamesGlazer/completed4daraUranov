
package org.example.topo;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.MetricsImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TopologicalSortTest {

    @Test
    public void testSimpleDAG() {
        System.out.println("--- TopologicalSortTest: testSimpleDAG ---");
        Graph graph = new Graph(6);
        graph.addEdge(new Edge(0, 1, 2));
        graph.addEdge(new Edge(0, 2, 5));
        graph.addEdge(new Edge(1, 3, 3));
        graph.addEdge(new Edge(2, 3, 1));
        graph.addEdge(new Edge(2, 4, 6));
        graph.addEdge(new Edge(3, 5, 4));
        graph.addEdge(new Edge(4, 5, 1));

        TopologicalSort topo = new TopologicalSort(graph, new MetricsImpl());
        List<Integer> topOrder = topo.sort();
        System.out.println("Topological order: " + topOrder);
        assertEquals(6, topOrder.size());
        System.out.println("Test passed.");
    }

    @Test
    public void testWithCycle() {
        System.out.println("--- TopologicalSortTest: testWithCycle ---");
        Graph graph = new Graph(3);
        graph.addEdge(new Edge(0, 1, 1));
        graph.addEdge(new Edge(1, 2, 1));
        graph.addEdge(new Edge(2, 0, 1));

        TopologicalSort topo = new TopologicalSort(graph, new MetricsImpl());
        List<Integer> topOrder = topo.sort();
        System.out.println("Topological order (should be empty): " + topOrder);
        assertTrue(topOrder.isEmpty());
        System.out.println("Test passed.");
    }

    @Test
    public void testEmptyGraph() {
        System.out.println("--- TopologicalSortTest: testEmptyGraph ---");
        Graph graph = new Graph(0);
        TopologicalSort topo = new TopologicalSort(graph, new MetricsImpl());
        List<Integer> topOrder = topo.sort();
        System.out.println("Topological order (should be empty): " + topOrder);
        assertTrue(topOrder.isEmpty());
        System.out.println("Test passed.");
    }
}
