package org.example;

import org.example.dagsp.DAGSP;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;
import org.example.metrics.MetricsImpl;
import org.example.scc.SCC;
import org.example.topo.TopologicalSort;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Entry point of the program that analyzes graph datasets.
// Performs SCC detection, topological sorting, and shortest/longest path analysis
// with metrics and performance measurement for each algorithm.
public class Main {
    // Main method to select and process graph datasets from the 'data' directory.
    // Supports multiple modes: small, medium, large, specific sets, or all datasets.
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a dataset to analyze:");
        System.out.println("  - small");
        System.out.println("  - medium");
        System.out.println("  - large");
        System.out.println("  - specific (dag, dense, multi_scc)");
        System.out.println("  - all");
        System.out.println("  - or enter a specific filename (e.g., small_1.json)");
        System.out.print("> ");
        String choice = scanner.nextLine();

        List<File> filesToAnalyze = new ArrayList<>();
        File dataDir = new File("data");

        // Load all datasets from the data directory if user selects "all".
        if (choice.equals("all")) {
            filesToAnalyze.addAll(Arrays.asList(dataDir.listFiles()));
        }
        // Load specific predefined datasets (DAG, dense graph, and multi-SCC examples).
        else if (choice.equals("specific")) {
            filesToAnalyze.add(new File("data/dag.json"));
            filesToAnalyze.add(new File("data/dense.json"));
            filesToAnalyze.add(new File("data/multi_scc.json"));
        } else if (choice.endsWith(".json")) {
            filesToAnalyze.add(new File("data/" + choice));
        } else {
            for (File file : dataDir.listFiles()) {
                if (file.getName().startsWith(choice)) {
                    filesToAnalyze.add(file);
                }
            }
        }

        // Iterate through each selected dataset and run the analysis pipeline.
        for (File dataset : filesToAnalyze) {
            if (dataset.isFile() && dataset.getName().endsWith(".json")) {
                runAnalysis(dataset);
            }
        }
    }

    // Core method that executes all analysis steps (SCC, Topological Sort, Shortest and Longest Paths)
    // on a given dataset and reports both results and performance metrics.
    private static void runAnalysis(File dataset) throws IOException {
        System.out.println("\n==================================================");
        System.out.println("Analyzing dataset: " + dataset.getName());
        System.out.println("==================================================");

        String content = new String(Files.readAllBytes(Paths.get(dataset.getPath())));
        JSONObject json = new JSONObject(content);

        int n = json.getInt("n");
        JSONArray edges = json.getJSONArray("edges");

        // Parse all edges from the JSON file and build the graph representation.
        Graph graph = new Graph(n);
        for (int i = 0; i < edges.length(); i++) {
            JSONObject edgeJson = edges.getJSONObject(i);
            graph.addEdge(new Edge(edgeJson.getInt("u"), edgeJson.getInt("v"), edgeJson.getInt("w")));
        }

        final int LOOPS = 1000;

        // --- SCC (Strongly Connected Components) Analysis ---
        // Repeated multiple times to measure average execution time.
        long totalSccTime = 0;
        Metrics sccMetrics = new MetricsImpl();
        for (int i = 0; i < LOOPS; i++) {
            SCC scc = new SCC(graph, new MetricsImpl());
            long startTime = System.nanoTime();
            scc.findSCCs();
            long endTime = System.nanoTime();
            totalSccTime += (endTime - startTime);
        }
        SCC scc = new SCC(graph, sccMetrics);
        List<List<Integer>> sccs = scc.findSCCs();
        System.out.println("Strongly Connected Components:");
        for (List<Integer> component : sccs) {
            System.out.println(component);
        }
        System.out.println("\n--- Metrics Report (SCC) ---");
        System.out.println("Average Execution Time: " + (totalSccTime / LOOPS) + " ns");
        sccMetrics.report();

        // Build condensation graph where each SCC becomes a single node.
        // Edges represent connections between SCCs.
        int numSccs = sccs.size();
        Graph condensationGraph = new Graph(numSccs);
        int[] sccMap = new int[n];
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                sccMap[node] = i;
            }
        }

        for (Edge edge : graph.getAllEdges()) {
            if (sccMap[edge.getU()] != sccMap[edge.getV()]) {
                condensationGraph.addEdge(new Edge(sccMap[edge.getU()], sccMap[edge.getV()], edge.getW()));
            }
        }

        // --- Topological Sort on Condensation Graph ---
        // Measures average performance and reports the topological order of SCCs.
        long totalTopoTime = 0;
        Metrics topoMetrics = new MetricsImpl();
        for (int i = 0; i < LOOPS; i++) {
            TopologicalSort topo = new TopologicalSort(condensationGraph, new MetricsImpl());
            long startTime = System.nanoTime();
            topo.sort();
            long endTime = System.nanoTime();
            totalTopoTime += (endTime - startTime);
        }
        TopologicalSort topo = new TopologicalSort(condensationGraph, topoMetrics);
        List<Integer> topoOrder = topo.sort();
        System.out.println("\nTopological Order of SCCs:");
        System.out.println(topoOrder);
        System.out.println("\n--- Metrics Report (Topo) ---");
        System.out.println("Average Execution Time: " + (totalTopoTime / LOOPS) + " ns");
        topoMetrics.report();

        // --- Shortest Path Analysis ---
        // Runs DAG Shortest Path algorithm multiple times for performance benchmarking.
        int source = json.getInt("source");
        long totalShortestPathTime = 0;
        Metrics shortestPathMetrics = new MetricsImpl();
        for (int i = 0; i < LOOPS; i++) {
            DAGSP dagsp = new DAGSP(graph, source, new MetricsImpl());
            long startTime = System.nanoTime();
            dagsp.shortestPaths();
            long endTime = System.nanoTime();
            totalShortestPathTime += (endTime - startTime);
        }
        DAGSP shortestDagsp = new DAGSP(graph, source, shortestPathMetrics);
        System.out.println("\nShortest Paths from source " + source + ":");
        System.out.println(Arrays.toString(shortestDagsp.shortestPaths()));
        System.out.println("\n--- Metrics Report (Shortest Path) ---");
        System.out.println("Average Execution Time: " + (totalShortestPathTime / LOOPS) + " ns");
        shortestPathMetrics.report();

        // --- Longest Path Analysis ---
        // Uses DAGSP algorithm to compute the longest path distances from the source vertex.
        long totalLongestPathTime = 0;
        Metrics longestPathMetrics = new MetricsImpl();
        for (int i = 0; i < LOOPS; i++) {
            DAGSP dagsp = new DAGSP(graph, source, new MetricsImpl());
            long startTime = System.nanoTime();
            dagsp.longestPaths();
            long endTime = System.nanoTime();
            totalLongestPathTime += (endTime - startTime);
        }
        DAGSP longestDagsp = new DAGSP(graph, source, longestPathMetrics);
        System.out.println("\nLongest Paths from source " + source + ":");
        System.out.println(Arrays.toString(longestDagsp.longestPaths()));
        System.out.println("\n--- Metrics Report (Longest Path) ---");
        System.out.println("Average Execution Time: " + (totalLongestPathTime / LOOPS) + " ns");
        longestPathMetrics.report();
    }
}
