package org.miod.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAstNode {
    private final List<BaseAstNode> subnodes = new ArrayList<>();

    final public List<BaseAstNode> getSubnodes() {
        return subnodes;
    }
    
}