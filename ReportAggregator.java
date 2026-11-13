package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.List;
import java.util.Map;

public class ReportAggregator {

    private final MetricCalculator activeTimeCalculator;
    private final MetricCalculator idleTimeCalculator;
    private final ErrorRatioCalculator errorRatioCalculator;

    public ReportAggregator(MetricCalculator activeTimeCalc,
                            MetricCalculator idleTimeCalc,
                            ErrorRatioCalculator errorRatioCalc) {
        this.activeTimeCalculator = activeTimeCalc;
        this.idleTimeCalculator = idleTimeCalc;
        this.errorRatioCalculator = errorRatioCalc;
    }

    public ReportData aggregate(List<TelemetryEvent> events, List<String> progressionTimeline) {
        double activeTime = activeTimeCalculator.calculate(events);
        double idleTime = idleTimeCalculator.calculate(events);
        double errorRatio = errorRatioCalculator.calculate(events);
        Map<String, Integer> errorBreakdown = errorRatioCalculator.getErrorBreakdown();

        return new ReportData.Builder()
                .setActiveTime(activeTime)
                .setIdleTime(idleTime)
                .setErrorRatio(errorRatio)
                .setCompilationErrorBreakdown(errorBreakdown)
                .setProgressionTimeline(progressionTimeline)
                .build();
    }
}