package org.miod.compiler;

import java.util.List;

import org.miod.compiler.errors.CompilationError;

public final class ParseResult {
    public final List<CompilationError> errors;
    // TODO add warnings?

    public ParseResult(List<CompilationError> errors) {
        this.errors = errors;
    }
}
