package org.miod.compiler;

import java.util.List;

import org.miod.ast.CompUnit;
import org.miod.compiler.errors.CompilationError;

public final class ParseResult {
    /// can be null
    public final List<CompilationError> errors;
    public final CompUnit root;

    public ParseResult(List<CompilationError> errors, CompUnit root) {
        this.errors = errors;
        this.root = root;
    }

    public boolean isSuccess() {
        return errors == null || errors.isEmpty();
    }
}
