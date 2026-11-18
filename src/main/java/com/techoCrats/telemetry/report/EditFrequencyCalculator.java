package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class EditFrequencyCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "editsPerMinute";
    }

    @Override
    public double calculate(Session session) {
        ArrayList<Long> editTimes = EditTimes.find(session.getEvents());
        if (editTimes.size() < 2) {
            return 0;
        }
        long durationMillis = editTimes.get(editTimes.size() - 1) - editTimes.get(0);
        if (durationMillis <= 0) {
            return 0;
        }
        return editTimes.size() * 60000.0 / durationMillis;
    }
}
