package org.miod.compiler;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface Frontend {
    void setSources(List<Path> sources);

    void setBuildTags(Set<String> tags);

    void setSearchRoots(List<Path> roots);

    // parse, syntax check phase
    ParseResult parse();

    // semantic analysis, can take two passes per unit
    SemanticResult analyze();

    // TODO optimization and code generation phases
    // optimization can exclude excessive reference counters, use stack alloc etc.

}
