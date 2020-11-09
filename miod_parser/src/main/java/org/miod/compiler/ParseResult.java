package org.miod.compiler;

import java.util.List;

import org.miod.ast.AstNode;
import org.miod.compiler.errors.CompilationError;

public final class ParseResult {
    public final List<CompilationError> errors;
    public final AstNode root;

    public ParseResult(List<CompilationError> errors, AstNode root) {
        this.errors = errors;
        this.root = root;
    }
}
