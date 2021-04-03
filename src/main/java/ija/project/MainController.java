package ija.project;

import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Pane content;

    private List<Drawable> elements = new ArrayList<>();

    @FXML
    private void onZoom(ScrollEvent event) {
        event.consume();
        double zoom = event.getDeltaY() > 0 ? 1.1 : 0.9;
        System.out.println(content.getScaleX());
        if (content.getScaleX() <= 1 && zoom == 0.9){
            return;
        }
        content.setScaleX(zoom * content.getScaleX());
        content.setScaleY(zoom * content.getScaleY());
        if (content.getScaleX() < 1){
            content.setScaleX(1);
            content.setScaleY(1);
        }
        content.layout();
    }

    public void setElements(List<Drawable> elements) {
        this.elements = elements;
        for (Drawable drawable : elements){
            content.getChildren().addAll(drawable.getGUI());
        }
    }
}
