package org.miod.compiler.errors;

import java.nio.file.Path;

public final class ReadError extends CompilationError {
    public ReadError(Path source) {
        super(String.format("Cannot read '%s' file.", source.toString()));
    }
}
