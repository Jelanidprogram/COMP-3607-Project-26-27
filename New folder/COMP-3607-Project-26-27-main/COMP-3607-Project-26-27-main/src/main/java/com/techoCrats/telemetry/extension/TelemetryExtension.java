package com.techoCrats.telemetry.extension;

import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;
import com.techoCrats.telemetry.collector.CompileListener;
import com.techoCrats.telemetry.collector.EventCollector;
import com.techoCrats.telemetry.collector.LifecycleListener;
import com.techoCrats.telemetry.collector.RunListener;

public class TelemetryExtension extends Extension {

    private BlueJ blueJ;
    private EventCollector collector;
    private CompileListener compileListener;
    private RunListener runListener;
    private LifecycleListener lifecycleListener;

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
        compileListener = new CompileListener(collector);
        runListener = new RunListener(collector);
        lifecycleListener = new LifecycleListener(collector);

        blueJ.addCompileListener(compileListener);
        blueJ.addInvocationFinishedListener(runListener);
        blueJ.addPackageListener(lifecycleListener);
        blueJ.addClassListener(lifecycleListener);
    }

    @Override
    public void terminate() {
        if (blueJ != null) {
            blueJ.removeCompileListener(compileListener);
            blueJ.removeInvocationFinishedListener(runListener);
            blueJ.removePackageListener(lifecycleListener);
            blueJ.removeClassListener(lifecycleListener);
            blueJ = null;
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
}
