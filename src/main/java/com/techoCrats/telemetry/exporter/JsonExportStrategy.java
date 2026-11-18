package com.techoCrats.telemetry.exporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import com.techoCrats.telemetry.report.ReportData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class JsonExportStrategy implements ExportStrategy {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .create();

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public void export(Session session, ReportData report, String filePath) throws IOException {
        Objects.requireNonNull(filePath, "filePath");
        Files.writeString(Path.of(filePath), toJson(session, report), StandardCharsets.UTF_8);
    }

    public String toJson(Session session, ReportData report) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(report, "report");

        LinkedHashMap<String, Object> root = new LinkedHashMap<>();
        root.put("schemaVersion", "1.0");

        LinkedHashMap<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("sessionId", session.getSessionId());
        metadata.put("studentId", session.getStudentId());
        metadata.put("startTime", session.getStartText());
        metadata.put("endTime", session.getEndText());
        metadata.put("durationSeconds", session.getDurationSeconds());
        metadata.put("totalCompilationAttempts", (int) report.getMetric("compileAttempts"));
        root.put("session", metadata);
        root.put("metrics", report.getMetrics());
        root.put("errorCounts", report.getErrorCounts());
        root.put("timeline", report.getTimeline());

        JsonArray events = new JsonArray();
        ArrayList<TelemetryEvent> sessionEvents = session.getEvents();
        for (TelemetryEvent event : sessionEvents) {
            events.add(JsonParser.parseString(event.toJson()));
        }
        root.put("events", events);
        return GSON.toJson(root) + System.lineSeparator();
    }
}
