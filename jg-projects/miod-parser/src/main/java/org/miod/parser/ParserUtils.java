package org.miod.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.miod.ast.NodeLocation;
import org.miod.parser.generated.MiodLexer;
import org.miod.parser.generated.MiodParser;

public final class ParserUtils {
    public static InputStream getStreamFromResource(String fileName) throws IOException {
        ClassLoader classLoader = ParserUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return inputStream;
    }

    public static URL getUrlFromResource(String filename) {
        ClassLoader classLoader = ParserUtils.class.getClassLoader();
        URL url = classLoader.getResource(filename);
        return url;
    }

    public static Optional<Path> getPathForResource(String filename) {
        URL url = ParserUtils.getUrlFromResource(filename);
        try {
            return Optional.of(Path.of(url.toURI()));
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    public static ParseTree parseSyntax(InputStream stream, ANTLRErrorListener listener) throws IOException {
        ParseTree tree;
        CharStream input = CharStreams.fromStream(stream);
        MiodLexer lexer = new MiodLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiodParser parser = new MiodParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
        // run parser
        tree = parser.compUnit();
        return tree;
    }

    public static NodeLocation locationFromContext(ParserRuleContext ctx, Path path) {
        return new NodeLocation(new NodeLocation.Point(ctx.start.getLine(), ctx.start.getCharPositionInLine()),
                new NodeLocation.Point(ctx.stop.getLine(), ctx.stop.getCharPositionInLine()), path);
    }
}