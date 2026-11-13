package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.List;

public interface MetricCalculator {
    String getName();
    double calculate(List<TelemetryEvent> events);
}