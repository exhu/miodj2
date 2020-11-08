package org.miod.semantic;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PackageContext {
    /// root unit name/package name
    String getName();

    List<Path> getImportPaths();

    Set<String> getBuildTags();

    /// processed units within this package, key = full unit name
    /// (package::unit::..unit)
    Map<String, UnitContext> getProcessedUnits();

    /// imported packages with their units, key = only root unit name (package name)
    Map<String, PackageContext> getImportedPackages();
}