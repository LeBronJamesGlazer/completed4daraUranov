
package org.example.dagsp;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.MetricsImpl;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DAGSPTest {

    @Test
    public void testShortestPath() {
        System.out.println("--- DAGSPTest: testShortestPath ---");
        Graph graph = new Graph(6);
        graph.addEdge(new Edge(0, 1, 2));
        graph.addEdge(new Edge(0, 2, 5));
        graph.addEdge(new Edge(1, 3, 3));
        graph.addEdge(new Edge(2, 3, 1));
        graph.addEdge(new Edge(2, 4, 6));
        graph.addEdge(new Edge(3, 5, 4));
        graph.addEdge(new Edge(4, 5, 1));

        DAGSP dagsp = new DAGSP(graph, 0, new MetricsImpl());
        int[] expected = {0, 2, 5, 5, 11, 9};
        int[] actual = dagsp.shortestPaths();
        System.out.println("Shortest paths: " + java.util.Arrays.toString(actual));
        assertArrayEquals(expected, actual);
        System.out.println("Test passed.");
    }

    @Test
    public void testLongestPath() {
        System.out.println("--- DAGSPTest: testLongestPath ---");
        Graph graph = new Graph(6);
        graph.addEdge(new Edge(0, 1, 2));
        graph.addEdge(new Edge(0, 2, 5));
        graph.addEdge(new Edge(1, 3, 3));
        graph.addEdge(new Edge(2, 3, 1));
        graph.addEdge(new Edge(2, 4, 6));
        graph.addEdge(new Edge(3, 5, 4));
        graph.addEdge(new Edge(4, 5, 1));

        DAGSP dagsp = new DAGSP(graph, 0, new MetricsImpl());
        int[] expected = {0, 2, 5, 6, 11, 12};
        int[] actual = dagsp.longestPaths();
        System.out.println("Longest paths: " + java.util.Arrays.toString(actual));
        assertArrayEquals(expected, actual);
        System.out.println("Test passed.");
    }

    @Test
    public void testDisconnectedSource() {
        System.out.println("--- DAGSPTest: testDisconnectedSource ---");
        Graph graph = new Graph(3);
        graph.addEdge(new Edge(1, 2, 1));

        DAGSP dagsp = new DAGSP(graph, 0, new MetricsImpl());
        int[] expected = {0, Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] actual = dagsp.shortestPaths();
        System.out.println("Shortest paths (disconnected): " + java.util.Arrays.toString(actual));
        assertArrayEquals(expected, actual);
        System.out.println("Test passed.");
    }
}
