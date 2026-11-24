package com.techoCrats.telemetry.model;

import java.util.Objects;
import java.util.LinkedHashMap;

public final class LifecycleEvent extends TelemetryEvent {

    private final String action;
    private final String name;

    public LifecycleEvent(long timestamp, String action, String name) {
        super(timestamp, "LIFECYCLE");
        this.action = Objects.requireNonNull(action, "action");
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toJson() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("type", getType());
        values.put("time", getTimeText());
        values.put("action", action);
        values.put("name", name);
        return Json.stringify(values);
    }
}
