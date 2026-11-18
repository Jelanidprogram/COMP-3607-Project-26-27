package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.IdleEvent;
import java.util.ArrayList;
import java.util.List;

public final class IdleDetector {

    public static final long DEFAULT_IDLE_LIMIT_MILLIS = 180_000L;

    private final long idleLimitMillis;
    private long lastActivity = -1;
    private boolean idle;

    public IdleDetector() {
        this(DEFAULT_IDLE_LIMIT_MILLIS);
    }

    public IdleDetector(long idleLimitMillis) {
        if (idleLimitMillis <= 0) {
            throw new IllegalArgumentException("Idle limit must be positive");
        }
        this.idleLimitMillis = idleLimitMillis;
    }

    public synchronized List<IdleEvent> recordActivity(long timestamp) {
        if (timestamp < 0 || (lastActivity >= 0 && timestamp < lastActivity)) {
            throw new IllegalArgumentException("Activity timestamps must be non-negative and non-decreasing");
        }
        ArrayList<IdleEvent> events = new ArrayList<>();
        if (lastActivity >= 0 && timestamp - lastActivity > idleLimitMillis) {
            if (!idle) {
                events.add(new IdleEvent(lastActivity + idleLimitMillis, true));
            }
            events.add(new IdleEvent(timestamp, false));
            idle = false;
        }
        lastActivity = timestamp;
        return events;
    }

    public synchronized List<IdleEvent> finish(long timestamp) {
        if (timestamp < 0 || (lastActivity >= 0 && timestamp < lastActivity)) {
            throw new IllegalArgumentException("Session end must not precede the last activity");
        }
        ArrayList<IdleEvent> events = new ArrayList<>();
        if (lastActivity >= 0 && !idle && timestamp - lastActivity > idleLimitMillis) {
            events.add(new IdleEvent(lastActivity + idleLimitMillis, true));
            idle = true;
        }
        return events;
    }

}
