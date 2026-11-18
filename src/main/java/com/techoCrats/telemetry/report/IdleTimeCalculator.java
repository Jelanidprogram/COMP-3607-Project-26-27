package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import java.util.ArrayList;

public class IdleTimeCalculator implements MetricCalculator {

    @Override
    public String getName() {
        return "idleTimeSeconds";
    }

    @Override
    public double calculate(Session session) {
        ArrayList<Long> times = EditTimes.activityTimes(session);

        long idle = 0;
        for (int i = 1; i < times.size(); i++) {
            long gap = (times.get(i) - times.get(i - 1)) / 1000;
            if (gap > ActiveTimeCalculator.IDLE_LIMIT_SECONDS) {
                idle = idle + (gap - ActiveTimeCalculator.IDLE_LIMIT_SECONDS);
            }
        }
        return idle;
    }
}
