package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new LinkedList<>();

    public void addFigure(Figure figure) {
        list.add(figure);
    }

    public Iterable<Figure> figures() {
        return list;
    }

    public void removeSelected(Collection<Figure> c){
        list.removeAll(c);
    }

    public void bringToFront(Collection<Figure> c){
        removeSelected(c);
        list.addAll(list.size(), c);
    }

    public void sendToBack(Collection<Figure> c){
        removeSelected(c);
        list.addAll(0, c);
    }

}
