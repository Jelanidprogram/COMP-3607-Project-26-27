package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import java.util.List;

public final class EditFrequencyCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "editsPerMinute";
    }

    @Override
    public double calculate(ArrayList<TelemetryEvent> events) {
        List<Long> editTimes = events.stream()
                .filter(EditorEvent.class::isInstance)
                .map(TelemetryEvent::getTimestamp)
                .sorted()
                .toList();
        if (editTimes.size() < 2) {
            return 0;
        }
        long durationMillis = editTimes.get(editTimes.size() - 1) - editTimes.get(0);
        if (durationMillis <= 0) {
            return 0;
        }
        return editTimes.size() * 60_000.0 / durationMillis;
    }
}
