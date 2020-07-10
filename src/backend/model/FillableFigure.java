package backend.model;

import backend.ColorProperty;
import javafx.scene.paint.Color;

public abstract class FillableFigure extends Figure {

    public FillableFigure(Color fillColor, Color borderColor, double borderWidth) {
        super(borderColor, borderWidth);
        setColorProperty(ColorProperty.FILL_COLOR, fillColor);
    }

    @Override
    public boolean isFillable() {
        return true;
    }
    
}
