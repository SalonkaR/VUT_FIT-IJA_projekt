package ija.project;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.GREEN;

public class Carriage implements Drawable, Mover {
    private String name;
    private Coordinates position;
    private int speed = 2;
    private int status;
    private Circle mainCircle;

    private List<Shape> gui = new ArrayList<>();
    private List<Shape> info = new ArrayList<>();

    public Carriage(String name, Coordinates position) {
        this.name = name;
        this.position = position;
        this.status = 0;
        mainCircle = new Circle(position.getX(), position.getY(), 6, GREEN);
        gui.add(mainCircle);


    }

    public String getName() {
        return name;
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public void off() {

    }

    @Override
    public void infoClear() {

    }

    @Override
    public void update() {
        mainCircle.setTranslateY(mainCircle.getTranslateY() + speed);
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                ", status=" + status +
                ", mainCircle=" + mainCircle +
                ", gui=" + gui +
                ", info=" + info +
                '}';
    }
}
