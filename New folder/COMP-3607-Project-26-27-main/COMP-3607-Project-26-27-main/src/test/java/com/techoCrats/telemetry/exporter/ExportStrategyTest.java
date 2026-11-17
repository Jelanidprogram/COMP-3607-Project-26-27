package com.techoCrats.telemetry.exporter;

import static org.junit.jupiter.api.Assertions.*;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportAggregator;
import com.techoCrats.telemetry.report.ReportData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExportStrategyTest {

    private Session session;

    @BeforeEach
    public void setUp() {
        session = new Session("s1", "816000000", 0);
        session.addEvent(new LifecycleEvent(10000, "CLASS_CREATED", "Room"));
        session.addEvent(new CompileEvent(20000, false, "MISSING_SEMICOLON", "\"x\" expected", 14));
        session.addEvent(new CompileEvent(30000, true, null, null, 0));
        session.end(60000);
    }

    @Test
    public void jsonHasSessionMetadataMetricsAndEvents() {
        ReportData report = new ReportAggregator().aggregate(session);
        String json = new JsonExportStrategy().toJson(session, report);

        assertTrue(json.contains("\"studentId\": \"816000000\""));
        assertTrue(json.contains("\"durationSeconds\": 60"));
        assertTrue(json.contains("\"totalCompilations\": 2"));
        assertTrue(json.contains("\"compileSuccessRate\": 50.0"));
        assertTrue(json.contains("\"MISSING_SEMICOLON\": 1"));
        assertTrue(json.contains("\"time\": \"1970-01-01T00:00:20Z\""));
        assertTrue(json.contains("\\\"x\\\" expected"));
    }

    @Test
    public void jsonBracketsAreBalanced() {
        String json = new JsonExportStrategy().toJson(session, new ReportAggregator().aggregate(session));
        int braces = 0;
        int brackets = 0;
        boolean inString = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"' && json.charAt(i - 1) != '\\') {
                inString = !inString;
            } else if (!inString) {
                if (c == '{') braces++;
                if (c == '}') braces--;
                if (c == '[') brackets++;
                if (c == ']') brackets--;
            }
        }
        assertEquals(0, braces);
        assertEquals(0, brackets);
    }

    @Test
    public void exporterUsesTheCurrentStrategy() throws IOException {
        String folder = Files.createTempDirectory("telemetry").toString();
        TelemetryExporter exporter = new TelemetryExporter(new JsonExportStrategy());

        String path = exporter.export(session, folder);

        assertTrue(path.endsWith("telemetry-s1.json"));
        assertTrue(new File(path).exists());
    }

    @Test
    public void strategyCanBeSwapped() throws IOException {
        String folder = Files.createTempDirectory("telemetry").toString();
        TelemetryExporter exporter = new TelemetryExporter(new JsonExportStrategy());
        exporter.setStrategy(new ExportStrategy() {
            public String getFileExtension() {
                return "txt";
            }

            public void export(Session s, ReportData report, String filePath) throws IOException {
                Files.writeString(new File(filePath).toPath(), "report for " + s.getSessionId());
            }
        });

        String path = exporter.export(session, folder);

        assertTrue(path.endsWith("telemetry-s1.txt"));
        assertEquals("report for s1", Files.readString(new File(path).toPath()));
    }

    @Test
    public void extensionsMatchFormats() {
        assertEquals("json", new JsonExportStrategy().getFileExtension());
        assertEquals("pdf", new PdfExportStrategy().getFileExtension());
    }
}
