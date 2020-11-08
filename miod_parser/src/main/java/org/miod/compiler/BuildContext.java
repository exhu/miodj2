package org.miod.compiler;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface BuildContext {
    List<Path> getSources();

    /// TODO generic classes used by the package to generate class info
    Set<String> getGenericInstances();
}
