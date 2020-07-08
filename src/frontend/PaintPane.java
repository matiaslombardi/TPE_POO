package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

import java.util.*;

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
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton toBackButton = new ToggleButton("Al Fondo");
	ToggleButton toFrontButton = new ToggleButton("Al Frente");

	Label borderLabel = new Label("Borde");
	final ColorPicker borderColorPicker = new ColorPicker(lineColor);

	final ColorPicker fillColorPicker = new ColorPicker(fillColor);

	Slider slider = new Slider(1, 50, 25);
	Label fillLabel = new Label("Relleno");

	//Seleccionar un botón
	FigureButton selectedButton;

	// Dibujar una figura
	Point startPoint;



	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		EventHandler<MouseEvent> sliderEvent = mouseEvent -> {
			if (canvasState.hasSelectedFigures()) {
				canvasState.setSelectedBordersWidth(slider.getValue());
				redrawCanvas();
			}
		};
		slider.setOnMouseDragged(sliderEvent);
		slider.setOnMouseClicked(sliderEvent);
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
			if (selectionButton.isSelected()) {
				canvasState.clearSelectedFigures();
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				if (startPoint.getX() < endPoint.getX() && startPoint.getY() < endPoint.getY()) {
					//Rectangle selection
					Rectangle selection = new Rectangle(startPoint, endPoint);
					for (Figure figure : canvasState.figures()) {
						if (figure.isContained(selection)) {
							canvasState.addSelectedFigure(figure);
							label.append(String.format("%s ,", figure.toString()));
						}
					}
					label.deleteCharAt(label.length() - 1);
				} else if (startPoint.equals(endPoint)) {
					//Normal click
					Figure last = null;
					for (Figure figure : canvasState.figures()) {
						if (figure.contains(startPoint))
							last = figure;
					}
					if (last != null) {
						canvasState.addSelectedFigure(last);
						label.append(last.toString());
					}
				}
				if( !canvasState.hasSelectedFigures() )
					label = new StringBuilder("Ninguna figura encontrada");
				statusPane.updateStatus(label.toString());
			} else {
				Figure newFigure = null;
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

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(canvasState.containsSelectedFigure(figure)) {
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
