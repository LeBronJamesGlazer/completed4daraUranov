package org.example.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Metrics interface.
 * Tracks algorithm performance by measuring execution time and counting operations.
 */
public class MetricsImpl implements Metrics {
    private long startTime;
    private long endTime;
    private Map<String, Long> counters = new HashMap<>();

    @Override
    public void start() {
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        endTime = System.nanoTime();
    }

    @Override
    public void incrementCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) + 1);
    }

    @Override
    public long getCounter(String name) {
        return counters.getOrDefault(name, 0L);
    }

    @Override
    public long getElapsedTime() {
        return endTime - startTime;
    }

    @Override
    public void report() {
        System.out.println("\n--- Metrics Report ---");
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("----------------------");
    }
}
