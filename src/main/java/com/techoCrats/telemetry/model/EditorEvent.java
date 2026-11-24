package com.techoCrats.telemetry.model;

import java.util.LinkedHashMap;
import java.util.Objects;

public final class EditorEvent extends TelemetryEvent {

    private final String className;
    private final int offset;
    private final int removedCharacters;
    private final int insertedCharacters;

    public EditorEvent(long timestamp, String className, int offset, int removedCharacters, int insertedCharacters) {
        super(timestamp, "EDITOR_EDIT");
        this.className = Objects.requireNonNull(className, "className");
        if (offset < 0 || removedCharacters < 0 || insertedCharacters < 0) {
            throw new IllegalArgumentException("Edit positions and character counts must be non-negative");
        }
        this.offset = offset;
        this.removedCharacters = removedCharacters;
        this.insertedCharacters = insertedCharacters;
    }

    public String getClassName() {
        return className;
    }

    public int getOffset() {
        return offset;
    }

    public int getRemovedCharacters() {
        return removedCharacters;
    }

    public int getInsertedCharacters() {
        return insertedCharacters;
    }

    @Override
    public String toJson() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("type", getType());
        values.put("time", getTimeText());
        values.put("className", className);
        values.put("offset", offset);
        values.put("removedCharacters", removedCharacters);
        values.put("insertedCharacters", insertedCharacters);
        return Json.stringify(values);
    }
}
