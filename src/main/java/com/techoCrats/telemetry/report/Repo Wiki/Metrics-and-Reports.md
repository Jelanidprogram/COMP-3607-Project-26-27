# Metrics and reports

`ReportAggregator` is intended to calculate report data from a `Session`.
`ReportData` carries a metrics map, compilation error counts, and a timeline.
The available metric names and current calculation rules are:

| Metric | Meaning in the calculator |
| --- | --- |
| `activeTimeSeconds` | Adds event or edit gaps up to 180 seconds; longer gaps contribute at most 180 seconds of activity. |
| `idleTimeSeconds` | Adds the portion of event or edit gaps beyond 180 seconds. |
| `compileAttempts` | Number of `COMPILE` events. |
| `compileSuccessRate` | Successful compile events divided by compile attempts, as a percentage; zero when there are no attempts. |
| `runtimeErrorRatio` | Run events with an exception divided by run events, as a percentage; zero when there are no runs. |
| `editCount` | Number of editor events. |

The aggregator source also references character-insertion and edit-frequency
calculators. Their implementations are not part of the current source, so those
metrics are not documented here as available behavior.

## Additional report fields

- **Error counts:** unsuccessful compile events grouped by error type.
- **Timeline:** lifecycle events for `CLASS_CREATED` and `METHOD_ADDED`, with
  their event time and name.
- **Session details:** session and student identifiers, start/end timestamps,
  and duration in seconds.

The report package is under development. Confirm the current sources and run
`mvn test` before depending on the calculations in production.
