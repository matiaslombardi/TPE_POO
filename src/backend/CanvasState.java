package backend;

import backend.model.Figure;
import backend.model.Point;
import javafx.scene.paint.Color;

import java.util.*;

public class CanvasState {

    private final LinkedList<Figure> list = new LinkedList<>();
    private final Set<Figure> selectedFigures = new HashSet<>();

    public void addFigure(Figure figure) {
        list.add(figure);
    }
    public void addSelectedFigure(Figure figure) {
        selectedFigures.add(figure);
    }

    //This method returns a copy for safety reasons
    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

    public void removeSelected(){
        list.removeAll(selectedFigures);
    }

    public boolean hasSelectedFigures(){
        return !selectedFigures.isEmpty();
    }
    public void setSelectedBordersWidth(double width){
        selectedFigures.forEach(figure -> figure.setBorderWidth(width));
    }
    public void setSelectedBordersColor(Color color){
        selectedFigures.forEach(figure -> figure.setColorProperty(ColorProperty.BORDER_COLOR, color));
    }
    public void setSelectedFillsColor(Color color){
        selectedFigures.forEach(figure -> {
            if (figure.isFillable())
                figure.setColorProperty(ColorProperty.FILL_COLOR, color);
        });
    }
    public void clearSelectedFigures(){
        selectedFigures.clear();
    }

    public boolean containsSelectedFigure(Figure figure){
        return selectedFigures.contains(figure);
    }

    public void moveSelectedFigures(double diffX, double diffY){
        selectedFigures.forEach(figure -> figure.move(diffX,diffY));
    }

    public void bringToFront(){
        removeSelected();
        list.addAll(selectedFigures);
    }

    public void sendToBack(){
        removeSelected();
        list.addAll(0, selectedFigures);
    }

    //To select only one figure
    public Figure selectContains(Point point){
        Iterator<Figure> it = list.descendingIterator();
        while (it.hasNext()){
            Figure aux = it.next();
            if(aux.contains(point)){
                addSelectedFigure(aux);
                return aux;
            }
        }
        return null;
    }

}
