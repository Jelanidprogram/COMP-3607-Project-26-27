package com.techoCrats.telemetry.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.IdleEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class EditorTelemetryTrackerTest {

    @Test
    public void editEventsKeepCountsWithoutStoringSourceTextAndRecordAddedMethods() {
        EventCollector collector = new EventCollector();
        ArrayList<TelemetryEvent> observed = new ArrayList<>();
        collector.addObserver(observed::add);
        EditorTelemetryTracker tracker = new EditorTelemetryTracker(collector, new IdleDetector(180_000));
        String initialSource = "class Room {\n}\n";
        String editedSource = "class Room {\n    public void clean() {\n    }\n}\n";

        tracker.recordEdit("Room", 0, "", "ab", initialSource, 1000);
        tracker.recordEdit("Room", 15, "", "method", editedSource, 2000);

        assertEquals(2, observed.stream().filter(EditorEvent.class::isInstance).count());
        LifecycleEvent methodAdded = observed.stream()
                .filter(LifecycleEvent.class::isInstance)
                .map(LifecycleEvent.class::cast)
                .findFirst()
                .orElseThrow();
        assertEquals("METHOD_ADDED", methodAdded.getAction());
        assertTrue(methodAdded.getName().contains("clean()"));
        assertFalse(observed.get(2).toJson().contains("public void"));
        assertEquals(2, ((EditorEvent) observed.get(0)).getInsertedCharacters());
    }

    @Test
    public void longEditGapEmitsIdleStartAndResumeEvents() {
        IdleDetector detector = new IdleDetector(1000);

        assertTrue(detector.recordActivity(100).isEmpty());
        var transition = detector.recordActivity(1500);

        assertEquals(2, transition.size());
        assertTrue(transition.get(0).isIdleStarted());
        assertFalse(transition.get(1).isIdleStarted());
        assertEquals(1100, transition.get(0).getTimestamp());
    }

    @Test
    public void sessionEndRecordsWhenIdleBegins() {
        IdleDetector detector = new IdleDetector(1000);
        detector.recordActivity(100);

        var events = detector.finish(1200);

        assertEquals(1, events.size());
        assertTrue(events.get(0).isIdleStarted());
        assertTrue(events.get(0).toJson().contains("\"state\":\"IDLE_STARTED\""));
    }
}
