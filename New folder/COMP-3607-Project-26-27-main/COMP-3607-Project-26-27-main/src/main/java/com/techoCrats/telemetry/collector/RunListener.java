package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.RunEvent;
import java.util.Objects;

public final class RunListener implements bluej.extensions2.event.InvocationFinishedListener {

    private final EventCollector collector;

    public RunListener(EventCollector collector) {
        this.collector = Objects.requireNonNull(collector, "collector");
    }

    @Override
    public void invocationFinished(bluej.extensions2.event.InvocationFinishedEvent event) {
        bluej.extensions2.event.InvocationFinishedEvent.EventType eventType = event.getEventType();
        boolean successful = eventType == bluej.extensions2.event.InvocationFinishedEvent.EventType.NORMAL_EXIT;
        String exception = eventType == bluej.extensions2.event.InvocationFinishedEvent.EventType.EXCEPTION_EXIT
                ? eventType.name() : null;
        collector.collect(new RunEvent(System.currentTimeMillis(), successful, exception));
    }
}
