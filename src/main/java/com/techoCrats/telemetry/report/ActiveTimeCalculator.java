package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import java.util.ArrayList;

public class ActiveTimeCalculator implements MetricCalculator {

    public static final long IDLE_LIMIT_SECONDS = 180;

    @Override
    public String getName() {
        return "activeTimeSeconds";
    }

    @Override
    public double calculate(Session session) {
        ArrayList<Long> times = EditTimes.activityTimes(session);

        long active = 0;
        for (int i = 1; i < times.size(); i++) {
            long gap = (times.get(i) - times.get(i - 1)) / 1000;
            if (gap < 0) {
                gap = 0;
            }
            if (gap > IDLE_LIMIT_SECONDS) {
                active = active + IDLE_LIMIT_SECONDS;
            } else {
                active = active + gap;
            }
        }
        return active;
    }
}
