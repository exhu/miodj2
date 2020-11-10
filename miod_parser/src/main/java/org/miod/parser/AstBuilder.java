package org.miod.parser;

import java.nio.file.Path;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.miod.ast.AstNode;
import org.miod.ast.Comment;
import org.miod.ast.CompUnit;
import org.miod.ast.Doc;
import org.miod.ast.NodeLocation;
import org.miod.parser.generated.MiodBaseListener;
import org.miod.parser.generated.MiodParser.CommentsContext;
import org.miod.parser.generated.MiodParser.CompUnitContext;
import org.miod.parser.generated.MiodParser.DocsContext;

public final class AstBuilder extends MiodBaseListener {
    private CompUnit root;
    private Path unitPath;
    private AstNode prevParent = null;

    public AstBuilder() {
    }

    public void parse(ParseTree tree, Path unitPath) {
        if (root != null) {
            throw new RuntimeException("already parsed!");
        }
        this.unitPath = unitPath;
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    public CompUnit getRoot() {
        return root;
    }

    @Override
    public void enterCompUnit(CompUnitContext ctx) {
        NodeLocation rootLoc = ParserUtils.locationFromContext(ctx, unitPath);
        root = new CompUnit(rootLoc, ctx.unitHeader().fullId().getText());
        prevParent = root;
    }

    @Override
    public void exitComments(CommentsContext ctx) {
        NodeLocation loc = ParserUtils.locationFromContext(ctx, unitPath);
        prevParent.getSubnodes().add(new Comment(loc, ctx.getText()));
    }

    @Override
    public void exitDocs(DocsContext ctx) {
        NodeLocation loc = ParserUtils.locationFromContext(ctx, unitPath);
        prevParent.getSubnodes().add(new Doc(loc, ctx.getText()));
    }

}