package com.techoCrats.telemetry.report;

import static org.junit.jupiter.api.Assertions.*;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import com.techoCrats.telemetry.model.RunEvent;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetricCalculatorTest {

    private ArrayList<TelemetryEvent> events;

    private long seconds(int s) {
        return s * 1000L;
    }

    @BeforeEach
    public void setUp() {
        events = new ArrayList<TelemetryEvent>();
        events.add(new LifecycleEvent(seconds(0), "CLASS_CREATED", "Room"));
        events.add(new CompileEvent(seconds(60), false, "MISSING_SEMICOLON", "';' expected", 14));
        events.add(new CompileEvent(seconds(120), true, null, null, 0));
        events.add(new RunEvent(seconds(150), true, "NullPointerException"));
        events.add(new CompileEvent(seconds(650), true, null, null, 0));
        events.add(new RunEvent(seconds(700), false, null));
    }

    @Test
    public void activeTimeCapsLongGapsAt180Seconds() {
        // gaps: 60, 60, 30, 500 (counts as 180), 50
        assertEquals(380, new ActiveTimeCalculator().calculate(events), 0.001);
    }

    @Test
    public void idleTimeIsTheRestOfLongGaps() {
        assertEquals(320, new IdleTimeCalculator().calculate(events), 0.001);
    }

    @Test
    public void countsCompileAttempts() {
        assertEquals(3, new CompileAttemptsCalculator().calculate(events), 0.001);
    }

    @Test
    public void compileSuccessRateIsAPercentage() {
        assertEquals(66.667, new CompileSuccessRateCalculator().calculate(events), 0.001);
    }

    @Test
    public void errorRatioIsPercentageOfRunsWithExceptions() {
        assertEquals(50, new ErrorRatioCalculator().calculate(events), 0.001);
    }

    @Test
    public void emptyListGivesZeroForEveryCalculator() {
        ArrayList<TelemetryEvent> empty = new ArrayList<TelemetryEvent>();
        assertEquals(0, new ActiveTimeCalculator().calculate(empty), 0.001);
        assertEquals(0, new IdleTimeCalculator().calculate(empty), 0.001);
        assertEquals(0, new CompileAttemptsCalculator().calculate(empty), 0.001);
        assertEquals(0, new CompileSuccessRateCalculator().calculate(empty), 0.001);
        assertEquals(0, new ErrorRatioCalculator().calculate(empty), 0.001);
    }

    @Test
    public void aggregatorRunsEveryCalculator() {
        Session session = new Session("s1", "816000000", 0);
        for (TelemetryEvent event : events) {
            session.addEvent(event);
        }
        ReportData report = new ReportAggregator().aggregate(session);

        assertEquals(8, report.getMetrics().size());
        assertEquals(380, report.getMetric("activeTimeSeconds"), 0.001);
        assertEquals(1, (int) report.getErrorCounts().get("MISSING_SEMICOLON"));
        assertEquals(1, report.getTimeline().size());
        assertTrue(report.getTimeline().get(0).contains("Room"));
    }

    @Test
    public void editorEventsDriveActiveIdleAndTypingMetrics() {
        Session session = Session.builder().sessionId("s1").studentId("student").startTime(0).build();
        session.addEvent(new EditorEvent(seconds(1), "Room", 0, 0, 4));
        session.addEvent(new EditorEvent(seconds(121), "Room", 4, 0, 2));
        session.addEvent(new EditorEvent(seconds(421), "Room", 6, 1, 0));
        session.end(seconds(500));

        ReportData report = new ReportAggregator().aggregate(session);

        assertEquals(379, report.getMetric("activeTimeSeconds"), 0.001);
        assertEquals(120, report.getMetric("idleTimeSeconds"), 0.001);
        assertEquals(3, report.getMetric("editCount"), 0.001);
        assertEquals(6, report.getMetric("charactersInserted"), 0.001);
        assertEquals(3.0 / 7.0, report.getMetric("editsPerMinute"), 0.001);
    }

    @Test
    public void aggregatorAcceptsANewCalculator() {
        ReportAggregator aggregator = new ReportAggregator();
        aggregator.addCalculator(new MetricCalculator() {
            public String getName() {
                return "eventCount";
            }

            public double calculate(ArrayList<TelemetryEvent> list) {
                return list.size();
            }
        });
        Session session = new Session("s1", "816000000", 0);
        session.addEvent(new RunEvent(seconds(5), false, null));

        ReportData report = aggregator.aggregate(session);

        assertEquals(1, report.getMetric("eventCount"), 0.001);
        assertEquals(0, report.getMetric("notAMetric"), 0.001);
    }
}
