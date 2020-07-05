package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	FigureButton rectangleButton = new FigureButton("Rectángulo") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY())
				return new Rectangle(fillColorPicker.getValue(), borderColorPicker.getValue(), slider.getValue(), start, end);
			return null;
		}
	};
	FigureButton circleButton = new FigureButton("Círculo") {
		@Override
		public Figure create(Point start, Point end) {
			return new Circle(fillColorPicker.getValue(), borderColorPicker.getValue(), slider.getValue(), start, start.distanceTo(end));
		}
	};
	FigureButton squareButton = new FigureButton("Cuadrado") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY())
				return new Square(fillColorPicker.getValue(), borderColorPicker.getValue(), slider.getValue(), start, new Point(end.getX(),start.getY() + end.getX() - start.getX()));
			return null;
		}
	};
	FigureButton ellipseButton = new FigureButton("Elipse") {
		@Override
		public Figure create(Point start, Point end) {
			if(start.getX() < end.getX() && start.getY() < end.getY()) {
				double diffX = end.getX() - start.getX();
				double diffY = end.getY() - start.getY();
				Point center = new Point(end.getX() - diffX / 2, end.getY() - diffY / 2);
				return new Ellipse(fillColorPicker.getValue(), borderColorPicker.getValue(), slider.getValue(), center, diffX / 2, diffY / 2);
			}
			return null;
		}
	};
	FigureButton lineButton = new FigureButton("Línea") {
		@Override
		public Figure create(Point start, Point end) {
			return new Line(fillColorPicker.getValue(), borderColorPicker.getValue(), slider.getValue(), start, end);
		}
	};

	Label borderLabel = new Label("Borde");
	final ColorPicker borderColorPicker = new ColorPicker(lineColor);

	final ColorPicker fillColorPicker = new ColorPicker(fillColor);

	Slider slider = new Slider(1, 50, 25);
	Label fillLabel = new Label("Relleno");
	//Seleccionar un botón
	FigureButton selectedButton;

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.valueProperty().addListener((ov, oldValue, newValue) -> {
			if (selectedFigure != null)
				selectedFigure.setBorderWidth(newValue.doubleValue());
		});
		slider.setOnMouseDragged(event -> {
		    if(selectedFigure != null) {
				selectedFigure.setBorderWidth(slider.getValue());
				redrawCanvas();
			}
        });
		borderColorPicker.setOnAction(event -> {
			if(selectedFigure != null)
				selectedFigure.setBorderColor(borderColorPicker.getValue());
		});

		fillColorPicker.setOnAction(event -> {
			if(selectedFigure != null) {
				selectedFigure.setFillColor(fillColorPicker.getValue());
				redrawCanvas();
			}

		});
		selectionButton.setOnAction(event -> selectedButton = null);
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().addAll(borderLabel, slider, borderColorPicker, fillLabel, fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);
		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));
		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			Figure newFigure = null;
			if (selectedButton != null) {
				newFigure = selectedButton.create(startPoint, endPoint);
			}
			if(newFigure != null)
				canvasState.addFigure(newFigure);
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
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if(figure.contains(eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure.toString());
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX());
				double diffY = (eventPoint.getY() - startPoint.getY());
				if(selectedFigure != null) {
					selectedFigure.move(diffX,diffY);
				}
				startPoint = eventPoint;
				redrawCanvas();
			}
		});
		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(figure.getBorderColor());
			}
            gc.setLineWidth(figure.getBorderWidth());
            gc.setFill(figure.getFillColor());
			figure.drawSelf(gc);
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
