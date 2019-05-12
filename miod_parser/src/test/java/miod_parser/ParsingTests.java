/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package miod_parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.miod.parser.ParserUtils;
import org.miod.parser.ParsingErrorListener;
import org.miod.parser.generated.MiodBaseListener;
import org.miod.parser.generated.MiodParser.UnitHeaderContext;

public class ParsingTests {
    private final static Logger LOGGER = Logger.getLogger(ParsingTests.class.getName());

    class MyParseTreeListener extends MiodBaseListener {
        public String pkgName = "";

        MyParseTreeListener() {
        }

        @Override
        public void exitUnitHeader(UnitHeaderContext ctx) {
            pkgName = ctx.bareName().ID().getText();
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
    }

}
