package com.techoCrats.telemetry.extension;

import bluej.extensions2.BPackage;
import bluej.extensions2.BlueJ;
import bluej.extensions2.MenuGenerator;
import com.techoCrats.telemetry.exporter.JsonExportStrategy;
import com.techoCrats.telemetry.exporter.PdfExportStrategy;
import com.techoCrats.telemetry.exporter.TelemetryExporter;
import com.techoCrats.telemetry.model.Session;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.LongConsumer;
import java.util.function.Supplier;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public final class TelemetryMenuGenerator extends MenuGenerator {

    private final BlueJ blueJ;
    private final Supplier<Session> sessionSupplier;
    private final LongConsumer recordExportBoundary;
    private final TelemetryExporter exporter;

    public TelemetryMenuGenerator(BlueJ blueJ, Supplier<Session> sessionSupplier, LongConsumer recordExportBoundary,
                                  TelemetryExporter exporter) {
        this.blueJ = Objects.requireNonNull(blueJ, "blueJ");
        this.sessionSupplier = Objects.requireNonNull(sessionSupplier, "sessionSupplier");
        this.recordExportBoundary = Objects.requireNonNull(recordExportBoundary, "recordExportBoundary");
        this.exporter = Objects.requireNonNull(exporter, "exporter");
    }

    @Override
    public MenuItem getToolsMenuItem(BPackage bPackage) {
        MenuItem exportItem = new MenuItem("Export telemetry report...");
        exportItem.setOnAction(event -> exportReport());
        return exportItem;
    }

    private void exportReport() {
        Session session = sessionSupplier.get();
        if (session == null) {
            showError("There is no telemetry session to export.");
            return;
        }

        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("PDF", "PDF", "JSON");
        formatDialog.setTitle("Export telemetry");
        formatDialog.setHeaderText("Choose a report format");
        Optional<String> selectedFormat = formatDialog.showAndWait();
        if (selectedFormat.isEmpty()) {
            return;
        }

        boolean pdf = selectedFormat.get().equals("PDF");
        String extension = pdf ? "pdf" : "json";
        exporter.setStrategy(pdf ? new PdfExportStrategy() : new JsonExportStrategy());

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save telemetry report");
        chooser.setInitialFileName("telemetry-" + session.getSessionId() + "." + extension);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                pdf ? "PDF report" : "JSON data", "*." + extension));
        File selectedFile = chooser.showSaveDialog(blueJ.getCurrentWindow());
        if (selectedFile == null) {
            return;
        }
        File outputFile = withExtension(selectedFile, extension);

        try {
            long now = System.currentTimeMillis();
            recordExportBoundary.accept(now);
            exporter.exportToFile(session, outputFile);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Telemetry exported");
            confirmation.setHeaderText("Report saved");
            confirmation.setContentText(outputFile.getAbsolutePath());
            confirmation.showAndWait();
        } catch (IOException exception) {
            showError("Could not export telemetry report: " + exception.getMessage());
        }
    }

    private File withExtension(File file, String extension) {
        if (file.getName().toLowerCase(java.util.Locale.ROOT).endsWith("." + extension)) {
            return file;
        }
        return new File(file.getAbsolutePath() + "." + extension);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Telemetry export failed");
        alert.setHeaderText("The report could not be exported");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
