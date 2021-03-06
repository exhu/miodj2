package org.miod.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.miod.ast.CompUnit;
import org.miod.compiler.errors.ReadError;
import org.miod.compiler.errors.SyntaxError;
import org.miod.compiler.errors.WrongUnitName;
import org.miod.parser.AstBuilder;
import org.miod.parser.ParserUtils;
import org.miod.parser.ParsingErrorListener;

public final class FrontendImpl implements Frontend {
    private static final Logger LOGGER = Logger.getLogger(FrontendImpl.class.getName());
    private final BuildContext buildContext;

    @Override
    public SemanticResult analyze(CompUnit unitNode) {
        return new Analyzer(unitNode).analyze();
    }

    @Override
    public SemanticResults analyzeAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ParseResult check(Path unitPath) {
        ParsingErrorListener errListener = new ParsingErrorListener();
        ParseTree tree = null;
        try (InputStream input = Files.newInputStream(unitPath)) {
            tree = ParserUtils.parseSyntax(input, errListener);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            return new ParseResult(List.of(new ReadError(unitPath)), null);
        }

        if (errListener.getErrors().isEmpty()) {
            AstBuilder builder = new AstBuilder();
            builder.parse(tree, unitPath);
            final String unitName = builder.getRoot().getName();
            final Optional<Path> unitPathFromName = buildContext.unitPathFromName(unitName);
            if (unitPathFromName.isPresent() && unitPath.equals(unitPathFromName.get())) {
                return new ParseResult(null, builder.getRoot());
            } else {
                return new ParseResult(List.of(new WrongUnitName(unitName, unitPath)), null);
            }
        }

        return new ParseResult(List.of(new SyntaxError(unitPath, errListener.getErrors().toString())), null);
    }

    @Override
    public void generateAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public void optimizeAll() {
        // TODO Auto-generated method stub

    }

    public FrontendImpl(BuildContext buildContext) {
        this.buildContext = buildContext;
    }
}
