package ija.project;

import javafx.scene.shape.Shape;

import java.util.List;

public interface Drawable {
    List<Shape> getGUI();
    List<Shape> getInfo();
    List<Shape> updateInfo();
    void off();
    void infoClear();

}
