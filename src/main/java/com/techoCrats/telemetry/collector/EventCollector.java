package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventCollector {

    private final CopyOnWriteArrayList<TelemetryEvent> events = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<TelemetryEventObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(TelemetryEventObserver observer) {
        observers.addIfAbsent(Objects.requireNonNull(observer, "observer"));
    }

    public void removeObserver(TelemetryEventObserver observer) {
        observers.remove(Objects.requireNonNull(observer, "observer"));
    }

    public void collect(TelemetryEvent event) {
        TelemetryEvent collectedEvent = Objects.requireNonNull(event, "event");
        events.add(collectedEvent);
        for (TelemetryEventObserver observer : observers) {
            observer.onTelemetryEvent(collectedEvent);
        }
    }

    public List<TelemetryEvent> getEvents() {
        return List.copyOf(events);
    }
}
