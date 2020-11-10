package org.miod.compiler.errors;

import java.nio.file.Path;
import java.util.List;

public final class UnitNotFound extends CompilationError {
    public UnitNotFound(String name, List<Path> importPaths) {
        super(String.format("Unit '%s' not found. Searched in %s.", name.toString(), importPaths.toString()));
    }
}
