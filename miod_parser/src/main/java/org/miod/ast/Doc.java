package org.miod.ast;

public final class Doc extends AstNode {
    private final String text;

    public Doc(NodeLocation loc, String text) {
        super(loc);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}