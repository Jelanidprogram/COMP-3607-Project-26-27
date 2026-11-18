package com.techoCrats.telemetry.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class Session {

    private final String sessionId;
    private final String studentId;
    private final long startTime;
    private long endTime;
    private final ArrayList<TelemetryEvent> events = new ArrayList<>();
    private boolean ended;

    public Session(String sessionId, String studentId, long startTime) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.studentId = Objects.requireNonNull(studentId, "studentId");
        this.startTime = startTime;
        this.endTime = startTime;
    }

    public static Builder builder() {
        return new Builder();
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

    public synchronized long getEndTime() {
        return endTime;
    }

    public String getStartText() {
        return Instant.ofEpochMilli(startTime).toString();
    }

    public synchronized String getEndText() {
        return Instant.ofEpochMilli(endTime).toString();
    }

    public synchronized long getDurationSeconds() {
        return (endTime - startTime) / 1000;
    }

    public synchronized void addEvent(TelemetryEvent event) {
        TelemetryEvent addedEvent = Objects.requireNonNull(event, "event");
        events.add(addedEvent);
        if (addedEvent.getTimestamp() > endTime) {
            endTime = addedEvent.getTimestamp();
        }
    }

    public synchronized ArrayList<TelemetryEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public synchronized void end(long timestamp) {
        updateEndTime(timestamp);
        ended = true;
    }

    public synchronized void updateEndTime(long timestamp) {
        if (timestamp < startTime || timestamp < endTime) {
            throw new IllegalArgumentException("Session end must not precede its start or latest event");
        }
        endTime = timestamp;
    }

    public synchronized boolean isEnded() {
        return ended;
    }

    public static final class Builder {
        private String sessionId = UUID.randomUUID().toString();
        private String studentId = "student-placeholder";
        private long startTime = System.currentTimeMillis();

        private Builder() {
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
            return this;
        }

        public Builder studentId(String studentId) {
            this.studentId = Objects.requireNonNull(studentId, "studentId");
            return this;
        }

        public Builder startTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Session build() {
            return new Session(sessionId, studentId, startTime);
        }
    }
}
