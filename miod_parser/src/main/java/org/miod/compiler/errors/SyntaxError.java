package org.miod.compiler.errors;

import java.nio.file.Path;

public final class SyntaxError extends CompilationError {
    public SyntaxError(Path source, String msg) {
        super(String.format("Syntax error in '%s':%s", source.toString(), msg));
    }
}
