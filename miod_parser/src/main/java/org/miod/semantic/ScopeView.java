package org.miod.semantic;

import java.util.Map;
import java.util.Optional;

public interface ScopeView {
    ScopeView getParentScopeView();

    Optional<Symbol> resolveSymbol(String name);

    Map<String, Symbol> getLocalSymbols();
}
