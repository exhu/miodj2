package org.miod.ast;

abstract class AstNode extends BaseAstNode {
    private final NodeLocation location;

    public AstNode(NodeLocation location) {
        this.location = location;    
    }

    public final NodeLocation getLocation() {
        return location;
    }
}