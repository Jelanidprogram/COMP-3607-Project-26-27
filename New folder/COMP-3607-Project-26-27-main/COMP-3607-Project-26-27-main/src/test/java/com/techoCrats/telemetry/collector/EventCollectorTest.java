package com.techoCrats.telemetry.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class EventCollectorTest {

    @Test
    public void collectingNotifiesRegisteredObserversAndStoresEvent() {
        EventCollector collector = new EventCollector();
        ArrayList<TelemetryEvent> observed = new ArrayList<>();
        collector.addObserver(observed::add);
        CompileEvent event = new CompileEvent(1000, true, null, null, 0);

        collector.collect(event);

        assertEquals(1, collector.getEvents().size());
        assertSame(event, collector.getEvents().get(0));
        assertSame(event, observed.get(0));
    }

    @Test
    public void removedObserverNoLongerReceivesEvents() {
        EventCollector collector = new EventCollector();
        ArrayList<TelemetryEvent> observed = new ArrayList<>();
        TelemetryEventObserver observer = observed::add;
        collector.addObserver(observer);
        collector.removeObserver(observer);

        collector.collect(new CompileEvent(1000, true, null, null, 0));

        assertEquals(0, observed.size());
        assertEquals(1, collector.getEvents().size());
    }
}
