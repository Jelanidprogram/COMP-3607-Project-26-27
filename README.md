# BlueJTelemetry

BlueJTelemetry is a Maven-based Java project.

## Project structure

- `src/main/java/com/techoCrats/telemetry/` contains the telemetry application packages:
  - `extension` — BlueJ extension entry point
  - `collector` — event collection and listeners
  - `model` — telemetry event and session types
  - `storage` — event storage
  - `processor` — telemetry processing
  - `exporter` — JSON and PDF exporters
  - `report` — report aggregation
- `src/main/resources/` contains application resources.
- `src/test/java/com/techoCrats/telemetry/` contains tests.
- `pom.xml` defines the Maven project.

## Building

The BlueJ Extensions API is supplied by BlueJ and is not published to Maven
Central. Install the `bluej.jar` from your BlueJ installation into your local
Maven repository before building. For the default Windows installation:

```powershell
mvn install:install-file "-Dfile=C:\Program Files\BlueJ\lib\bluej.jar" "-DgroupId=bluej" "-DartifactId=bluej" "-Dversion=5.1.0" "-Dpackaging=jar"
```

If you use another BlueJ release or installation path, adjust the command and
the `bluej.version` property in `pom.xml` to match. Then build and run tests:

```powershell
mvn test
```
"# COMP-3607-Project-26-27" 
