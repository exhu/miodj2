package org.miod.compiler;

import java.util.List;

public final class SemanticResults {
    public final List<SemanticResult> results;
    public final int totalErrors;

    public SemanticResults(List<SemanticResult> results) {
        this.results = results;
        int errors = 0;
        for (SemanticResult result : results) {
            if (result.errors != null) {
                errors += result.errors.size();
            }
        }
        totalErrors = errors;
    }

    public boolean isSuccess() {
        return totalErrors == 0;
    }

}
