package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class CompileSuccessRateCalculator implements MetricCalculator {

    public String getName() {
        return "compileSuccessRate";
    }

    public double calculate(ArrayList<TelemetryEvent> events) {
        int attempts = 0;
        int successes = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("COMPILE")) {
                attempts++;
                CompileEvent compile = (CompileEvent) event;
                if (compile.isSuccessful()) {
                    successes++;
                }
            }
        }
        if (attempts == 0) {
            return 0;
        }
        return successes * 100.0 / attempts;
    }
}
