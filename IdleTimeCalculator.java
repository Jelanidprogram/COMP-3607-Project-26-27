package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.List;

public class IdleTimeCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "idleTime";
    }

    @Override
    public double calculate(List<TelemetryEvent> events) {
        if (events == null || events.isEmpty()) {
            return 0.0;
        }

        long idleMillis = 0;
        TelemetryEvent previous = null;

        for (TelemetryEvent event : events) {
            if (previous != null) {
                long diff = event.getTimestamp() - previous.getTimestamp();
                // Gaps of 5 minutes or more count as idle time
                if (diff >= 300000) {
                    idleMillis += diff;
                }
            }
            previous = event;
        }

        return idleMillis / 60000.0;
    }
}