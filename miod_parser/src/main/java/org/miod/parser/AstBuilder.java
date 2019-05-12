package org.miod.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.antlr.v4.runtime.tree.ParseTree;
import org.miod.ast.CompUnit;
import org.miod.ast.NodeLocation;
import org.miod.parser.generated.MiodBaseListener;

final class AstBuilder extends MiodBaseListener {
    private CompUnit root;

    public AstBuilder() {       
    }

    public void parse(ParseTree tree) {
        URL rootUrl;

        try {
            rootUrl = new URL("file://");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //throw e;
            return;
        }

        NodeLocation rootLoc = new NodeLocation(new NodeLocation.Point(0, 0), new NodeLocation.Point(0, 0),
                rootUrl);
        // TODO
        root = new CompUnit(rootLoc, "root");

    }

    public CompUnit getRoot() {
        return root;
    }
}