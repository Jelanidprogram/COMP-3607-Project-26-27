package com.techoCrats.telemetry.exporter;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportAggregator;
import com.techoCrats.telemetry.report.ReportData;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TelemetryExporter {

    private ExportStrategy strategy;
    private final ReportAggregator aggregator;

    public TelemetryExporter(ExportStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
        this.aggregator = new ReportAggregator();
    }

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public ExportStrategy getStrategy() {
        return strategy;
    }

    public String export(Session session, String folder) throws IOException {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(folder, "folder");
        ReportData report = aggregator.aggregate(session);
        String filePath = folder + File.separator + "telemetry-" + session.getSessionId()
                + "." + strategy.getFileExtension();
        strategy.export(session, report, filePath);
        return filePath;
    }

    public String exportToFile(Session session, File file) throws IOException {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(file, "file");
        ReportData report = aggregator.aggregate(session);
        strategy.export(session, report, file.getAbsolutePath());
        return file.getAbsolutePath();
    }
}
