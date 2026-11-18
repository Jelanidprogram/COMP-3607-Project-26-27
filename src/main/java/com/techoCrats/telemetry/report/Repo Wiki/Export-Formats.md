# Export formats

`TelemetryExporter` accepts an `ExportStrategy`. The project includes strategy
implementations for JSON and PDF:

```java
TelemetryExporter exporter = new TelemetryExporter(new JsonExportStrategy());
String outputPath = exporter.export(session, outputFolder);
```

Use `new PdfExportStrategy()` to select PDF instead. The exporter builds report
data from the session and writes a file named
`telemetry-<sessionId>.<extension>` in the supplied folder. The returned string
is the generated path.

## JSON

The JSON strategy writes:

- session identifiers, times, duration, and total compilation count;
- report metrics;
- compilation error counts;
- the progression timeline; and
- the session's serialized telemetry events.

## PDF

The PDF strategy writes session details, active/idle time, compilation and run
metrics, compilation error counts, and the progression timeline.

## Notes

The strategy classes contain the format-specific generation code. The older
`JsonExporter` and `PdfExporter` classes are currently placeholders. Report
generation depends on the report aggregation code, which is still under
development; verify the project builds and tests pass before relying on exports.
