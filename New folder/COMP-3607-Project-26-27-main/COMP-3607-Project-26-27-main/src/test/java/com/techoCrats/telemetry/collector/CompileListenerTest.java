package com.techoCrats.telemetry.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.techoCrats.telemetry.model.CompileEvent;
import java.io.File;
import org.junit.jupiter.api.Test;

public class CompileListenerTest {

    @Test
    public void recordsSuccessfulCompilationAsOneCompileEvent() {
        EventCollector collector = new EventCollector();
        CompileListener listener = new CompileListener(collector);
        bluej.extensions2.event.CompileEvent event = new bluej.extensions2.event.CompileEvent(
                bluej.extensions2.event.CompileEvent.EventType.COMPILE_DONE_EVENT, new File[0]);

        listener.compileSucceeded(event);

        assertEquals(1, collector.getEvents().size());
        assertTrue(((CompileEvent) collector.getEvents().get(0)).isSuccessful());
    }

    @Test
    public void recordsFailureDetailsWithoutCountingIndividualErrorCallbacksAsAttempts() {
        EventCollector collector = new EventCollector();
        CompileListener listener = new CompileListener(collector);
        bluej.extensions2.event.CompileEvent event = new bluej.extensions2.event.CompileEvent(
                bluej.extensions2.event.CompileEvent.EventType.COMPILE_FAILED_EVENT, new File[0]);
        event.setErrorMessage("';' expected");
        event.setErrorLineNumber(12);

        listener.compileError(event);
        assertEquals(0, collector.getEvents().size());

        listener.compileFailed(event);

        assertEquals(1, collector.getEvents().size());
        CompileEvent recorded = (CompileEvent) collector.getEvents().get(0);
        assertFalse(recorded.isSuccessful());
        assertEquals("COMPILATION_ERROR", recorded.getErrorType());
        assertEquals("';' expected", recorded.getErrorMessage());
        assertEquals(12, recorded.getErrorLine());
    }
}
