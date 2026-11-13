package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.TelemetryEvent;

@FunctionalInterface
public interface TelemetryEventObserver {
    void onTelemetryEvent(TelemetryEvent event);
}
