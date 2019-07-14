package org.miod.parser;

import java.net.URL;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.miod.ast.CompUnit;
import org.miod.ast.NodeLocation;
import org.miod.parser.generated.MiodBaseListener;
import org.miod.parser.generated.MiodParser.CompUnitContext;

public final class AstBuilder extends MiodBaseListener {
    private CompUnit root;
    private URL rootUrl;

    public AstBuilder() {
    }

    public void parse(ParseTree tree, URL rootUrl) {
        if (root != null) {
            throw new RuntimeException("already parsed!");
        }
        this.rootUrl = rootUrl;
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    public CompUnit getRoot() {
        return root;
    }

    @Override
    public void exitCompUnit(CompUnitContext ctx) {
        NodeLocation rootLoc = ParserUtils.locationFromContext(ctx, rootUrl);
        root = new CompUnit(rootLoc, ctx.unitHeader().bareName().getText());
    }
}