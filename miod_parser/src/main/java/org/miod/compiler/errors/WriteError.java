package org.miod.compiler.errors;

import java.net.URL;

public final class WriteError extends CompilationError {
    public WriteError(URL source) {
        super(String.format("Cannot write '%s' file.", source.toString()));
    }
}
