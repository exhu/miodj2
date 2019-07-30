package org.miod.ast;

// "int" type node.
public final class IntegerTypeDecl extends TypeDecl {
    public final static IntegerTypeDecl NODE = new IntegerTypeDecl();

    private IntegerTypeDecl() {
        super(NodeLocation.MAGIC_LOCATION);
    }
}