package ija.project;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class Shelf implements Drawable {
    private Cordinate position;
    private double width = 50;
    private double height = 50;

    private List<Shape> gui;

    public Shelf(Cordinate position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        gui = new ArrayList<>();
        Rectangle rect = new Rectangle(position.getX(), position.getY(), width, height);
        rect.setFill(SKYBLUE);
        gui.add(rect);

    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }
}
