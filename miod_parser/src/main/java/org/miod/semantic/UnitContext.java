package org.miod.semantic;

import java.util.List;

import org.miod.ast.AstNode;

public interface UnitContext {
    List<String> getImports();

    // TODO unit symbols
    AstNode getRootNode();

    /// TODO generic classes used by the unit to generate class info
    // Generic class info is put into the root unit of a package.
    List<String> getGenericInstances();
}
