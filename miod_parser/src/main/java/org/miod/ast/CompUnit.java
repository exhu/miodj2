package org.miod.ast;

public final class CompUnit extends AstNode {
    private final String packageName;
    public CompUnit(NodeLocation loc, String packageName) {
        super(loc);
        this.packageName = packageName;
    }
}