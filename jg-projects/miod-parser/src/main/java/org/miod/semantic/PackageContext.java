package org.miod.semantic;

import java.util.Map;
import java.util.Set;

// TODO remove
@Deprecated
public interface PackageContext {
    /// root unit name/package name
    String getName();

    /// processed units within this package, key = full unit name
    /// (package::unit::..unit)
    Map<String, UnitContext> getProcessedUnits();

    /// imported packages with their units, key = only root unit name (package name)
    Map<String, PackageContext> getImportedPackages();

    /// TODO generic classes used by the package to generate class info
    Set<String> getGenericInstances();
}