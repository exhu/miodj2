package org.miod.compiler.errors;

import java.nio.file.Path;

public final class WrongUnitName extends CompilationError {
    public WrongUnitName(String name, Path path) {
        super(String.format("Wrong unit name '%s' for path '%s'.", name.toString(), path.toString()));
    }
}
