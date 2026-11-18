# Architecture

## Package map

The Java sources are under
`src/main/java/com/techoCrats/telemetry/`.

| Package | Responsibility |
| --- | --- |
| `extension` | BlueJ extension lifecycle and listener registration. |
| `collector` | BlueJ event listeners and the in-memory event collector. |
| `model` | Compile, run, lifecycle, session, and base telemetry event types. |
| `storage` | Intended event-storage area; `EventStorage` is currently a placeholder. |
| `processor` | Intended processing area; `TelemetryProcessor` is currently a placeholder. |
| `report` | Metric calculators, report aggregation, and report data. |
| `exporter` | Export strategy interface and JSON/PDF report generation. |

## Event flow

1. BlueJ calls `TelemetryExtension.startup`.
2. The extension creates an `EventCollector` and registers compile, run, and
   package/class lifecycle listeners with BlueJ.
3. Listeners send telemetry events to the collector. The collector keeps events
   in memory and notifies its registered observers.
4. A `Session` represents a collection of events with session/student
   identifiers and start/end timestamps.
5. `ReportAggregator` is intended to calculate metrics, compilation-error
   counts, and a lifecycle timeline from a session.
6. `TelemetryExporter` selects an `ExportStrategy` to write the report.

The project has source for these components, but the complete flow is still
under development. In particular, `EventStorage` and `TelemetryProcessor` do
not yet provide implementations.

## Event types

- `CompileEvent` records success and compilation error details.
- `RunEvent` records run success and an exception, if present.
- `LifecycleEvent` records an action and the affected class or member name.

All event types extend `TelemetryEvent`, which carries an epoch-millisecond
timestamp, a type, a UTC text representation, and JSON serialization.

## Extension lifecycle

The extension registers listeners during startup. Calling `terminate` removes
the listeners from BlueJ. Starting the same extension instance again first
terminates its prior registrations.
