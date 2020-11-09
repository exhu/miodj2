package org.miod.compiler.errors;

import java.net.URL;

public final class ReadError implements CompilationError {
    final String msg;

    public ReadError(URL source) {
        msg = String.format("Cannot read '%s' file.", source.toString());
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
