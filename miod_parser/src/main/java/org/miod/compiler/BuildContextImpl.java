package org.miod.compiler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.miod.semantic.UnitContext;

public final class BuildContextImpl implements BuildContext {
    private final Set<String> buildTags;
    private final List<Path> importPaths;
    private final Map<String, UnitContext> processedUnits = new HashMap<>();
    private final List<Path> sources;

    // produced files
    private final Path outputPath;

    @Override
    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public Set<String> getBuildTags() {
        return buildTags;
    }

    @Override
    public List<Path> getImportPaths() {
        return importPaths;
    }

    @Override
    public Map<String, UnitContext> getProcessedUnits() {
        return processedUnits;
    }

    @Override
    public List<Path> getSources() {
        return sources;
    }

    @Override
    public Optional<Path> unitPathFromName(String unitName) {
        Path relative = Paths.get(unitName.replace("::", "/") + ".miod");
        for (Path path : importPaths) {
            Path absolute = path.resolve(relative);
            if (absolute.toFile().exists()) {
                return Optional.of(absolute);
            }
        }
        return Optional.empty();
    }

    public BuildContextImpl(Set<String> buildTags, List<Path> importPaths, List<Path> sources, Path outputPath) {
        this.buildTags = buildTags;
        this.importPaths = importPaths;
        this.sources = sources;
        this.outputPath = outputPath;
    }
}
