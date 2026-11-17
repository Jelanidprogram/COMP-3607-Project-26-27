package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.LifecycleEvent;
import java.util.Objects;

public final class LifecycleListener implements bluej.extensions2.event.PackageListener,
        bluej.extensions2.event.ClassListener {

    private final EventCollector collector;

    public LifecycleListener(EventCollector collector) {
        this.collector = Objects.requireNonNull(collector, "collector");
    }

    @Override
    public void packageOpened(bluej.extensions2.event.PackageEvent event) {
        record("PACKAGE_OPENED", event.getPackage().toString());
    }

    @Override
    public void packageClosing(bluej.extensions2.event.PackageEvent event) {
        record("PACKAGE_CLOSING", event.getPackage().toString());
    }

    @Override
    public void classStateChanged(bluej.extensions2.event.ClassEvent event) {
        record("CLASS_STATE_CHANGED", event.getBClass().getName());
    }

    @Override
    public void classNameChanged(bluej.extensions2.event.ClassEvent event) {
        record("CLASS_RENAMED", event.getOldName() + " -> " + event.getBClass().getName());
    }

    @Override
    public void classRemoved(bluej.extensions2.event.ClassEvent event) {
        record("CLASS_REMOVED", event.getBClass().getName());
    }

    private void record(String action, String name) {
        collector.collect(new LifecycleEvent(System.currentTimeMillis(), action, name));
    }
}
