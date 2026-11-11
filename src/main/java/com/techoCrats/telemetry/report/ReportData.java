package com.techoCrats.telemetry.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ReportData {

    private LinkedHashMap<String, Double> metrics;
    private LinkedHashMap<String, Integer> errorCounts;
    private ArrayList<String> timeline;

    public ReportData(LinkedHashMap<String, Double> metrics, LinkedHashMap<String, Integer> errorCounts,
                      ArrayList<String> timeline) {
        this.metrics = metrics;
        this.errorCounts = errorCounts;
        this.timeline = timeline;
    }

    public LinkedHashMap<String, Double> getMetrics() {
        return metrics;
    }

    public double getMetric(String name) {
        if (metrics.containsKey(name)) {
            return metrics.get(name);
        }
        return 0;
    }

    public LinkedHashMap<String, Integer> getErrorCounts() {
        return errorCounts;
    }

    public ArrayList<String> getTimeline() {
        return timeline;
    }
}
