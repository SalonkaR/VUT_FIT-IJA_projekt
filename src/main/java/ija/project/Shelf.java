package ija.project;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

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
        gui.add(new Rectangle(position.getX(), position.getY(), width, height));
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }
}
