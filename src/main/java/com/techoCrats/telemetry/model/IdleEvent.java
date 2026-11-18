package com.techoCrats.telemetry.model;

import java.util.LinkedHashMap;

public final class IdleEvent extends TelemetryEvent {

    private final boolean idleStarted;

    public IdleEvent(long timestamp, boolean idleStarted) {
        super(timestamp, "IDLE");
        this.idleStarted = idleStarted;
    }

    public boolean isIdleStarted() {
        return idleStarted;
    }

    @Override
    public String toJson() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("type", getType());
        values.put("time", getTimeText());
        values.put("state", idleStarted ? "IDLE_STARTED" : "IDLE_ENDED");
        return Json.stringify(values);
    }
}
