package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class CompileAttemptsCalculator implements MetricCalculator {

    public String getName() {
        return "compileAttempts";
    }

    public double calculate(Session session) {
        ArrayList<TelemetryEvent> events = session.getEvents();
        int attempts = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("COMPILE")) {
                attempts++;
            }
        }
        return attempts;
    }
}
