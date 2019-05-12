package org.miod.semantic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PackageContext {
    private final List<Path> importPaths = new ArrayList<>();
    private final Set<String> buildTags = new HashSet<>();
    
}