package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.RunEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;

public class ErrorRatioCalculator implements MetricCalculator {

    public String getName() {
        return "runtimeErrorRatio";
    }

    public double calculate(ArrayList<TelemetryEvent> events) {
        int runs = 0;
        int errors = 0;
        for (TelemetryEvent event : events) {
            if (event.getType().equals("RUN")) {
                runs++;
                RunEvent run = (RunEvent) event;
                if (run.endedWithException()) {
                    errors++;
                }
            }
        }
        if (runs == 0) {
            return 0;
        }
        return errors * 100.0 / runs;
    }
}
