package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.CompileEvent;
import com.techoCrats.telemetry.model.LifecycleEvent;
import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ReportAggregator {

    private ArrayList<MetricCalculator> calculators;

    public ReportAggregator() {
        calculators = new ArrayList<MetricCalculator>();
        calculators.add(new ActiveTimeCalculator());
        calculators.add(new IdleTimeCalculator());
        calculators.add(new CompileAttemptsCalculator());
        calculators.add(new CompileSuccessRateCalculator());
        calculators.add(new ErrorRatioCalculator());
    }

    public void addCalculator(MetricCalculator calculator) {
        calculators.add(calculator);
    }

    public ReportData aggregate(Session session) {
        ArrayList<TelemetryEvent> events = session.getEvents();

        LinkedHashMap<String, Double> metrics = new LinkedHashMap<String, Double>();
        for (MetricCalculator calculator : calculators) {
            metrics.put(calculator.getName(), calculator.calculate(events));
        }

        return new ReportData(metrics, countErrors(events), buildTimeline(events));
    }

    private LinkedHashMap<String, Integer> countErrors(ArrayList<TelemetryEvent> events) {
        LinkedHashMap<String, Integer> counts = new LinkedHashMap<String, Integer>();
        for (TelemetryEvent event : events) {
            if (event.getType().equals("COMPILE")) {
                CompileEvent compile = (CompileEvent) event;
                if (!compile.isSuccessful()) {
                    String type = compile.getErrorType();
                    if (counts.containsKey(type)) {
                        counts.put(type, counts.get(type) + 1);
                    } else {
                        counts.put(type, 1);
                    }
                }
            }
        }
        return counts;
    }

    private ArrayList<String> buildTimeline(ArrayList<TelemetryEvent> events) {
        ArrayList<String> timeline = new ArrayList<String>();
        for (TelemetryEvent event : events) {
            if (event.getType().equals("LIFECYCLE")) {
                LifecycleEvent lifecycle = (LifecycleEvent) event;
                if (lifecycle.getAction().equals("CLASS_CREATED")
                        || lifecycle.getAction().equals("METHOD_ADDED")) {
                    timeline.add(event.getTimeText() + "  " + lifecycle.getAction() + "  " + lifecycle.getName());
                }
            }
        }
        return timeline;
    }
}
