package com.techoCrats.telemetry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

public class TelemetryEventJsonTest {

    @Test
    public void compilationEventSerializesDiagnosticsAndFilesAsJson() {
        CompileEvent event = new CompileEvent(0, false, "UNRESOLVED_SYMBOL",
                "cannot find symbol: \"Room\"", 9, java.util.List.of("Room.java"));

        JsonObject json = JsonParser.parseString(event.toJson()).getAsJsonObject();

        assertEquals("COMPILE", json.get("type").getAsString());
        assertEquals("UNRESOLVED_SYMBOL", json.get("errorCode").getAsString());
        assertEquals(9, json.get("errorLine").getAsInt());
        assertEquals("Room.java", json.getAsJsonArray("sourceFiles").get(0).getAsString());
    }

    @Test
    public void lifecycleAndRunEventsSerializeNullableFields() {
        JsonObject lifecycle = JsonParser.parseString(
                new LifecycleEvent(0, "CLASS_CREATED", "Room").toJson()).getAsJsonObject();
        JsonObject run = JsonParser.parseString(new RunEvent(0, false, null, "Room", "run", null).toJson())
                .getAsJsonObject();

        assertEquals("CLASS_CREATED", lifecycle.get("action").getAsString());
        assertEquals("Room", lifecycle.get("name").getAsString());
        assertTrue(run.get("exception").isJsonNull());
        assertEquals("run", run.get("methodName").getAsString());
    }

    @Test
    public void jsonEscapingCoversControlCharactersAndNull() {
        assertEquals("line\\n\\\"quoted\\\"", Json.escape("line\n\"quoted\""));
        assertEquals("\"a\\\\b\"", Json.toJsonValue("a\\b"));
        assertEquals("null", Json.toJsonValue(null));
    }
}
