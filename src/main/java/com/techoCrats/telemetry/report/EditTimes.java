package com.techoCrats.telemetry.report;

import com.techoCrats.telemetry.model.Session;
import com.techoCrats.telemetry.model.TelemetryEvent;
import java.util.ArrayList;
import java.util.Collections;

public class EditTimes {

    public static ArrayList<Long> find(ArrayList<TelemetryEvent> events) {
        ArrayList<Long> times = new ArrayList<Long>();
        for (TelemetryEvent event : events) {
            if (event.getType().equals("EDITOR_EDIT")) {
                times.add(event.getTimestamp());
            }
        }
        Collections.sort(times);
        return times;
    }

    public static ArrayList<Long> activityTimes(Session session) {
        ArrayList<Long> times = find(session.getEvents());
        if (times.isEmpty()) {
            for (TelemetryEvent event : session.getEvents()) {
                times.add(event.getTimestamp());
            }
        } else {
            times.add(session.getEndTime());
        }
        return times;
    }
}
