package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.Session;
import java.util.ArrayList;
import java.util.List;

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

    public double calculate(Session session) {
        List<Long> edits = session.getEvents().stream()
                .filter(EditorEvent.class::isInstance)
                .map(TelemetryEvent::getTimestamp)
                .sorted()
                .toList();
        if (edits.isEmpty()) {
            return calculate(session.getEvents());
        }

        long idle = 0;
        for (int i = 1; i < edits.size(); i++) {
            long gap = Math.max(0, (edits.get(i) - edits.get(i - 1)) / 1000);
            idle += Math.max(0, gap - ActiveTimeCalculator.IDLE_LIMIT_SECONDS);
        }
        long finalGap = Math.max(0, (session.getEndTime() - edits.get(edits.size() - 1)) / 1000);
        idle += Math.max(0, finalGap - ActiveTimeCalculator.IDLE_LIMIT_SECONDS);
        return idle;
    }
}
