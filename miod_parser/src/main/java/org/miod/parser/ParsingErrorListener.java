package org.miod.parser;

import java.util.BitSet;
import java.util.logging.Logger;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public final class ParsingErrorListener implements ANTLRErrorListener {
    private static final Logger LOGGER = Logger.getLogger(ParsingErrorListener.class.getName());

    public ParsingErrorListener() {}

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
            BitSet ambigAlts, ATNConfigSet configs) {

        LOGGER.severe("reportAmbiguity");
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
            BitSet conflictingAlts, ATNConfigSet configs) {

        LOGGER.severe("reportAttemptingFullContext");
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
            ATNConfigSet configs) {

        LOGGER.severe("reportContextSensitivity");
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) {

        LOGGER.severe("syntaxError: " + msg);
    }
}