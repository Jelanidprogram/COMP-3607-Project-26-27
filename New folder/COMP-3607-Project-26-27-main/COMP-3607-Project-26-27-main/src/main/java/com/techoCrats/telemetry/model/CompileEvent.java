package com.techoCrats.telemetry.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class CompileEvent extends TelemetryEvent {

    private final boolean successful;
    private final String errorType;
    private final String errorMessage;
    private final int errorLine;
    private final List<String> sourceFiles;

    public CompileEvent(long timestamp, boolean successful, String errorType, String errorMessage, int errorLine) {
        this(timestamp, successful, errorType, errorMessage, errorLine, List.of());
    }

    public CompileEvent(long timestamp, boolean successful, String errorType, String errorMessage, int errorLine,
                        List<String> sourceFiles) {
        super(timestamp, "COMPILE");
        this.successful = successful;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorLine = errorLine;
        this.sourceFiles = List.copyOf(sourceFiles);
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

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    @Override
    public String toJson() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("type", getType());
        values.put("time", getTimeText());
        values.put("successful", successful);
        values.put("errorCode", errorType);
        values.put("errorMessage", errorMessage);
        values.put("errorLine", errorLine);
        values.put("sourceFiles", new ArrayList<>(sourceFiles));
        return Json.stringify(values);
    }
}
