// 🧩 Problem: Error Rate Alert System
// 📌 Description
// You are given a sequence of log entries representing HTTP requests. Each log line contains a timestamp and an HTTP status code.
// Your task is to monitor the system’s error rate over time and trigger alerts when the error rate exceeds a threshold.

// 📥 Input
// A list of log entries, where each entry is a string in the format:
// <ISO8601 UTC timestamp> <HTTP status code>

// Example:

// 2025-10-14T10:00:00Z 200

// 📋 Requirements
// Log Format
// Each log line contains:
// ISO8601 UTC timestamp (e.g., 2025-10-14T10:00:00Z)
// HTTP status code (integer)

// Sorted Input
// Logs are sorted in non-decreasing order of timestamp

// Error Definition
// Status codes in [500, 599] are considered errors
// All other codes are successful requests

// Sliding Window
// For each log entry, compute the error rate over the past 5 minutes (300 seconds)

// Error Rate Formula
// error_rate = (number of error requests) / (total requests)

// Alert Trigger
// When error rate first exceeds 1%, trigger an alert
// Output the timestamp of that log entry

// Debouncing Alerts
// Only trigger one alert while error rate stays above 1%

// Trigger another alert only if:
// error rate drops to ≤ 1%
// and later exceeds 1% again

// Invalid Logs
// Ignore malformed log lines (do not crash)
// Output
// Return a list of timestamps (ISO8601 format) when alerts are triggered

// 📤 Example
// Input
// 2025-10-14T10:00:00Z 200
// 2025-10-14T10:00:10Z 200
// 2025-10-14T10:02:00Z 500
// 2025-10-14T10:02:05Z 500
// 2025-10-14T10:06:00Z 200
// Output
// 2025-10-14T10:02:00Z
// 🧠 Function Signature (Java)
// public List<String> detectAlerts(List<String> logs);

// Be careful with:
// time comparison
// floating-point precision
// state transitions (for debouncing)
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class ErrorRateAlert {

        private final long windowSeconds;
        private final double threshold;

        public ErrorRateAlert(long windowSize, double threshold){
            this.windowSeconds = windowSize;
            this.threshold = threshold;
        }

    private static class LogEntry {
        Instant time;
        boolean isError;
        String originalTimestamp;

        LogEntry(Instant time, boolean isError, String originalTimestamp) {
            this.time = time;
            this.isError = isError;
            this.originalTimestamp = originalTimestamp;
        }
    }


    /**
     * Extended version:
     * allows configurable window size for testing/interview discussion.
     */
    public List<String> detectAlerts(List<String> logs) {
        List<String> alerts = new ArrayList<>();

        if (logs == null || logs.isEmpty()) {
            return alerts;
        }

        Deque<LogEntry> window = new ArrayDeque<>();
        int totalRequests = 0;
        int errorRequests = 0;

        // false = currently not in alert state
        // true  = currently already alerting
        boolean alerting = false;

        for (String log : logs) {
            LogEntry current = parseLog(log);

            // Ignore malformed log lines
            if (current == null) {
                continue;
            }

            Instant currentTime = current.time;

            // Remove logs that are older than the time window
            // Keep logs within [currentTime - windowSeconds, currentTime]
            while (!window.isEmpty()
                    && Duration.between(window.peekFirst().time, currentTime).getSeconds() > windowSeconds) {
                LogEntry expired = window.pollFirst();
                totalRequests--;
                if (expired.isError) {
                    errorRequests--;
                }
            }

            // Add current log into the window
            window.offerLast(current);
            totalRequests++;
            if (current.isError) {
                errorRequests++;
            }

            // Safety check: should never be 0 after adding current log, but keep it robust
            if (totalRequests == 0) {
                continue;
            }

            double currentRate = (double) errorRequests / totalRequests;

            // Enter alert state only when crossing from non-alerting to alerting
            if (!alerting && currentRate > threshold) {
                alerts.add(current.originalTimestamp);
                alerting = true;
            }
            // Exit alert state when rate drops back to <= threshold
            else if (alerting && currentRate <= threshold) {
                alerting = false;
            }
        }

        return alerts;
    }

    /**
     * Parses one log line.
     * Expected format:
     * <ISO8601 UTC timestamp> <HTTP status code>
     *
     * Returns null if malformed.
     */
    private LogEntry parseLog(String log) {
        if (log == null) {
            return null;
        }

        log = log.trim();
        if (log.isEmpty()) {
            return null;
        }

        String[] parts = log.split("\\s+");
        if (parts.length != 2) {
            return null;
        }

        String timestampStr = parts[0];
        String statusCodeStr = parts[1];

        Instant time;
        int statusCode;

        try {
            time = Instant.parse(timestampStr);
        } catch (DateTimeParseException e) {
            return null;
        }

        try {
            statusCode = Integer.parseInt(statusCodeStr);
        } catch (NumberFormatException e) {
            return null;
        }

        // You can decide whether to reject impossible HTTP codes.
        // Here we allow any integer and only treat 500-599 as error.
        boolean isError = statusCode >= 500 && statusCode <= 599;

        return new LogEntry(time, isError, timestampStr);
    }

        // ---- Public API: streaming style ----
            private final Deque<LogEntry> deque = new ArrayDeque<>();
    private int total = 0;
    private int errors = 0;
    private boolean alerting = false;
    public synchronized String process(String logLine) {
        LogEntry entry = parseLog(logLine);
        if (entry == null) return null;

        Instant now = entry.time;

        // Remove expired
        while (!deque.isEmpty() &&
                Duration.between(deque.peekFirst().time, now).getSeconds() > windowSeconds) {
            LogEntry old = deque.pollFirst();
            total--;
            if (old.isError) errors--;
        }

        // Add new
        deque.offerLast(entry);
        total++;
        if (entry.isError) errors++;

        double rate = (double) errors / total;

        // State machine
        if (!alerting && rate > threshold) {
            alerting = true;
            return entry.originalTimestamp; // trigger alert
        } else if (alerting && rate <= threshold) {
            alerting = false;
        }

        return null;
    }


    public static void main(String[] args) {
        ErrorRateAlert solution = new ErrorRateAlert(300,0.01);

        List<String> logs1 = Arrays.asList(
                "2025-10-14T10:00:00Z 200",
                "2025-10-14T10:00:10Z 200",
                "2025-10-14T10:02:00Z 500",
                "2025-10-14T10:02:05Z 500",
                "2025-10-14T10:06:00Z 200"
        );

        List<String> result1 = solution.detectAlerts(logs1);
        System.out.println("Test 1 alerts: " + result1);

        List<String> logs2 = Arrays.asList(
                "2025-10-14T10:00:00Z 200",
                "2025-10-14T10:00:01Z 500", // alert triggered here: 1/2 = 50%
                "2025-10-14T10:00:02Z 500", // still alerting, no duplicate alert
                "2025-10-14T10:10:00Z 200", // old error logs expire, rate may drop
                "2025-10-14T10:10:01Z 500"  // may trigger again after dropping
        );

        List<String> result2 = solution.detectAlerts(logs2);
        System.out.println("Test 2 alerts: " + result2);

        List<String> logs3 = Arrays.asList(
                "2025-10-14T10:00:00Z 200",
                "bad log line",
                "2025-10-14T10:00:05Z abc",
                "2025-10-14T10:00:10Z 503",
                "   ",
                null,
                "2025-10-14T10:00:20Z 200"
        );

        List<String> result3 = solution.detectAlerts(logs3);
        System.out.println("Test 3 alerts: " + result3);

        List<String> logs4 = Arrays.asList(
                "2025-10-14T10:00:00Z 500"
        );

        List<String> result4 = solution.detectAlerts(logs4);
        System.out.println("Test 4 alerts: " + result4);

        List<String> logs5 = Arrays.asList(
                "2025-10-14T10:00:00Z 200",
                "2025-10-14T10:04:59Z 500",
                "2025-10-14T10:05:01Z 200",
                "2025-10-14T10:10:02Z 500"
        );

        List<String> result5 = solution.detectAlerts(logs5);
        System.out.println("Test 5 alerts: " + result5);
    }
}
