package com.techoCrats.telemetry.exporter;

import com.techoCrats.telemetry.model.Json;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import com.techoCrats.telemetry.report.ReportData;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JsonExportStrategy implements ExportStrategy {

    public String getFileExtension() {
        return "json";
    }

    public void export(Session session, ReportData report, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(toJson(session, report));
        writer.close();
    }

    public String toJson(Session session, ReportData report) {
        String json = "{\n";

        json = json + "  \"session\": {\n";
        json = json + "    \"sessionId\": \"" + Json.escape(session.getSessionId()) + "\",\n";
        json = json + "    \"studentId\": \"" + Json.escape(session.getStudentId()) + "\",\n";
        json = json + "    \"startTime\": \"" + session.getStartText() + "\",\n";
        json = json + "    \"endTime\": \"" + session.getEndText() + "\",\n";
        json = json + "    \"durationSeconds\": " + session.getDurationSeconds() + ",\n";
        json = json + "    \"totalCompilations\": " + (int) report.getMetric("compileAttempts") + "\n";
        json = json + "  },\n";

        json = json + "  \"metrics\": {\n";
        LinkedHashMap<String, Double> metrics = report.getMetrics();
        int count = 0;
        for (String name : metrics.keySet()) {
            count++;
            json = json + "    \"" + name + "\": " + metrics.get(name);
            json = json + (count < metrics.size() ? ",\n" : "\n");
        }
        json = json + "  },\n";

        json = json + "  \"errorCounts\": {\n";
        LinkedHashMap<String, Integer> errors = report.getErrorCounts();
        count = 0;
        for (String type : errors.keySet()) {
            count++;
            json = json + "    \"" + Json.escape(type) + "\": " + errors.get(type);
            json = json + (count < errors.size() ? ",\n" : "\n");
        }
        json = json + "  },\n";

        json = json + "  \"timeline\": [\n";
        ArrayList<String> timeline = report.getTimeline();
        for (int i = 0; i < timeline.size(); i++) {
            json = json + "    \"" + Json.escape(timeline.get(i)) + "\"";
            json = json + (i < timeline.size() - 1 ? ",\n" : "\n");
        }
        json = json + "  ],\n";

        json = json + "  \"events\": [\n";
        ArrayList<TelemetryEvent> events = session.getEvents();
        for (int i = 0; i < events.size(); i++) {
            json = json + "    " + events.get(i).toJson();
            json = json + (i < events.size() - 1 ? ",\n" : "\n");
        }
        json = json + "  ]\n";

        return json + "}\n";
    }
}
