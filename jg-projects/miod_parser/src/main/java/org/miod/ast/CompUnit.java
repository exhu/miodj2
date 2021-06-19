package org.miod.ast;

public final class CompUnit extends AstNode {
    private final String name;

    public CompUnit(NodeLocation loc, String name) {
        super(loc);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}