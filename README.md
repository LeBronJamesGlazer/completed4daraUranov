
# Analysis Report

## 1. Data Summary

This report analyzes the performance of several graph algorithms on a variety of datasets. The datasets vary in size (number of vertices and edges) and structure (DAG, dense, multi-SCC). All datasets use the "edge" weight model, where weights are assigned to edges.

| Dataset        | Vertices (n) | Edges (m) | Density | Structure         |
|----------------|--------------|-----------|---------|-------------------|
| small_1.json   | 9            | 13        | 0.18    | Sparse            |
| small_2.json   | 6            | 10        | 0.33    | Sparse            |
| small_3.json   | 8            | 15        | 0.27    | Sparse            |
| medium_1.json  | 20           | 24        | 0.06    | Sparse            |
| medium_2.json  | 13           | 25        | 0.16    | Sparse            |
| medium_3.json  | 14           | 21        | 0.11    | Sparse            |
| large_1.json   | 24           | 49        | 0.09    | Sparse            |
| large_2.json   | 41           | 68        | 0.04    | Sparse            |
| large_3.json   | 29           | 52        | 0.06    | Sparse            |
| dag.json       | 6            | 7         | 0.23    | DAG               |
| dense.json     | 5            | 20        | 1.00    | Dense, Complete   |
| dense_2.json   | 4            | 12        | 1.00    | Dense, Complete   |
| dag_2.json     | 5            | 4         | 0.20    | DAG               |
| multi_scc.json | 8            | 8         | 0.14    | Multiple SCCs     |

*Density is calculated as m / (n * (n - 1)) for directed graphs.*

## 2. Results

The following tables show the performance metrics for each algorithm across the different datasets. The execution time is reported in milliseconds (ms).

### SCC (Strongly Connected Components) using Tarjan's Algorithm

| Dataset        | Execution Time (ms) | DFS Visits | DFS Edges |
|----------------|---------------------|------------|-----------|
| small_1.json   | 0                   | 9          | 13        |
| small_2.json   | 0                   | 6          | 10        |
| small_3.json   | 0                   | 8          | 15        |
| medium_1.json  | 0                   | 20         | 24        |
| medium_2.json  | 0                   | 13         | 25        |
| medium_3.json  | 0                   | 14         | 21        |
| large_1.json   | 0                   | 24         | 49        |
| large_2.json   | 0                   | 41         | 68        |
| large_3.json   | 0                   | 29         | 52        |
| dag.json       | 0                   | 6          | 7         |
| dense.json     | 0                   | 5          | 20        |
| dense_2.json   | 0                   | 4          | 12        |
| dag_2.json     | 0                   | 5          | 4         |
| multi_scc.json | 0                   | 8          | 8         |

### Topological Sort using Kahn's Algorithm

| Dataset        | Execution Time (ms) | Pushes | Pops |
|----------------|---------------------|--------|------|
| small_1.json   | 0                   | 9      | 9    |
| small_2.json   | 0                   | 2      | 2    |
| small_3.json   | 0                   | 5      | 5    |
| medium_1.json  | 0                   | 16     | 16   |
| medium_2.json  | 0                   | 5      | 5    |
| medium_3.json  | 0                   | 13     | 13   |
| large_1.json   | 0                   | 19     | 19   |
| large_2.json   | 0                   | 21     | 21   |
| large_3.json   | 0                   | 14     | 14   |
| dag.json       | 0                   | 6      | 6    |
| dense.json     | 0                   | 1      | 1    |
| dense_2.json   | 0                   | 1      | 1    |
| dag_2.json     | 0                   | 5      | 5    |
| multi_scc.json | 0                   | 4      | 4    |

### DAG Shortest/Longest Path (based on Topological Sort)

| Dataset        | Path      | Execution Time (ms) | Relaxations |
|----------------|-----------|---------------------|-------------|
| dag.json       | Shortest  | 0                   | 5           |
|                | Longest   | 0                   | 7           |
| dag_2.json     | Shortest  | 0                   | 4           |
|                | Longest   | 0                   | 4           |

*Note: The DAG SP algorithm is only applicable to Directed Acyclic Graphs. The current implementation runs on all graphs, but the results are only meaningful for DAGs. For non-DAG graphs, the shortest/longest path results are not computed correctly as the algorithm expects a topological sort of a DAG.*

## 3. Analysis

### Execution Time
The execution time for all algorithms on all datasets was measured as 0 ms. This indicates that the datasets are too small for the `System.nanoTime()` to capture a meaningful difference. For larger graphs, we would expect to see more significant execution times.

### SCC Algorithm
The `DFS Visits` and `DFS Edges` metrics for the SCC algorithm are directly proportional to the number of vertices and edges in the graph, as expected from a DFS-based algorithm. The complexity of Tarjan's algorithm is O(V+E), which is consistent with the collected metrics.

-   **dense.json**: This graph is a single large SCC. The algorithm correctly identifies it.
-   **multi_scc.json**: This graph has multiple SCCs, and the algorithm correctly identifies them.
-   **dag.json**: A DAG has no cycles, so every vertex is its own SCC.

### Topological Sort
The topological sort is performed on the condensation graph. The number of `Pushes` and `Pops` corresponds to the number of SCCs in the graph.

-   **dense.json**: The condensation graph has only one node, so there is only one push and pop.
-   **dag.json**: The condensation graph has the same number of nodes as the original graph, as every vertex is an SCC.

### DAG Shortest/Longest Path
The DAG SP algorithm's performance depends on the number of vertices and edges. The number of `Relaxations` is at most the number of edges in the graph. The results are only meaningful for the `dag.json` dataset. For other graphs, the algorithm runs on the original graph, which might not be a DAG, leading to incorrect results. The implementation should be corrected to only run on DAGs or on the condensation graph.

### Bottlenecks and Structure Effect
-   **SCC**: The main bottleneck for the SCC algorithm is the two DFS traversals. For dense graphs, the number of edges is the dominant factor.
-   **Topological Sort**: This is a very efficient algorithm (O(V+E) on the condensation graph), and it's unlikely to be a bottleneck.
-   **DAG-SP**: The performance is dependent on the topological sort and then iterating through the vertices. The number of relaxations is the main factor, which is tied to the number of edges.

The structure of the graph has a significant impact:
-   **Density**: Dense graphs lead to more edges being traversed in DFS, increasing the runtime of SCC and DAG-SP.
-   **SCC sizes**: The number and size of SCCs affect the condensation graph's size, which in turn affects the topological sort. A graph with many small SCCs will have a larger condensation graph than a graph with one large SCC.

## 4. Conclusions

-   **SCC (Tarjan's Algorithm)**: The implemented SCC algorithm is efficient and correctly identifies strongly connected components. Its performance is directly related to the graph's size (vertices and edges).
-   **Topological Sort (Kahn's Algorithm)**: The topological sort is efficient and works well on the condensation graph.
-   **DAG-SP (Based on Topological Sort)**: The DAG-SP algorithm is efficient for finding shortest and longest paths in DAGs. **However, the current implementation has a flaw where it is run on graphs that are not DAGs, producing meaningless results.** It should be modified to run only on DAGs, or on the condensation graph which is always a DAG.

### Practical Recommendations
-   For general-purpose shortest path calculations on any graph, use Dijkstra's or Bellman-Ford algorithm.
-   If you know your graph is a DAG, the DAG-SP algorithm is more efficient than Dijkstra's or Bellman-Ford.
-   Finding SCCs is a useful first step in analyzing a directed graph, as it simplifies the graph's structure and reveals its cyclical nature. The condensation graph is a powerful tool for further analysis.

## 5. Visualizations

To help visualize the results, here is the data in CSV format, which can be used to generate plots in any spreadsheet or plotting software.

### SCC Metrics (CSV)
```csv
Dataset,Execution Time (ms),DFS Visits,DFS Edges
small_1.json,0,9,13
small_2.json,0,6,10
small_3.json,0,8,15
medium_1.json,0,20,24
medium_2.json,0,13,25
medium_3.json,0,14,21
large_1.json,0,24,49
large_2.json,0,41,68
large_3.json,0,29,52
dag.json,0,6,7
dense.json,0,5,20
dense_2.json,0,4,12
dag_2.json,0,5,4
multi_scc.json,0,8,8
```

### Topological Sort Metrics (CSV)
```csv
Dataset,Execution Time (ms),Pushes,Pops
small_1.json,0,9,9
small_2.json,0,2,2
small_3.json,0,5,5
medium_1.json,0,16,16
medium_2.json,0,5,5
medium_3.json,0,13,13
large_1.json,0,19,19
large_2.json,0,21,21
large_3.json,0,14,14
dag.json,0,6,6
dense.json,0,1,1
dense_2.json,0,1,1
dag_2.json,0,5,5
multi_scc.json,0,4,4
```

### Text-based Bar Charts

Since the execution times are all 0, the bar charts below visualize the `DFS Visits` for SCC and `Pushes` for Topological Sort, as they are more interesting metrics.

**SCC: DFS Visits**
```
small_1   : █████████ (9)
small_2   : ██████ (6)
small_3   : ████████ (8)
medium_1  : ████████████████████ (20)
medium_2  : █████████████ (13)
medium_3  : ██████████████ (14)
large_1   : ████████████████████████ (24)
large_2   : ███████████████████████████████████████ (41)
large_3   : █████████████████████████████ (29)
dag       : ██████ (6)
dag_2     : █████ (5)
dense     : █████ (5)
dense_2   : ████ (4)
multi_scc : ████████ (8)
```

**Topological Sort: Pushes (Number of SCCs)**
```
small_1   : █████████ (9)
small_2   : ██ (2)
small_3   : █████ (5)
medium_1  : ████████████████ (16)
medium_2  : █████ (5)
medium_3  : █████████████ (13)
large_1   : ███████████████████ (19)
large_2   | █████████████████████ (21)
large_3   | ██████████████ (14)
dag       : ██████ (6)
dag_2     : █████ (5)
dense     : █ (1)
dense_2   : █ (1)
multi_scc : ████ (4)
```
