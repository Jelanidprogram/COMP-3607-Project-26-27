package com.techoCrats.telemetry.collector;

import bluej.extensions2.BClass;
import bluej.extensions2.BPackage;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.PackageEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LifecycleListener implements bluej.extensions2.event.PackageListener,
        bluej.extensions2.event.ClassListener {

    private static final Logger LOGGER = Logger.getLogger(LifecycleListener.class.getName());

    private final EventCollector collector;
    private final EditorTelemetryTracker editorTracker;
    private final Set<String> knownClasses = new HashSet<>();
    private final Set<String> openPackages = new HashSet<>();
    private final Map<String, Integer> packageCountsByProject = new HashMap<>();

    public LifecycleListener(EventCollector collector) {
        this(collector, null);
    }

    public LifecycleListener(EventCollector collector, EditorTelemetryTracker editorTracker) {
        this.collector = Objects.requireNonNull(collector, "collector");
        this.editorTracker = editorTracker;
    }

    @Override
    public void packageOpened(PackageEvent event) {
        BPackage bPackage = event.getPackage();
        try {
            String projectId = projectId(bPackage);
            String packageId = projectId + "::" + bPackage.getName();
            if (openPackages.add(packageId)) {
                int count = packageCountsByProject.getOrDefault(projectId, 0);
                packageCountsByProject.put(projectId, count + 1);
                if (count == 0) {
                    record("PROJECT_OPENED", projectId);
                }
            }
            record("PACKAGE_OPENED", bPackage.toString());
            for (BClass bClass : bPackage.getClasses()) {
                knownClasses.add(classId(bClass));
                attachEditor(bClass);
            }
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            LOGGER.log(Level.WARNING, "Could not inspect classes in opened BlueJ package", exception);
        }
    }

    @Override
    public void packageClosing(PackageEvent event) {
        BPackage bPackage = event.getPackage();
        try {
            String projectId = projectId(bPackage);
            String packageId = projectId + "::" + bPackage.getName();
            record("PACKAGE_CLOSING", bPackage.toString());
            if (openPackages.remove(packageId)) {
                int count = packageCountsByProject.getOrDefault(projectId, 1) - 1;
                if (count <= 0) {
                    packageCountsByProject.remove(projectId);
                    record("PROJECT_CLOSED", projectId);
                } else {
                    packageCountsByProject.put(projectId, count);
                }
            }
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            LOGGER.log(Level.WARNING, "Could not identify closing BlueJ project", exception);
        }
    }

    @Override
    public void classStateChanged(ClassEvent event) {
        BClass bClass = event.getBClass();
        try {
            String id = classId(bClass);
            String action = knownClasses.add(id) ? "CLASS_CREATED" : "CLASS_MODIFIED";
            record(action, bClass.getName());
            attachEditor(bClass);
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            LOGGER.log(Level.WARNING, "Could not inspect changed BlueJ class", exception);
        }
    }

    @Override
    public void classNameChanged(ClassEvent event) {
        BClass bClass = event.getBClass();
        try {
            String packageId = projectId(bClass.getPackage()) + "::" + bClass.getPackage().getName();
            knownClasses.remove(packageId + "::" + event.getOldName());
            knownClasses.add(classId(bClass));
            record("CLASS_RENAMED", event.getOldName() + " -> " + bClass.getName());
            attachEditor(bClass);
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            LOGGER.log(Level.WARNING, "Could not inspect renamed BlueJ class", exception);
        }
    }

    @Override
    public void classRemoved(ClassEvent event) {
        BClass bClass = event.getBClass();
        try {
            knownClasses.remove(classId(bClass));
            record("CLASS_REMOVED", bClass.getName());
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            LOGGER.log(Level.WARNING, "Could not inspect removed BlueJ class", exception);
        }
    }

    private String projectId(BPackage bPackage) throws bluej.extensions2.ProjectNotOpenException,
            bluej.extensions2.PackageNotFoundException {
        return bPackage.getProject().getDir().getAbsolutePath();
    }

    private String classId(BClass bClass) throws bluej.extensions2.ProjectNotOpenException,
            bluej.extensions2.PackageNotFoundException {
        BPackage bPackage = bClass.getPackage();
        return projectId(bPackage) + "::" + bPackage.getName() + "::" + bClass.getName();
    }

    private void attachEditor(BClass bClass) {
        if (editorTracker != null) {
            editorTracker.attach(bClass);
        }
    }

    private void record(String action, String name) {
        collector.collect(new LifecycleEvent(System.currentTimeMillis(), action, name));
    }
}
