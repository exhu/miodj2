package org.miod.compiler;

import java.nio.file.Path;

import org.miod.ast.CompUnit;

public interface Frontend {
    void setBuildContext(BuildContext buildContext);

    // parse, syntax check phase
    ParseResult check(Path src);

    // semantic analysis, can take two passes per unit
    SemanticResult analyze(CompUnit unitNode);

    SemanticResults analyzeAll();

    // optimization can exclude excessive reference counters, use stack alloc etc.
    void optimizeAll();

    void generateAll();
}
