package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class IdleTimeCalculator implements MetricCalculator {

    public String getName() {
        return "idleTimeSeconds";
    }

    public double calculate(ArrayList<TelemetryEvent> events) {
        long idle = 0;
        for (int i = 1; i < events.size(); i++) {
            long gap = (events.get(i).getTimestamp() - events.get(i - 1).getTimestamp()) / 1000;
            if (gap > ActiveTimeCalculator.IDLE_LIMIT_SECONDS) {
                idle = idle + (gap - ActiveTimeCalculator.IDLE_LIMIT_SECONDS);
            }
        }
        return idle;
    }
}
