package org.miod.semantic;

import java.util.List;

import org.miod.ast.CompUnit;

public interface UnitContext {
    List<String> getImports();

    // TODO unit symbols
    CompUnit getRootNode();

    /// TODO generic classes used by the unit to generate class info
    // Generic class info is put into the root unit of a package.
    List<String> getGenericInstances();
}
