package org.miod.compiler.errors;

import org.miod.ast.NodeLocation;

public final class CyclicDependency implements CompilationError {
    private final String msg;

    public CyclicDependency(NodeLocation importLocation, String unitA, String unitB) {
        msg = String.format("Cyclic dependency: unit '%s' and '%s', imported at %s", unitA, unitB,
                importLocation.toString());
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
