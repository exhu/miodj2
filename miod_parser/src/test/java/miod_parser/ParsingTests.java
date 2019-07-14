/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package miod_parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.miod.parser.AstBuilder;
import org.miod.parser.ParserUtils;
import org.miod.parser.ParsingErrorListener;
import org.miod.parser.generated.MiodBaseListener;
import org.miod.parser.generated.MiodParser.CommentsContext;
import org.miod.parser.generated.MiodParser.DocsContext;
import org.miod.parser.generated.MiodParser.UnitHeaderContext;

public class ParsingTests {
    private final static Logger LOGGER = Logger.getLogger(ParsingTests.class.getName());

    static class MyParseTreeListener extends MiodBaseListener {
        public String pkgName = "";
        public final List<String> comments = new ArrayList<>();
        public final List<String> docs = new ArrayList<>();

        MyParseTreeListener() {
        }

        @Override
        public void exitUnitHeader(UnitHeaderContext ctx) {
            pkgName = ctx.bareName().ID().getText();
        }

        @Override
        public void exitComments(CommentsContext ctx) {
            comments.add(ctx.getText());
        }

        @Override
        public void exitDocs(DocsContext ctx) {
            docs.add(ctx.getText());
        }
    }

    @Test
    public void testSyntax1() throws IOException {
        ParsingErrorListener errListener = new ParsingErrorListener();
        ParseTree tree = ParserUtils.parseSyntax(ParserUtils.getStreamFromResource("t1.miod"), errListener);

        assertNotNull(tree);
        assertTrue(errListener.getErrors().isEmpty());

        MyParseTreeListener listener = new MyParseTreeListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        assertEquals("t1", listener.pkgName);
        assertEquals("# comments1\n", listener.comments.get(0));
        assertEquals("## docs1\n", listener.docs.get(0));
    }

    @Test
    public void testAstBuilder() throws IOException, MalformedURLException {
        ParsingErrorListener errListener = new ParsingErrorListener();
        URL url = ParserUtils.getUrlFromResource("t1.miod");
        ParseTree tree = ParserUtils.parseSyntax(url.openStream(), errListener);

        assertTrue(errListener.getErrors().isEmpty());

        AstBuilder builder = new AstBuilder();
        builder.parse(tree, url);
        LOGGER.info(builder.getRoot().getPackageName());
        assertEquals("t1", builder.getRoot().getPackageName());
    }

}
