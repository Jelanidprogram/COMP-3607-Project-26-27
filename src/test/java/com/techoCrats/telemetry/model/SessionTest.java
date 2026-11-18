package com.techoCrats.telemetry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SessionTest {

    @Test
    public void builderSetsMetadataAndGeneratedSessionId() {
        Session session = Session.builder()
                .studentId("student-7")
                .startTime(1000)
                .build();

        assertEquals("student-7", session.getStudentId());
        assertEquals(1000, session.getStartTime());
        assertFalse(session.getSessionId().isBlank());
        assertFalse(session.isEnded());
    }

    @Test
    public void eventsAdvanceSessionEndAndEndCanFinalizeIt() {
        Session session = Session.builder().sessionId("s1").studentId("student").startTime(1000).build();
        session.addEvent(new LifecycleEvent(2000, "CLASS_CREATED", "Room"));

        assertEquals(2000, session.getEndTime());
        session.end(5000);
        assertTrue(session.isEnded());
        assertEquals(4, session.getDurationSeconds());
    }
}
