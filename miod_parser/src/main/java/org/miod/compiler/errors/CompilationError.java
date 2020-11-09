package org.miod.compiler.errors;

public abstract class CompilationError {
    protected final String msg;

    protected CompilationError(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
