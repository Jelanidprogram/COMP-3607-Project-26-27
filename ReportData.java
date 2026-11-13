package src.main.java.com.techoCrats.telemetry.report;

import java.util.HashMap;
import java.util.HashMap;
import java.util.HashMap;
import java.util.HashMap;

public class ReportData {
    // Final fields make the ReportData immutable once built
    private final double activeTime;
    private final double idleTime;
    private final double errorRatio;
    private final Map<String, Integer> compilationErrorBreakdown;
    private final List<String> progressionTimeline;

    // Private constructor accessed only by the Builder
    private ReportData(Builder builder) {
        this.activeTime = builder.activeTime;
        this.idleTime = builder.idleTime;
        this.errorRatio = builder.errorRatio;
        this.compilationErrorBreakdown = Collections.unmodifiableMap(new HashMap<>(builder.compilationErrorBreakdown));
        this.progressionTimeline = Collections.unmodifiableList(new ArrayList<>(builder.progressionTimeline));
    }

    // Getters
    public double getActiveTime() { return activeTime; }
    public double getIdleTime() { return idleTime; }
    public double getErrorRatio() { return errorRatio; }
    public Map<String, Integer> getCompilationErrorBreakdown() { return compilationErrorBreakdown; }
    public List<String> getProgressionTimeline() { return progressionTimeline; }


      //Static Builder Class for ReportData
    public static class Builder {
        private double activeTime = 0.0;
        private double idleTime = 0.0;
        private double errorRatio = 0.0;
        private Map<String, Integer> compilationErrorBreakdown = new HashMap<>();
        private List<String> progressionTimeline = new ArrayList<>();

        public Builder setActiveTime(double activeTime) {
            this.activeTime = activeTime;
            return this; // Return this for method chaining
        }

        public Builder setIdleTime(double idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public Builder setErrorRatio(double errorRatio) {
            this.errorRatio = errorRatio;
            return this;
        }

        public Builder setCompilationErrorBreakdown(Map<String, Integer> breakdown) {
            if (breakdown != null) {
                this.compilationErrorBreakdown = breakdown;
            }
            return this;
        }

        public Builder addCompilationError(String errorType, int count) {
            this.compilationErrorBreakdown.put(errorType, count);
            return this;
        }

        public Builder setProgressionTimeline(List<String> timeline) {
            if (timeline != null) {
                this.progressionTimeline = timeline;
            }
            return this;
        }

        public Builder addProgressionEvent(String event) {
            this.progressionTimeline.add(event);
            return this;
        }

        // Terminal build method returns the fully constructed ReportData instance
        public ReportData build() {
            return new ReportData(this);
        }
    }
}