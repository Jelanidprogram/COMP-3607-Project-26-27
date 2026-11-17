package com.techoCrats.telemetry.model;

public final class RunEvent extends TelemetryEvent {

    private final boolean successful;
    private final String exception;

    public RunEvent(long timestamp, boolean successful, String exception) {
        super(timestamp, "RUN");
        this.successful = successful;
        this.exception = exception;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getException() {
        return exception;
    }

    public boolean endedWithException() {
        return exception != null;
    }

    @Override
    public String toJson() {
        return "{\"" + jsonPrefix() + "\", \"successful\": " + successful
                + ", \"exception\": " + Json.toJsonValue(exception) + "}";
    }
}
