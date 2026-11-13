package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.List;

public class ActiveTimeCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "activeTime";
    }

    @Override
    public double calculate(List<TelemetryEvent> events) {
        if (events == null || events.isEmpty()) {
            return 0.0;
        }

        long activeMillis = 0;
        TelemetryEvent previous = null;

        for (TelemetryEvent event : events) {
            if (previous != null) {
                long diff = event.getTimestamp() - previous.getTimestamp();
                // Count gaps under 5 minutes (300,000 ms) as active coding time
                if (diff > 0 && diff < 300000) {
                    activeMillis += diff;
                }
            }
            previous = event;
        }

        // Return active time in minutes
        return activeMillis / 60000.0;
    }
}