package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class EditCountCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "editCount";
    }

    @Override
    public double calculate(Session session) {
        ArrayList<TelemetryEvent> events = session.getEvents();
        int edits = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("EDITOR_EDIT")) {
                edits++;
            }
        }
        return edits;
    }
}
