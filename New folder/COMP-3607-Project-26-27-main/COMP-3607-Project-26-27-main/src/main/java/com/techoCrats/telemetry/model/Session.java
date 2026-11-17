package com.techoCrats.telemetry.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public final class Session {

    private final String sessionId;
    private final String studentId;
    private final long startTime;
    private long endTime;
    private final ArrayList<TelemetryEvent> events = new ArrayList<>();

    public Session(String sessionId, String studentId, long startTime) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.studentId = Objects.requireNonNull(studentId, "studentId");
        this.startTime = startTime;
        this.endTime = startTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getStartText() {
        return Instant.ofEpochMilli(startTime).toString();
    }

    public String getEndText() {
        return Instant.ofEpochMilli(endTime).toString();
    }

    public long getDurationSeconds() {
        return (endTime - startTime) / 1000;
    }

    public void addEvent(TelemetryEvent event) {
        events.add(Objects.requireNonNull(event, "event"));
    }

    public ArrayList<TelemetryEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public void end(long timestamp) {
        if (timestamp < startTime) {
            throw new IllegalArgumentException("Session end cannot be before its start");
        }
        endTime = timestamp;
    }
}
