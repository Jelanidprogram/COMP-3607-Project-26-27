package com.techoCrats.telemetry.model;

import java.util.Objects;

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
        return "{\"" + jsonPrefix() + "\", \"action\": \"" + Json.escape(action)
                + "\", \"name\": \"" + Json.escape(name) + "\"}";
    }
}
