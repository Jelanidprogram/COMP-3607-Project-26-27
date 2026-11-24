package com.techoCrats.telemetry.collector;

import com.techoCrats.telemetry.model.CompileEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public final class CompileListener implements bluej.extensions2.event.CompileListener {

    private final EventCollector collector;

    public CompileListener(EventCollector collector) {
        this.collector = Objects.requireNonNull(collector, "collector");
    }

    @Override
    public void compileStarted(bluej.extensions2.event.CompileEvent event) {
        // A compilation attempt is recorded once, when BlueJ reports its terminal result.
    }

    @Override
    public void compileError(bluej.extensions2.event.CompileEvent event) {
        // compileFailed records the attempt and its final error details.
    }

    @Override
    public void compileWarning(bluej.extensions2.event.CompileEvent event) {
        // Warnings are not compilation attempts or failures.
    }

    @Override
    public void compileSucceeded(bluej.extensions2.event.CompileEvent event) {
        collector.collect(new CompileEvent(System.currentTimeMillis(), true, null, null, 0, sourceFiles(event)));
    }

    @Override
    public void compileFailed(bluej.extensions2.event.CompileEvent event) {
        collector.collect(new CompileEvent(
                System.currentTimeMillis(),
                false,
                CompileErrorClassifier.classify(event.getErrorMessage()),
                event.getErrorMessage(),
                event.getErrorLineNumber(),
                sourceFiles(event)));
    }

    private ArrayList<String> sourceFiles(bluej.extensions2.event.CompileEvent event) {
        ArrayList<String> paths = new ArrayList<>();
        for (File file : event.getFiles()) {
            if (file != null) {
                paths.add(file.getName());
            }
        }
        return paths;
    }
}
