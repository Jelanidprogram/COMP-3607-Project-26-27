package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.Session;
import java.util.ArrayList;
import java.util.List;

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

        public double calculate(Session session) {
            List<Long> edits = session.getEvents().stream()
                    .filter(EditorEvent.class::isInstance)
                    .map(TelemetryEvent::getTimestamp)
                    .sorted()
                    .toList();
            if (edits.isEmpty()) {
                return calculate(session.getEvents());
            }

            long active = 0;
            for (int i = 1; i < edits.size(); i++) {
                long gap = Math.max(0, (edits.get(i) - edits.get(i - 1)) / 1000);
                active += Math.min(gap, IDLE_LIMIT_SECONDS);
            }
            long finalGap = Math.max(0, (session.getEndTime() - edits.get(edits.size() - 1)) / 1000);
            active += Math.min(finalGap, IDLE_LIMIT_SECONDS);
            return active;
    }
}
