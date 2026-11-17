package com.techoCrats.telemetry.model;

public final class CompileEvent extends TelemetryEvent {

    private final boolean successful;
    private final String errorType;
    private final String errorMessage;
    private final int errorLine;

    public CompileEvent(long timestamp, boolean successful, String errorType, String errorMessage, int errorLine) {
        super(timestamp, "COMPILE");
        this.successful = successful;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorLine = errorLine;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorLine() {
        return errorLine;
    }

    @Override
    public String toJson() {
        return "{\"" + jsonPrefix() + "\", \"successful\": " + successful
                + ", \"errorType\": " + Json.toJsonValue(errorType)
                + ", \"errorMessage\": " + Json.toJsonValue(errorMessage)
                + ", \"errorLine\": " + errorLine + "}";
    }
}
