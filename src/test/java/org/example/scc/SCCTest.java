
package org.example.scc;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.MetricsImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SCCTest {

    @Test
    public void testSingleSCC() {
        System.out.println("--- SCCTest: testSingleSCC ---");
        Graph graph = new Graph(3);
        graph.addEdge(new Edge(0, 1, 1));
        graph.addEdge(new Edge(1, 2, 1));
        graph.addEdge(new Edge(2, 0, 1));

        SCC scc = new SCC(graph, new MetricsImpl());
        List<List<Integer>> sccs = scc.findSCCs();
        System.out.println("SCCs found: " + sccs);
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
        System.out.println("Test passed.");
    }

    @Test
    public void testMultipleSCCs() {
        System.out.println("--- SCCTest: testMultipleSCCs ---");
        Graph graph = new Graph(8);
        graph.addEdge(new Edge(0, 1, 1));
        graph.addEdge(new Edge(1, 2, 1));
        graph.addEdge(new Edge(2, 0, 1));
        graph.addEdge(new Edge(3, 4, 1));
        graph.addEdge(new Edge(4, 5, 1));
        graph.addEdge(new Edge(5, 3, 1));
        graph.addEdge(new Edge(1, 3, 5));
        graph.addEdge(new Edge(6, 7, 1));

        SCC scc = new SCC(graph, new MetricsImpl());
        List<List<Integer>> sccs = scc.findSCCs();
        System.out.println("SCCs found: " + sccs);
        assertEquals(4, sccs.size());
        System.out.println("Test passed.");
    }

    @Test
    public void testSingleNode() {
        System.out.println("--- SCCTest: testSingleNode ---");
        Graph graph = new Graph(1);
        SCC scc = new SCC(graph, new MetricsImpl());
        List<List<Integer>> sccs = scc.findSCCs();
        System.out.println("SCCs found: " + sccs);
        assertEquals(1, sccs.size());
        assertEquals(1, sccs.get(0).size());
        System.out.println("Test passed.");
    }

    @Test
    public void testNoEdges() {
        System.out.println("--- SCCTest: testNoEdges ---");
        Graph graph = new Graph(3);
        SCC scc = new SCC(graph, new MetricsImpl());
        List<List<Integer>> sccs = scc.findSCCs();
        System.out.println("SCCs found: " + sccs);
        assertEquals(3, sccs.size());
        System.out.println("Test passed.");
    }
}
