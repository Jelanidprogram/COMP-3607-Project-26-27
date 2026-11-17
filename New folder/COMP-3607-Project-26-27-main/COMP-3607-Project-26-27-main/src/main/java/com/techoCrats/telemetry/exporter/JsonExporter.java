package com.techoCrats.telemetry.exporter;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportData;
import java.io.IOException;
import java.util.Objects;

public class JsonExporter {

    private final JsonExportStrategy strategy = new JsonExportStrategy();

    public String toJson(Session session, ReportData report) {
        return strategy.toJson(session, report);
    }

    public void export(Session session, ReportData report, String filePath) throws IOException {
        strategy.export(Objects.requireNonNull(session, "session"),
                Objects.requireNonNull(report, "report"), filePath);
    }
}
