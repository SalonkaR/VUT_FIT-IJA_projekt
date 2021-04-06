package ija.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
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
    @FXML
    private Slider speed;

    private Data data;

    private List<Drawable> elements = new ArrayList<>();
    private List<Mover> movers = new ArrayList<>();
    private List<Shape> informations = new ArrayList<>();
    private Drawable selected;

    private Timer timer;
    private LocalTime time =  LocalTime.now();

    @FXML
    private void chengeTime() {

        if (speed.getValue() == 0) {
            timer.cancel();
            this.startTime(false, 101 - (int)(speed.getValue()));
            System.out.println("paused");
        }else{
            timer.cancel();
            this.startTime(true, 101 - (int)(speed.getValue()));
            System.out.println("speed set on "+ (105 - (int)(speed.getValue())));
        }
    }

    @FXML
    private void onZoom(ScrollEvent event) {
        event.consume();
        double zoom = event.getDeltaY() > 0 ? 1.1 : 0.9;

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
            if (drawable instanceof Mover){
                movers.add((Mover) drawable);
            }
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setInformation(List<Drawable> elemensts){
        if (selected != null) {
            informations.clear();
            informations.addAll(selected.updateInfo());
            selected.infoClear();
            sideBar.getChildren().addAll(informations);
        }

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
        if (informations.size() > 0) {
            sideBar.getChildren().addAll(informations);
        }
    }

    public void makeUpdates(){
        for (Mover mover : movers){
            mover.update();
        }
    }

    public void startTime(boolean run, int scale){
        if (run) {
            timer = new Timer(false);
            MainController controler = this;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time = time.plusSeconds(1);
                    Platform.runLater(() -> {
                        controler.setInformation(elements);
                        controler.makeUpdates();
                    });
                }
            }, 0, scale);
        }else{
            timer = new Timer(false);
            MainController controler = this;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time = time.plusSeconds(0);
                    Platform.runLater(() -> {
                        controler.setInformation(elements);
                    });
                }
            }, 0, scale);
        }
    }


}
