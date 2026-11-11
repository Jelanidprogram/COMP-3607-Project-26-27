package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class ActiveTimeCalculator implements MetricCalculator {

    public static final long IDLE_LIMIT_SECONDS = 180;

    public String getName() {
        return "activeTimeSeconds";
    }

    public double calculate(ArrayList<TelemetryEvent> events) {
        long active = 0;
        for (int i = 1; i < events.size(); i++) {
            long gap = (events.get(i).getTimestamp() - events.get(i - 1).getTimestamp()) / 1000;
            if (gap > IDLE_LIMIT_SECONDS) {
                active = active + IDLE_LIMIT_SECONDS;
            } else {
                active = active + gap;
            }
        }
        return active;
    }
}
