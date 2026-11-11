package com.techoCrats.telemetry.exporter;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportAggregator;
import com.techoCrats.telemetry.report.ReportData;
import java.io.File;
import java.io.IOException;

public class TelemetryExporter {

    private ExportStrategy strategy;
    private ReportAggregator aggregator;

    public TelemetryExporter(ExportStrategy strategy) {
        this.strategy = strategy;
        this.aggregator = new ReportAggregator();
    }

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public ExportStrategy getStrategy() {
        return strategy;
    }

    public String export(Session session, String folder) throws IOException {
        ReportData report = aggregator.aggregate(session);
        String filePath = folder + File.separator + "telemetry-" + session.getSessionId()
                + "." + strategy.getFileExtension();
        strategy.export(session, report, filePath);
        return filePath;
    }
}
