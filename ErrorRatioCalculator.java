package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorRatioCalculator implements MetricCalculator {

    private Map<String, Integer> errorBreakdown = new HashMap<>();

    @Override
    public String getName() {
        return "compilationErrorRatio";
    }

    @Override
    public double calculate(List<TelemetryEvent> events) {
        errorBreakdown.clear();
        if (events == null || events.isEmpty()) {
            return 0.0;
        }

        int totalCompiles = 0;
        int errorCompiles = 0;

        for (TelemetryEvent event : events) {
            if ("COMPILE".equals(event.getType()) && event instanceof CompileEvent) {
                totalCompiles++;
                CompileEvent compile = (CompileEvent) event;
                if (!compile.isSuccessful()) {
                    errorCompiles++;
                    String errorCode = compile.getErrorCode() != null ? compile.getErrorCode() : "UNKNOWN_ERROR";
                    errorBreakdown.put(errorCode, errorBreakdown.getOrDefault(errorCode, 0) + 1);
                }
            }
        }

        if (totalCompiles == 0) {
            return 0.0;
        }

        return (double) errorCompiles / totalCompiles * 100.0;
    }

    public Map<String, Integer> getErrorBreakdown() {
        return errorBreakdown;
    }
}