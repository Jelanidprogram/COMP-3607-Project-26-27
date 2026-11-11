package com.techoCrats.telemetry.exporter;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportData;
import java.io.IOException;

public interface ExportStrategy {

    public String getFileExtension();

    public void export(Session session, ReportData report, String filePath) throws IOException;
}
