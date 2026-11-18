package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class CharactersInsertedCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "charactersInserted";
    }

    @Override
    public double calculate(Session session) {
        ArrayList<TelemetryEvent> events = session.getEvents();
        int inserted = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("EDITOR_EDIT")) {
                EditorEvent edit = (EditorEvent) event;
                inserted = inserted + edit.getInsertedCharacters();
            }
        }
        return inserted;
    }
}
