package com.techoCrats.telemetry.collector;

import java.util.Locale;

public final class CompileErrorClassifier {

    private CompileErrorClassifier() {
    }

    public static String classify(String message) {
        if (message == null || message.isBlank()) {
            return "COMPILATION_ERROR";
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        if (normalized.contains("';' expected") || normalized.contains("semicolon")) {
            return "MISSING_SEMICOLON";
        }
        if (normalized.contains("cannot find symbol") || normalized.contains("does not exist")) {
            return "UNRESOLVED_SYMBOL";
        }
        if (normalized.contains("incompatible types")) {
            return "INCOMPATIBLE_TYPES";
        }
        if (normalized.contains("reached end of file") || normalized.contains("expected }")) {
            return "UNMATCHED_BRACE";
        }
        if (normalized.contains("already defined") || normalized.contains("duplicate")) {
            return "DUPLICATE_DECLARATION";
        }
        return "COMPILATION_ERROR";
    }
}
