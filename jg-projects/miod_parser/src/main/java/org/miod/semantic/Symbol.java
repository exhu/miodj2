package org.miod.semantic;

import org.miod.ast.AstNode;

public interface Symbol {
    AstNode getNode();

    String getName();

    String getGlobalName();
}
