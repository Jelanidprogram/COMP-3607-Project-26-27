package com.techoCrats.telemetry.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CompileErrorClassifierTest {

    @Test
    public void mapsCommonCompilerMessagesToStableErrorCodes() {
        assertEquals("MISSING_SEMICOLON", CompileErrorClassifier.classify("';' expected"));
        assertEquals("UNRESOLVED_SYMBOL", CompileErrorClassifier.classify("cannot find symbol"));
        assertEquals("INCOMPATIBLE_TYPES", CompileErrorClassifier.classify("incompatible types"));
        assertEquals("COMPILATION_ERROR", CompileErrorClassifier.classify("unclassified error"));
        assertEquals("COMPILATION_ERROR", CompileErrorClassifier.classify(null));
    }
}
