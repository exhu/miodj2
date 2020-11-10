package org.miod.semantic;

import java.util.Optional;

public interface UnitResolver {
    void resolveUnit(UnitContext ctx);

    Optional<UnitContext> getUnit(String name);
}
