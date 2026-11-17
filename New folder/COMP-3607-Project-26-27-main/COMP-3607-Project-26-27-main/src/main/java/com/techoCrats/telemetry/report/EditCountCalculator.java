package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public final class EditCountCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "editCount";
    }

    @Override
    public double calculate(ArrayList<TelemetryEvent> events) {
        return events.stream().filter(EditorEvent.class::isInstance).count();
    }
}
