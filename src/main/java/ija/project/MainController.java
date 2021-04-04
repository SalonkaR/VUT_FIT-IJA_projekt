package ija.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    @FXML
    private Pane content;
    @FXML
    private Pane sideBar;

    private List<Drawable> elements = new ArrayList<>();
    private List<Shape> informations = new ArrayList<>();
    private Drawable selected;

    private Timer timer;
    private LocalTime time =  LocalTime.now();

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

    public void setInformation(List<Drawable> elemensts){
        for (Drawable drawable : elemensts){
            if (drawable.getInfo().size() > 0){
                informations.clear();
                informations.addAll((drawable.getInfo()));
                if (selected != null && selected != drawable){
                    selected.off();
                }
                selected = drawable;
                drawable.infoClear();
            }
        }
        sideBar.getChildren().clear();

        sideBar.getChildren().add(informations.get(0));
    }

    public void startTime(){
        timer = new Timer(false);
        MainController controler = this;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                Platform.runLater(() -> {
                    controler.setInformation(elements);
                });
                System.out.println("clock");
            }
        },0, 1000);
    }


}
