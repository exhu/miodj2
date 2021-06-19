package org.miod.compiler;

import java.util.List;

import org.miod.compiler.errors.CompilationError;
import org.miod.semantic.UnitContext;

public final class SemanticResult {
    public final List<CompilationError> errors;
    // TODO add warnings?

    public final UnitContext unit;

    public SemanticResult(List<CompilationError> errors, UnitContext unit) {
        this.errors = errors;
        this.unit = unit;
    }
}
