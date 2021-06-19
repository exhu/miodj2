package org.miod.ast.builtins;

import org.miod.ast.NodeLocation;
import org.miod.ast.TypeDecl;

// "int" type node.
public final class IntegerTypeDecl extends TypeDecl {
    public final static IntegerTypeDecl NODE = new IntegerTypeDecl();

    private IntegerTypeDecl() {
        super(NodeLocation.MAGIC_LOCATION);
    }
}