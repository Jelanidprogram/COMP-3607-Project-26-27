package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public interface MetricCalculator {

    public String getName();

    public double calculate(ArrayList<TelemetryEvent> events);
}
