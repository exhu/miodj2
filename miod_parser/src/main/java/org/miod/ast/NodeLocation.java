package org.miod.ast;

import java.net.URL;

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
    public final URL url;

    public NodeLocation(Point start, Point end, URL url) {
        this.start = start;
        this.end = end;
        this.url = url;
    }
}