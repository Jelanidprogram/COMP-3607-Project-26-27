package com.techoCrats.telemetry.model;

import java.util.LinkedHashMap;

public final class RunEvent extends TelemetryEvent {

    private final boolean successful;
    private final String exception;
    private final String className;
    private final String methodName;
    private final String resultSummary;

    public RunEvent(long timestamp, boolean successful, String exception) {
        this(timestamp, successful, exception, null, null, null);
    }

    public RunEvent(long timestamp, boolean successful, String exception, String className,
                    String methodName, String resultSummary) {
        super(timestamp, "RUN");
        this.successful = successful;
        this.exception = exception;
        this.className = className;
        this.methodName = methodName;
        this.resultSummary = resultSummary;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getException() {
        return exception;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public boolean endedWithException() {
        return exception != null;
    }

    @Override
    public String toJson() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("type", getType());
        values.put("time", getTimeText());
        values.put("successful", successful);
        values.put("exception", exception);
        values.put("className", className);
        values.put("methodName", methodName);
        values.put("resultSummary", resultSummary);
        return Json.stringify(values);
    }
}
