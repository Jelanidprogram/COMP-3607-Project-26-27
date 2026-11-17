package com.techoCrats.telemetry.model;

import java.time.Instant;
import java.util.Objects;

public abstract class TelemetryEvent {

    private final long timestamp;
    private final String type;

    protected TelemetryEvent(long timestamp, String type) {
        this.timestamp = timestamp;
        this.type = Objects.requireNonNull(type, "type");
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getTimeText() {
        return Instant.ofEpochMilli(timestamp).toString();
    }

    public abstract String toJson();

    protected String jsonPrefix() {
        return "\"type\": \"" + Json.escape(type) + "\", \"time\": \""
                + Json.escape(getTimeText()) + "\"";
    }
}
