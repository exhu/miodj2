package org.miod.compiler.errors;

import org.miod.ast.NodeLocation;

public final class CyclicDependency extends CompilationError {
    public CyclicDependency(NodeLocation importLocation, String unitA, String unitB) {
        super(String.format("Cyclic dependency: unit '%s' and '%s', imported at %s", unitA, unitB,
                importLocation.toString()));
    }
}
