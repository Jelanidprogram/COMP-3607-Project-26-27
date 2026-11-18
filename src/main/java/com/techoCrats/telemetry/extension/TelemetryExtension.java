package com.techoCrats.telemetry.extension;

import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;
import com.techoCrats.telemetry.collector.EditorTelemetryTracker;
import com.techoCrats.telemetry.collector.CompileListener;
import com.techoCrats.telemetry.collector.EventCollector;
import com.techoCrats.telemetry.collector.IdleDetector;
import com.techoCrats.telemetry.collector.LifecycleListener;
import com.techoCrats.telemetry.collector.RunListener;
import com.techoCrats.telemetry.exporter.JsonExportStrategy;
import com.techoCrats.telemetry.exporter.PdfExportStrategy;
import com.techoCrats.telemetry.exporter.TelemetryExporter;
import com.techoCrats.telemetry.model.Session;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TelemetryExtension extends Extension {

    private static final Logger LOGGER = Logger.getLogger(TelemetryExtension.class.getName());

    private BlueJ blueJ;
    private EventCollector collector;
    private CompileListener compileListener;
    private RunListener runListener;
    private LifecycleListener lifecycleListener;
    private EditorTelemetryTracker editorTracker;
    private Session session;
    private Timeline editorScan;

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public void startup(BlueJ blueJ) {
        if (this.blueJ != null) {
            terminate();
        }

        this.blueJ = blueJ;
        collector = new EventCollector();
        String studentId = blueJ.getExtensionPropertyString("studentId", "student-placeholder");
        session = Session.builder().studentId(studentId).build();
        collector.addObserver(session::addEvent);
        editorTracker = new EditorTelemetryTracker(collector, new IdleDetector());
        compileListener = new CompileListener(collector);
        runListener = new RunListener(collector);
        lifecycleListener = new LifecycleListener(collector, editorTracker);

        blueJ.addCompileListener(compileListener);
        blueJ.addInvocationFinishedListener(runListener);
        blueJ.addPackageListener(lifecycleListener);
        blueJ.addClassListener(lifecycleListener);
        blueJ.setMenuGenerator(new TelemetryMenuGenerator(
                blueJ, () -> session, this::recordExportBoundary, new TelemetryExporter(new PdfExportStrategy())));
        editorScan = new Timeline(new KeyFrame(Duration.seconds(2), event -> scanOpenEditors()));
        editorScan.setCycleCount(Animation.INDEFINITE);
        editorScan.play();
    }

    @Override
    public void terminate() {
        if (blueJ != null) {
            if (editorScan != null) {
                editorScan.stop();
                editorScan = null;
            }
            if (session != null) {
                endSession(System.currentTimeMillis());
                saveOnExit();
            }
            blueJ.removeCompileListener(compileListener);
            blueJ.removeInvocationFinishedListener(runListener);
            blueJ.removePackageListener(lifecycleListener);
            blueJ.removeClassListener(lifecycleListener);
            blueJ = null;
        }
    }

    private void scanOpenEditors() {
        if (blueJ == null || editorTracker == null) {
            return;
        }
        try {
            editorTracker.attachOpenEditors(blueJ);
        } catch (IllegalStateException exception) {
            LOGGER.log(Level.WARNING, "Could not scan BlueJ editors for telemetry", exception);
        }
    }

    private void endSession(long timestamp) {
        if (editorTracker != null) {
            editorTracker.finishSession(timestamp);
        }
        if (session != null) {
            session.end(timestamp);
        }
    }

    private void saveOnExit() {
        String folder = System.getProperty("user.home") + File.separator + "BlueJ-Telemetry";
        new File(folder).mkdirs();
        try {
            TelemetryExporter exporter = new TelemetryExporter(new JsonExportStrategy());
            exporter.export(session, folder);
            exporter.setStrategy(new PdfExportStrategy());
            exporter.export(session, folder);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not save telemetry on exit", e);
        }
    }

    private void recordExportBoundary(long timestamp) {
        if (editorTracker != null) {
            editorTracker.finishSession(timestamp);
        }
        if (session != null) {
            session.updateEndTime(timestamp);
        }
    }

    @Override
    public String getName() {
        return "BlueJ Telemetry";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public EventCollector getCollector() {
        return collector;
    }

    public Session getSession() {
        return session;
    }
}
