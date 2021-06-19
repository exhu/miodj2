package org.miod.ast;

import java.nio.file.Path;

public final class NodeLocation {
    public static final Point ZERO_POINT = new Point(0, 0);
    public static final NodeLocation MAGIC_LOCATION;
    static {
        MAGIC_LOCATION = new NodeLocation(ZERO_POINT, ZERO_POINT, null);
    }

    public static class Point {
        public final int line;
        public final int column;

        public Point(int line, int column) {
            this.line = line;
            this.column = column;
        }
    }

    public final Point start;
    public final Point end;
    // can be null
    public final Path path;

    public NodeLocation(Point start, Point end, Path path) {
        this.start = start;
        this.end = end;
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%d - %d,%d)", path != null ? path.toString() : "null", start.line, start.column,
                end.line, end.column);
    }
}