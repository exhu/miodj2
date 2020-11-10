package org.miod.compiler.errors;

import java.nio.file.Path;

public final class WriteError extends CompilationError {
    public WriteError(Path source) {
        super(String.format("Cannot write '%s' file.", source.toString()));
    }
}
