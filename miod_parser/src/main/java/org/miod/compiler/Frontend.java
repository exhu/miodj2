package org.miod.compiler;

import java.nio.file.Path;

public interface Frontend {
    void setBuildContext(BuildContext buildContext);

    // parse, syntax check phase
    ParseResult check(Path src);

    // semantic analysis, can take two passes per unit
    SemanticResult analyze(ParseResult src);

    // TODO optimization and code generation phases
    // optimization can exclude excessive reference counters, use stack alloc etc.

}
