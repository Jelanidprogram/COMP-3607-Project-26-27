# BlueJTelemetry project wiki

BlueJTelemetry is a Maven-based Java project for a BlueJ extension that collects
development activity events and produces session reports.

## Pages

- [Architecture](Architecture.md) — source packages and the event/report flow.
- [Development setup](Development-Setup.md) — prerequisites, BlueJ dependency,
  builds, and tests.
- [Metrics and reports](Metrics-and-Reports.md) — report fields and metric
  definitions in the source.
- [Export formats](Export-Formats.md) — JSON and PDF report output.

## Project facts

- Java release: 17.
- Build system: Maven.
- BlueJ Extensions API version configured in the project: 5.1.0.
- Test framework: JUnit Jupiter 5.11.0.

See the repository [README](../../../../../../../README.md) for the short
project summary and build command.

## Implementation status

This wiki describes the source currently in the repository. Some classes are
still placeholders or under development; check the source and run the test suite
before treating a described flow as an end-to-end feature.
