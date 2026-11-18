package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;

public interface MetricCalculator {

    public String getName();

    public double calculate(Session session);
}
