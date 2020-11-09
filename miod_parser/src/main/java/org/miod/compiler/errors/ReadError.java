package org.miod.compiler.errors;

import java.net.URL;

public final class ReadError extends CompilationError {
    public ReadError(URL source) {
        super(String.format("Cannot read '%s' file.", source.toString()));
    }
}
