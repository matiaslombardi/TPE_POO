package frontend;

import backend.CanvasState;
import backend.ColorProperty;
import backend.Observer;
import backend.Selector;
import backend.model.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class PaintPane extends BorderPane {

	// Constants
	private final static double CANVAS_WIDTH = 800;
	private final static double CANVAS_HEIGHT = 600;
	private final static double SLIDER_MIN_VALUE = 1;
	private final static double SLIDER_MAX_VALUE = 50;
	private final static double SLIDER_INITIAL_VALUE = 25;
	private final static double TOOL_MIN_WIDTH = 90;
	private final static double VBOX_SPACING = 10;
	private final static double VBOX_WIDTH = 100;
	private final static double PADDING_INSETS_VALUE = 5;
	private final static Color SELECTED_COLOR = Color.RED;
	private final static Color LINE_DEFAULT_COLOR = Color.BLACK;
	private final static Color FILL_DEFAULT_COLOR = Color.YELLOW;

	// BackEnd
	private CanvasState canvasState;

	// Canvas y relacionados
	private Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	private GraphicsContext gc = canvas.getGraphicsContext2D();

	//Drawers
	private Observer rectangleDrawer = data -> {
		gc.fillRect(data.getFirstX(), data.getFirstY(),
				data.getSecondX(), data.getSecondY());
		gc.strokeRect(data.getFirstX(), data.getFirstY(),
				data.getSecondX(), data.getSecondY());
	};
	private Observer ellipseDrawer = data -> {
		gc.fillOval(data.getFirstX(),data.getFirstY(),data.getSecondX(),data.getSecondY());
		gc.strokeOval(data.getFirstX(),data.getFirstY(),data.getSecondX(),data.getSecondY());
	};
	private Observer lineDrawer = data -> gc.strokeLine(data.getFirstX(),data.getFirstY(),data.getSecondX(),data.getSecondY());

	// Botones Barra Izquierda
	private ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private FigureButton rectangleButton = new FigureButton("Rectángulo") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY()) {
				Rectangle toReturn = new Rectangle(fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue(), start, end);
				toReturn.addObserver(rectangleDrawer);
				return toReturn;
			}
			updateLabelError("Rectangle must be created from top left to bottom right");
			return null;
		}
	};
	private FigureButton circleButton = new FigureButton("Círculo") {
		@Override
		public Figure create(Point start, Point end) {
			Circle toReturn = new Circle(fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue(), start, start.distanceTo(end));
			toReturn.addObserver(ellipseDrawer);
			return toReturn;
		}
	};
	private FigureButton squareButton = new FigureButton("Cuadrado") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY()) {
				Square toReturn = new Square(fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue(), start, new Point(end.getX(), start.getY() + end.getX() - start.getX()));
				toReturn.addObserver(rectangleDrawer);
				return toReturn;
			}
			updateLabelError("Square must be created from top left to bottom right");
			return null;
		}
	};
	private FigureButton ellipseButton = new FigureButton("Elipse") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY()) {
				double diffX = end.getX() - start.getX();
				double diffY = end.getY() - start.getY();
				Point center = new Point(end.getX() - diffX / 2, end.getY() - diffY / 2);
				Ellipse toReturn = new Ellipse(fillColorPicker.getValue(), borderColorPicker.getValue(), borderWidthSlider.getValue(), center, diffX / 2, diffY / 2);
				toReturn.addObserver(ellipseDrawer);
				return toReturn;
			}
			updateLabelError("Ellipse must be created from top left to bottom right");
			return null;
		}
	};
	private FigureButton lineButton = new FigureButton("Línea") {
		@Override
		public Figure create(Point start, Point end) {
			Line toReturn = new Line(borderColorPicker.getValue(), borderWidthSlider.getValue(), start, end);
			toReturn.addObserver(lineDrawer);
			return toReturn;
		}
	};
	private ToggleButton deleteButton = new ToggleButton("Borrar");
	private ToggleButton toBackButton = new ToggleButton("Al Fondo");
	private ToggleButton toFrontButton = new ToggleButton("Al Frente");

	private Label borderLabel = new Label("Borde");
	private final ColorPicker borderColorPicker = new ColorPicker(LINE_DEFAULT_COLOR);

	private final ColorPicker fillColorPicker = new ColorPicker(FILL_DEFAULT_COLOR);

	private Slider borderWidthSlider = new Slider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE, SLIDER_INITIAL_VALUE);
	private Label fillLabel = new Label("Relleno");

	//Seleccionar un botón
	private FigureButton selectedButton;

	// Dibujar una figura
	private Point startPoint;

	// StatusBar
	private StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		borderWidthSlider.setShowTickLabels(true);
		borderWidthSlider.setShowTickMarks(true);
		EventHandler<MouseEvent> sliderEvent = mouseEvent -> {
			if (canvasState.hasSelectedFigures()) {
				canvasState.setSelectedBordersWidth(borderWidthSlider.getValue());
				redrawCanvas();
			}
		};
		borderWidthSlider.setOnMouseDragged(sliderEvent);
		borderWidthSlider.setOnMouseClicked(sliderEvent);
		borderColorPicker.setOnAction(event -> canvasState.setSelectedBordersColor(borderColorPicker.getValue()));
		fillColorPicker.setOnAction(event -> {
			if( canvasState.hasSelectedFigures() ) {
				canvasState.setSelectedFillsColor(fillColorPicker.getValue());
				redrawCanvas();
			}

		});
		selectionButton.setOnAction(event -> selectedButton = null);
		deleteButton.setOnAction(event -> {
			if( canvasState.hasSelectedFigures() ){
				canvasState.removeSelected();
				canvasState.clearSelectedFigures();
				redrawCanvas();
			}
		});
		toBackButton.setOnAction(event -> {
			if( canvasState.hasSelectedFigures() ){
				canvasState.sendToBack();
				redrawCanvas();
			}
		});
		toFrontButton.setOnAction(event -> {
			if( canvasState.hasSelectedFigures() ){
				canvasState.bringToFront();
				redrawCanvas();
			}
		});
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton, deleteButton, toBackButton, toFrontButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(TOOL_MIN_WIDTH);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		VBox buttonsBox = new VBox(VBOX_SPACING);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().addAll(borderLabel, borderWidthSlider, borderColorPicker, fillLabel, fillColorPicker);
		buttonsBox.setPadding(new Insets(PADDING_INSETS_VALUE));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(VBOX_WIDTH);
		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));
		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) return ;
			if (selectionButton.isSelected()) {
				canvasState.clearSelectedFigures();
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				if (startPoint.getX() < endPoint.getX() && startPoint.getY() < endPoint.getY()) {
					//Rectangle selection
					Selector selector = point -> point.getX() > startPoint.getX() && point.getX() < endPoint.getX() && point.getY() > startPoint.getY() && point.getY() < endPoint.getY();
					for (Figure figure : canvasState.figures()) {
						if (figure.isContained(selector)) {
							canvasState.addSelectedFigure(figure);
							label.append(String.format("%s ,", figure.toString()));
						}
					}
					label.deleteCharAt(label.length() - 1);
				} else if (startPoint.equals(endPoint)) {
					//Normal click
					Figure last = canvasState.selectContains(startPoint);
					if (last != null) {
						label.append(last.toString());
					}
				}
				if( !canvasState.hasSelectedFigures() )
					label = new StringBuilder("Ninguna figura encontrada");
				statusPane.updateStatus(label.toString());
			} else {
				Figure newFigure;
				if (selectedButton != null) {
					newFigure = selectedButton.create(startPoint, endPoint);
					if (newFigure != null) canvasState.addFigure(newFigure);
				}
			}
			startPoint = null;
			redrawCanvas();
		});
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figure.contains(eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
			statusPane.normalBackground();
		});
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX());
				double diffY = (eventPoint.getY() - startPoint.getY());
				if( canvasState.hasSelectedFigures() ) {
					canvasState.moveSelectedFigures(diffX,diffY);
					startPoint = eventPoint;
					redrawCanvas();
				}
			}
		});
		setLeft(buttonsBox);
		setRight(canvas);
	}

	private void updateLabelError(String message){
		statusPane.updateStatus(message);
		statusPane.errorBackground();
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(canvasState.containsSelectedFigure(figure))
				gc.setStroke(SELECTED_COLOR);
			else
				gc.setStroke(figure.getColorProperty(ColorProperty.BORDER_COLOR));
            gc.setLineWidth(figure.getBorderWidth());
			if (figure.isFillable())
				gc.setFill(figure.getColorProperty(ColorProperty.FILL_COLOR));
			figure.notifyObservers();
		}
	}

	private abstract class FigureButton extends ToggleButton {
		FigureButton(String text) {
			super(text);
		}

		public abstract Figure create(Point start, Point end);

		@Override
		public void fire() {
			super.fire();
			selectedButton = this.isSelected()? this: null;
		}
	}
}