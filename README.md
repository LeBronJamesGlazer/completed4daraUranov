Uranov Daryn SE-2403
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

### How to Use
How to Compile
If using terminal:
javac -d out -cp "libs/*" src/main/java/org/example/**/*.java
if using Maven:
mvn clean compile

How to Run
Option 1 — from terminal:
java -cp out org.example.Main
Option 2 — with Maven:
mvn exec:java -Dexec.mainClass="org.example.Main"
Option 3 — from IDE:
Open Main.java → right-click → “Run Main”.

When you run the program, you’ll see a menu:
Choose a dataset to analyze:
1. small
2. medium
3. large
4. specific
5. all
>

Type one of the options (for example, all) and press Enter.
	•	all → analyzes all datasets in /data
	•	specific → analyzes selected graphs (dag.json, dense.json, etc.)
	•	small, medium, large → analyze size-based subsets

The program then performs:
	1.	SCC detection
	2.	Condensation graph construction
	3.	Topological sorting
	4.	Shortest and longest path analysis

Each step reports:
	•	Number of operations (DFS visits, relaxations, pushes/pops)
	•	Average execution time (in nanoseconds)
	•	Identified SCCs and resulting topological order

Results are printed to the terminal and stored in tables for analysis.
## 2. Results

The following tables show the performance metrics for each algorithm across the different datasets. The execution time is reported in milliseconds (ms).

### SCC (Strongly Connected Components) using Tarjan's Algorithm

| Dataset        | Avg. Exec. Time (ns) | DFS Visits | DFS Edges |
|----------------|----------------------|------------|-----------|
| small_1.json   | 2356                 | 9          | 13        |
| small_2.json   | 1548                 | 6          | 10        |
| small_3.json   | 1843                 | 8          | 15        |
| medium_1.json  | 4339                 | 20         | 24        |
| medium_2.json  | 3021                 | 13         | 25        |
| medium_3.json  | 3645                 | 14         | 21        |
| large_1.json   | 8345                 | 24         | 49        |
| large_2.json   | 14898                | 41         | 68        |
| large_3.json   | 10452                | 29         | 52        |
| dag.json       | 1123                 | 6          | 7         |
| dense.json     | 1234                 | 5          | 20        |
| dense_2.json   | 1118                 | 4          | 12        |
| dag_2.json     | 987                  | 5          | 4         |
| multi_scc.json | 1312                 | 8          | 8         |

### Topological Sort using Kahn's Algorithm

| Dataset        | Avg. Exec. Time (ns) | Pushes | Pops |
|----------------|----------------------|--------|------|
| small_1.json   | 1011                 | 9      | 9    |
| small_2.json   | 803                  | 2      | 2    |
| small_3.json   | 912                  | 5      | 5    |
| medium_1.json  | 2034                 | 16     | 16   |
| medium_2.json  | 1567                 | 5      | 5    |
| medium_3.json  | 1843                 | 13     | 13   |
| large_1.json   | 4123                 | 19     | 19   |
| large_2.json   | 7345                 | 21     | 21   |
| large_3.json   | 5221                 | 14     | 14   |
| dag.json       | 543                  | 6      | 6    |
| dense.json     | 612                  | 1      | 1    |
| dense_2.json   | 554                  | 1      | 1    |
| dag_2.json     | 476                  | 5      | 5    |
| multi_scc.json | 723                  | 4      | 4    |


### DAG Shortest/Longest Path (based on Topological Sort)
| Dataset        | Path      | Avg. Exec. Time (ns) | Relaxations |
|----------------|-----------|----------------------|-------------|
| dag.json       | Shortest  | 1134                 | 5           |
|                | Longest   | 1156                 | 7           |
| dag_2.json     | Shortest  | 998                  | 4           |
|                | Longest   | 1012                 | 4           |

*Note: The DAG SP algorithm is only applicable to Directed Acyclic Graphs. The current implementation runs on all graphs, but the results are only meaningful for DAGs. For non-DAG graphs, the shortest/longest path results are not computed correctly as the algorithm expects a topological sort of a DAG.*

## 3. Analysis

### Execution Time
The execution time for all algorithms across all datasets was measured in nanoseconds and averaged over multiple iterations. Even after repeated runs, the measured times remained extremely small, indicating that the datasets are too small for noticeable variation. For larger graphs, we would expect significantly higher and more distinguishable execution times.

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
Or you can see it in plots folder.

### SCC Metrics (CSV)
```csv
Dataset,Avg. Exec. Time (ns),DFS Visits,DFS Edges
small_1.json,2356,9,13
small_2.json,1548,6,10
small_3.json,1843,8,15
medium_1.json,4339,20,24
medium_2.json,3021,13,25
medium_3.json,3645,14,21
large_1.json,8345,24,49
large_2.json,14898,41,68
large_3.json,10452,29,52
dag.json,1123,6,7
dense.json,1234,5,20
dense_2.json,1118,4,12
dag_2.json,987,5,4
multi_scc.json,1312,8,8
```

### Topological Sort Metrics (CSV)
```csv
Dataset,Avg. Exec. Time (ns),Pushes,Pops
small_1.json,1011,9,9
small_2.json,803,2,2
small_3.json,912,5,5
medium_1.json,2034,16,16
medium_2.json,1567,5,5
medium_3.json,1843,13,13
large_1.json,4123,19,19
large_2.json,7345,21,21
large_3.json,5221,14,14
dag.json,543,6,6
dense.json,612,1,1
dense_2.json,554,1,1
dag_2.json,476,5,5
multi_scc.json,723,4,4
```

### Text-based Bar Charts
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
**SCC: Avg. Exec. Time (ns)**
```
small_1   : ██ (2356)
small_2   : █ (1548)
small_3   : █ (1843)
medium_1  : ████ (4339)
medium_2  : ███ (3021)
medium_3  : ███ (3645)
large_1   : ████████ (8345)
large_2   : ███████████████ (14898)
large_3   : ██████████ (10452)
dag       : █ (1123)
dag_2     : █ (987)
dense     : █ (1234)
dense_2   : █ (1118)
multi_scc : █ (1312)
```

**Topological Sort: Avg. Exec. Time (ns)**
```
small_1   : █ (1011)
small_2   : █ (803)
small_3   : █ (912)
medium_1  : ██ (2034)
medium_2  : █ (1567)
medium_3  : █ (1843)
large_1   : ████ (4123)
large_2   : ███████ (7345)
large_3   : █████ (5221)
dag       : █ (543)
dag_2     : █ (476)
dense     : █ (612)
dense_2   : █ (554)
multi_scc : █ (723)
```
