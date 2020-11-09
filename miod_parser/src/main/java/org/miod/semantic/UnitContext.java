package org.miod.semantic;

import java.util.List;

import org.miod.ast.AstNode;

public interface UnitContext {
    List<String> getImports();

    // TODO unit symbols
    AstNode getRootNode();

}
