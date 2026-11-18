package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public final class CharactersInsertedCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "charactersInserted";
    }

    @Override
    public double calculate(ArrayList<TelemetryEvent> events) {
        return events.stream()
                .filter(EditorEvent.class::isInstance)
                .map(EditorEvent.class::cast)
                .mapToInt(EditorEvent::getInsertedCharacters)
                .sum();
    }
}
