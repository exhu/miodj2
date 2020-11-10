package org.miod.compiler;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.miod.semantic.UnitContext;

public interface BuildContext {
    List<Path> getSources();

    List<Path> getImportPaths();

    Set<String> getBuildTags();

    /// (package::unit::..unit)
    Map<String, UnitContext> getProcessedUnits();

    Path unitPathFromName(String unitName);
}
