package org.miod.parser;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Logger;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public final class ParsingErrorListener implements ANTLRErrorListener {
    public static final class ErrorDescription {
        public final Object symbol;
        public final int line;
        public final int pos;
        public final String msg;

        public ErrorDescription(Object symbol, int line, int pos, String msg) {
            this.symbol = symbol;
            this.line = line;
            this.pos = pos;
            this.msg = msg;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ParsingErrorListener.class.getName());
    private final List<ErrorDescription> errors = new ArrayList<>();

    public List<ErrorDescription> getErrors() {
        return errors;
    }

    public ParsingErrorListener() {
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
            BitSet ambigAlts, ATNConfigSet configs) {

        LOGGER.severe("reportAmbiguity");
        throw new UnsupportedOperationException();
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
            BitSet conflictingAlts, ATNConfigSet configs) {

        LOGGER.severe("reportAttemptingFullContext");
        throw new UnsupportedOperationException();
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
            ATNConfigSet configs) {

        LOGGER.severe("reportContextSensitivity");
        throw new UnsupportedOperationException();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) {

        LOGGER.warning(String.format("syntaxError at %d, %d: %s", line, charPositionInLine, msg));
        errors.add(new ErrorDescription(offendingSymbol, line, charPositionInLine, msg));
    }
}