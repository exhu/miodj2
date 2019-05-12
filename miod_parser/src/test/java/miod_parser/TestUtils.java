package miod_parser;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.miod.parser.generated.MiodLexer;
import org.miod.parser.generated.MiodParser;

final class TestUtils {
    public static InputStream getStreamFromResource(String fileName) throws IOException {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return inputStream;
    }

    public static ParseTree parseSyntax(InputStream stream, ANTLRErrorListener listener) throws IOException {
        ParseTree tree;
        CharStream input = CharStreams.fromStream(stream);
        MiodLexer lexer = new MiodLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiodParser parser = new MiodParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
        // run parser
        tree = parser.compUnit();
        return tree;
    }
}