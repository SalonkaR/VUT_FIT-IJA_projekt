package ija.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button start;
    @FXML
    private Button pause;
    @FXML
    private Button reset;
    @FXML
    private Button showLines;

    private int refresher = 55;

    private Data data;

    private List<Drawable> elements = new ArrayList<>();
    private List<Mover> movers = new ArrayList<>();
    private List<Shape> informations = new ArrayList<>();
    private List<Shape> path = new ArrayList<>();
    private List<OrderGenerator2> orderGenerators;
    private Drawable selected;
    private boolean lines = false;

    private Timer timer;
    private LocalTime time =  LocalTime.now();

    @FXML
    private void chengeTime() {
        timer.cancel();
        refresher = (110 - (int)(speed.getValue()));
        this.startTime(true);
    }

    @FXML
    private void showLines(){
        lines = !lines;
        if (!lines){
            content.getChildren().removeAll(path);
            path.clear();
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
    @FXML
    private void start() {
        timer.cancel();
        this.startTime(true);
    }

    @FXML
    private void pause() {
        timer.cancel();
        this.startTime(false);
    }

    @FXML
    private void resetAll() {
        selected = null;
        sideBar.getChildren().clear();
        informations = new ArrayList<>();
        content.getChildren().clear();
        for(Drawable drawable : elements){
            drawable.reset();
            content.getChildren().addAll(drawable.getGUI());
        }
        for (Goods goods : data.getGoods()){
            goods.reset();
        }
        for (ItemGenerator iGem : data.getItemGenerators()){
            iGem.createIt();
        }
        for (OrderGenerator2 oGen : orderGenerators){
            oGen.reset();
        }

    }

    public void generateOrders(){
        for (OrderGenerator2 generator : orderGenerators){
            generator.generate();
        }
    }

    public void setOrderGenerators(List<OrderGenerator2> orderGenerators) {
        this.orderGenerators = orderGenerators;
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

        if (!lines){
            return;
        }

        content.getChildren().removeAll(path);
        path.clear();
        if (selected instanceof Mover){

            Mover mover = (Mover)selected;
            path.addAll(mover.getPath());
        }
        if (path.size() > 0){
            content.getChildren().addAll(path);
        }
    }

    public void makeUpdates(){
        for (Mover mover : movers){
            mover.update();
        }
    }

    public void startTime(boolean run){
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
                        controler.generateOrders();
                    });
                }
            }, 0, refresher);
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
            }, 0, refresher);
        }
    }


}
