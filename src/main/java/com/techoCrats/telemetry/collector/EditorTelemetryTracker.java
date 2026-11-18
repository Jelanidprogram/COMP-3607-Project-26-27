package com.techoCrats.telemetry.collector;

import bluej.extensions2.BClass;
import bluej.extensions2.editor.DocumentListener;
import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;
import com.techoCrats.telemetry.model.EditorEvent;
import com.techoCrats.telemetry.model.IdleEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EditorTelemetryTracker {

    private static final Pattern METHOD_DECLARATION = Pattern.compile(
            "(?m)^[ \\t]*(?:(?:public|protected|private|static|final|abstract|synchronized|native|strictfp|default)"
                    + "\\s+)*(?:<[^>\\n]+>\\s+)?(?:[\\w$.,<>?\\[\\]]+\\s+)+"
                    + "([A-Za-z_$][\\w$]*)\\s*\\(([^;{}]*)\\)\\s*"
                    + "(?:throws\\s+[\\w$.,\\s]+)?\\s*\\{");
    private static final Set<String> NON_METHOD_NAMES = Set.of(
            "if", "for", "while", "switch", "catch", "synchronized", "try", "do", "else");

    private final EventCollector collector;
    private final IdleDetector idleDetector;
    private final Map<JavaEditor, DocumentListener> attachedEditors = new IdentityHashMap<>();
    private final Map<String, Set<String>> methodDefinitionsByClass = new java.util.HashMap<>();

    public EditorTelemetryTracker(EventCollector collector, IdleDetector idleDetector) {
        this.collector = Objects.requireNonNull(collector, "collector");
        this.idleDetector = Objects.requireNonNull(idleDetector, "idleDetector");
    }

    public void attach(BClass bClass) {
        Objects.requireNonNull(bClass, "bClass");
        try {
            JavaEditor editor = bClass.getJavaEditorIfOpen();
            if (editor == null) {
                return;
            }
            synchronized (attachedEditors) {
                if (attachedEditors.containsKey(editor)) {
                    return;
                }
                String className = bClass.getName();
                methodDefinitionsByClass.putIfAbsent(className, findMethodDefinitions(readSource(editor)));
                DocumentListener listener = (offset, oldText, newText, oldCaretPosition, newCaretPosition) ->
                        recordEdit(className, offset, oldText, newText, readSource(editor), System.currentTimeMillis());
                attachedEditors.put(editor, listener);
                editor.addDocumentListener(listener);
            }
        } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
            throw new IllegalStateException("Could not attach telemetry to BlueJ editor", exception);
        }
    }

    public void attachOpenEditors(bluej.extensions2.BlueJ blueJ) {
        for (bluej.extensions2.BProject project : blueJ.getOpenProjects()) {
            try {
                for (bluej.extensions2.BPackage bPackage : project.getPackages()) {
                    for (BClass bClass : bPackage.getClasses()) {
                        attach(bClass);
                    }
                }
            } catch (bluej.extensions2.ProjectNotOpenException | bluej.extensions2.PackageNotFoundException exception) {
                throw new IllegalStateException("Could not inspect open BlueJ classes", exception);
            }
        }
    }

    void recordEdit(String className, int offset, String oldText, String newText, String sourceText, long timestamp) {
        String previousText = Objects.requireNonNullElse(oldText, "");
        String replacementText = Objects.requireNonNullElse(newText, "");
        for (IdleEvent event : idleDetector.recordActivity(timestamp)) {
            collector.collect(event);
        }
        collector.collect(new EditorEvent(timestamp, className, Math.max(offset, 0),
                previousText.length(), replacementText.length()));

        Set<String> currentDefinitions = findMethodDefinitions(sourceText);
        Set<String> knownDefinitions = methodDefinitionsByClass.computeIfAbsent(
                className, ignored -> new LinkedHashSet<>());
        for (String definition : currentDefinitions) {
            if (knownDefinitions.add(definition)) {
                collector.collect(new LifecycleEvent(timestamp, "METHOD_ADDED", className + "#" + definition));
            }
        }
    }

    public void finishSession(long timestamp) {
        for (IdleEvent event : idleDetector.finish(timestamp)) {
            collector.collect(event);
        }
    }

    private String readSource(JavaEditor editor) {
        TextLocation start = new TextLocation(1, 1);
        TextLocation end = editor.getTextLocationFromOffset(editor.getTextLength());
        return editor.getText(start, end);
    }

    private Set<String> findMethodDefinitions(String source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> definitions = new LinkedHashSet<>();
        Matcher matcher = METHOD_DECLARATION.matcher(source);
        while (matcher.find()) {
            String name = matcher.group(1);
            if (!NON_METHOD_NAMES.contains(name)) {
                definitions.add(name + "(" + matcher.group(2).replaceAll("\\s+", " ").trim() + ")");
            }
        }
        return definitions;
    }
}
