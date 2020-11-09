package org.miod.compiler;

import java.util.List;

import org.miod.compiler.errors.CompilationError;

public final class SemanticResult {
    public final List<CompilationError> errors;

    public SemanticResult(List<CompilationError> errors) {
        this.errors = errors;
    }
}
