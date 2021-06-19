package org.miod.compiler.errors;

import java.util.List;

import org.miod.ast.NodeLocation;

public final class CyclicDependency extends CompilationError {
    public CyclicDependency(List<NodeLocation> importStack, String unitA, String unitB) {
        super(String.format("Cyclic dependency: unit '%s' and '%s', imported at %s", unitA, unitB,
                importStack.toString()));
    }
}
