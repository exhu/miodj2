package org.miod.ast;

public final class Comment extends AstNode {
    private final String text;

    public Comment(NodeLocation loc, String text) {
        super(loc);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}