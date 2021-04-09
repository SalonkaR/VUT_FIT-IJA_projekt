package ija.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Trieda MainControler slúži na riadenie aplikacie.
 *
 * @author Matúš Tvarožný -  xtvaroh00
 * @version 1.0
 */
public class MainController {
    @FXML
    private Pane content;
    @FXML
    private Pane sideBar;
    @FXML
    private Slider speed;
    @FXML

    // rýchlost vykreslovania
    private int refresher = 55;
    private Data data;
    private List<Drawable> elements = new ArrayList<>();
    private List<Mover> movers = new ArrayList<>();
    private List<Shape> informations = new ArrayList<>();
    private List<Shape> path = new ArrayList<>();
    private List<OrderGenerator2> orderGenerators;
    private Drawable selected;
    //flag zapína a vypína zobrazovanie cestičiek na mape
    private boolean lines = false;
    private Timer timer;

    /**
     * Metoda slúži na zmenu rýchlosti času. Vyvolaná je nastavením slideru v pravom hornom rohu scény.
     * Maximalna rýchlosť vykreslovania je 100fps, najpomalšia je 9,1fps.
     */
    @FXML
    private void chengeTime() {
        timer.cancel();
        refresher = (110 - (int)(speed.getValue()));
        this.startTime(true);
    }

    /**
     * Metóda slúži na zoom hlavneho okna so skladom
     *
     * @param event - ScrollEvent
     */
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

    /**
     * Metóda zastavý aplikáciu, vykreslujú sa iba informácie, prípadne cestičky, rozkliknutého objektu.
     * Metóda je vyvolaná po stlačení tlačídla Pause v pravom hornom rohu scény.
     */
    @FXML
    private void pause() {
        timer.cancel();
        this.startTime(false);
    }

    /**
     * Metóda vyvolá reset nad všetkými objektami, tie sa nastavia opäť na inicializačné data.
     * Metóda je vyvolaná po stlačení tlačídla Reset v pravom hornom rohu scény.
     */
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

    /**
     * Metóda nastavý flag, ktorý zapína a vypína zobrazovanie cestičiek na mape
     * Metóda je vyvolaná po stlačení tlačídla Show lines v pravom hornom rohu scény.
     */
    @FXML
    private void showLines(){
        lines = !lines;
        if (!lines){
            content.getChildren().removeAll(path);
            path.clear();
        }
    }


    @FXML
    /**
     * Metóda púšta aplikáciu.
     * Metóda je vyvolaná po stlačení tlačídla Start v pravom hornom rohu scény.
     */
    private void start() {
        timer.cancel();
        this.startTime(true);
    }

    /**
     * Metóda volá spúšta všetky orderGenerátor2.
     */
    public void generateOrders(){
        for (OrderGenerator2 generator : orderGenerators){
            generator.generate();
        }
    }

    /**
     * Metóda vyvolá update všetkým objektom ktoré su implemntáciou Mover.
     */
    public void makeUpdates(){
        for (Mover mover : movers){
            mover.update();
        }
    }

    /**
     * Metóda aktualizuje info v bočnom panely, ak je nastavéné zobrazovanie cestičiek, zobrazí cestičku (iba v prípade, že je označený vozík).
     *
     */
    public void showInformation(){

        //zistí, ci nebolo kliknuté na iný objekt, ak ano nastavý ho ako selected...
        for (Drawable drawable : elements){
            if (drawable.getInfo().size() > 0){
                if (selected != null && selected != drawable){
                    selected.off();
                }
                selected = drawable;
                drawable.infoClear();
            }
        }

        //ak je rozkliknutý objekt, ziska aktualne info a vykreslí ho.
        if (selected != null) {
            informations.clear();
            informations.addAll(selected.updateInfo());
            selected.infoClear();
            sideBar.getChildren().clear();
            sideBar.getChildren().addAll(informations);

        }

        //vykreslovanie cesticiek, ak je aktivny flag lines
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

    /**
     * Metóda spúšta čas, podla ktualne nastavenej rýchlosti vykreslovania.
     *
     * @param run - flag určujúci, či je aplikácia pauznutá, alebo nie.
     */
    public void startTime(boolean run){
        if (run) {
            timer = new Timer(false);
            MainController controler = this;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        controler.showInformation();
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
                    Platform.runLater(() -> {
                        controler.showInformation();
                    });
                }
            }, 0, refresher);
        }
    }

    /**
     * Setter zoznam objektov z yml súboru.
     *
     * @param data - zoznam objektov z yml súboru.
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * Setter pre zoznam objektov, ktoré sú implementáciou Drawable, teda sa  vykreslujú.
     *
     * @param elements - zoznam Drawable objektov.
     */
    public void setElements(List<Drawable> elements) {
        this.elements = elements;
        for (Drawable drawable : elements){
            content.getChildren().addAll(drawable.getGUI());
            if (drawable instanceof Mover){
                movers.add((Mover) drawable);
            }
        }
    }

    /**
     * Setter pre zoznam OrderGenerátor2.
     *
     * @param orderGenerators - zoznam zoznam OrderGenerátor2
     */
    public void setOrderGenerators(List<OrderGenerator2> orderGenerators) {
        this.orderGenerators = orderGenerators;
    }


}
