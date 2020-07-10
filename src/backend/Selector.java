package backend;

import backend.model.Point;

@FunctionalInterface
public interface Selector {
    boolean contains(Point point);
}
