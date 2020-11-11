package org.miod.compiler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.miod.ast.AstNode;
import org.miod.ast.CompUnit;

public final class Analyzer {
    private final CompUnit root;
    // revisit nodes for unknown ids
    private final List<AstNode> revisitNodes = new ArrayList<>();
    private final Deque<String> unitsUnderProcessing = new ArrayDeque<>();

    public Analyzer(CompUnit root) {
        this.root = root;

    }

    SemanticResult analyze() {
        // TODO
        // to detect cyclic imports
        // private final Set<String> unitsUnderProcessing = new HashSet<>();
        // TODO maybe support cyclic deps?

        return new SemanticResult(null, null);
    }
}
