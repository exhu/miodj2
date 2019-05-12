package org.miod.ast;

import java.net.URL;

public final class NodeLocation {
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
    public final URL url;

    public NodeLocation(Point start, Point end, URL url) {
        this.start = start;
        this.end = end;        
        this.url = url;
    }

}