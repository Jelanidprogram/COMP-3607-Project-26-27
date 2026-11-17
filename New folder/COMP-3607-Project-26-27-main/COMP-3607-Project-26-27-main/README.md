# BlueJTelemetry

BlueJTelemetry is a Maven-based Java project.

## Project structure

- `src/main/java/com/techoCrats/telemetry/` contains the telemetry application packages:
  - `extension` — BlueJ extension entry point and manual report-export menu
  - `collector` — observer-based event collection and BlueJ/editor listeners
  - `model` — timestamped telemetry event and Builder-created session types
  - `storage` — event storage
  - `processor` — telemetry processing
  - `exporter` — Strategy-based JSON and PDF exporters
  - `report` — report aggregation and active/idle/edit metrics
- `src/main/resources/` contains application resources.
- `src/test/java/com/techoCrats/telemetry/` contains tests.
- `pom.xml` defines the Maven project.

## Telemetry and reports

The extension starts a timestamped session when BlueJ loads it. The student ID
is read from the extension property `studentId`, falling back to
`student-placeholder`. Telemetry events are sent through the `EventCollector`
Observer interface and copied into the session. Java editor changes record the
class, offset, and inserted/deleted character counts; source text itself is not
stored. Idle time is inferred from gaps between edits using a three-minute
threshold.

Use **Tools → Export telemetry report...** to choose JSON or PDF and save the
report. JSON includes session metadata, metrics, error counts, progression
timeline, and individual events. The PDF includes coding activity, active/idle
time, compile/run metrics, error frequencies, and the class/method timeline.
`Session.Builder`, `ExportStrategy`, and `TelemetryEventObserver` demonstrate
Builder, Strategy, and Observer respectively.

BlueJ's public Extensions API does not expose a console-output listener or
distinct project-created/project-deleted callbacks. Run events therefore
include BlueJ's invocation result/exception information; project events record
package/project open and close, while class creation/modification/removal uses
the available class and editor callbacks. Error codes are stable categories
derived from BlueJ's compiler message because its event API exposes no compiler
diagnostic code.

## Building

The BlueJ Extensions API is supplied by BlueJ and is not published to Maven
Central. Install the `bluej.jar` from your BlueJ installation into your local
Maven repository before building. For the default Windows installation:

```powershell
mvn install:install-file "-Dfile=C:\Program Files\BlueJ\lib\bluej.jar" "-DgroupId=bluej" "-DartifactId=bluej" "-Dversion=5.1.0" "-Dpackaging=jar"
```

If you use another BlueJ release or installation path, adjust the command and
the `bluej.version` property in `pom.xml` to match.

Run Maven from the project directory containing `pom.xml`. In this download,
that is the nested `COMP-3607-Project-26-27-main` directory. From the outer
download directory, enter it and run the tests:

```cmd
cd /d "COMP-3607-Project-26-27-main"
mvn clean verify
```

Alternatively, from the outer download directory, run Maven by specifying the
POM path:

```cmd
mvn -f "COMP-3607-Project-26-27-main\pom.xml" clean verify
```

`mvn clean verify` runs the JUnit 5 suite and enforces a 75% line-coverage minimum for
the testable core. BlueJ/JavaFX extension entry points and callbacks requiring a
running BlueJ GUI are excluded from that unit-test coverage calculation.
"# COMP-3607-Project-26-27" 
