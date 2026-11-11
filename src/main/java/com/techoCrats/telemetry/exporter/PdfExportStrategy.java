package com.techoCrats.telemetry.exporter;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.report.ReportData;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfExportStrategy implements ExportStrategy {

    public String getFileExtension() {
        return "pdf";
    }

    public void export(Session session, ReportData report, String filePath) throws IOException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("BlueJ Telemetry Report", titleFont));
            document.add(new Paragraph("Session " + session.getSessionId()
                    + "   Student " + session.getStudentId()));
            document.add(new Paragraph("From " + session.getStartText() + " to " + session.getEndText()
                    + " (" + session.getDurationSeconds() + " seconds)"));

            document.add(new Paragraph("Active vs idle time", headingFont));
            PdfPTable timeTable = new PdfPTable(2);
            timeTable.addCell("Active time (seconds)");
            timeTable.addCell("" + (long) report.getMetric("activeTimeSeconds"));
            timeTable.addCell("Idle time (seconds)");
            timeTable.addCell("" + (long) report.getMetric("idleTimeSeconds"));
            document.add(timeTable);

            document.add(new Paragraph("Compilation and runs", headingFont));
            PdfPTable compileTable = new PdfPTable(2);
            compileTable.addCell("Compile attempts");
            compileTable.addCell("" + (int) report.getMetric("compileAttempts"));
            compileTable.addCell("Compile success rate");
            compileTable.addCell(String.format("%.1f%%", report.getMetric("compileSuccessRate")));
            compileTable.addCell("Runtime error ratio");
            compileTable.addCell(String.format("%.1f%%", report.getMetric("runtimeErrorRatio")));
            document.add(compileTable);

            document.add(new Paragraph("Compilation errors by type", headingFont));
            PdfPTable errorTable = new PdfPTable(2);
            errorTable.addCell("Error type");
            errorTable.addCell("Count");
            for (String type : report.getErrorCounts().keySet()) {
                errorTable.addCell(type);
                errorTable.addCell("" + report.getErrorCounts().get(type));
            }
            document.add(errorTable);

            document.add(new Paragraph("Progression timeline", headingFont));
            for (String entry : report.getTimeline()) {
                document.add(new Paragraph(entry));
            }

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Could not create PDF: " + e.getMessage());
        }
    }
}
