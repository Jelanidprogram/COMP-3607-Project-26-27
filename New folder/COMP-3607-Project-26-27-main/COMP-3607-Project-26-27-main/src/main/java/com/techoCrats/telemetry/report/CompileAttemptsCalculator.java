package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class CompileAttemptsCalculator implements MetricCalculator {

    public String getName() {
        return "compileAttempts";
    }

    public double calculate(ArrayList<TelemetryEvent> events) {
        int attempts = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("COMPILE")) {
                attempts++;
            }
        }
        return attempts;
    }
}
