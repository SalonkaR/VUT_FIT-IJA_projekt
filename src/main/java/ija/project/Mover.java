package ija.project;

import javafx.scene.shape.Line;

import java.util.List;

public interface Mover {
    void update();
    List<Line> getPath();
}
