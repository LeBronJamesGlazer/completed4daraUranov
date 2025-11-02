package org.example.metrics;

/**
 * Interface for collecting and reporting algorithm performance metrics.
 * Provides methods for timing and counting key algorithm operations.
 */
public interface Metrics {
    void start();
    void stop();

    // Used to track how many times a specific operation occurs (e.g., relaxations)
    void incrementCounter(String name);

    long getCounter(String name);

    long getElapsedTime();

    // Outputs a short report summarizing performance results
    void report();
}
