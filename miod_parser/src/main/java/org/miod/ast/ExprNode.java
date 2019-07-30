package org.miod.ast;

/// Any node that requires evaluation
public abstract class ExprNode extends AstNode {
    public ExprNode(NodeLocation loc) {
        super(loc);
    }
}