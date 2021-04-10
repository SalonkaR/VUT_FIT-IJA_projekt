package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

/**
 * Trieda Parking reprezentuje parkovisko, kde parkujú všetky vozíky. Tiež sa tam nabíjajú.
 *
 * @author Jakub Sokolík - xsokol14
 * @version 1.0
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Parking implements Drawable{
    private String name;
    private DropPoint dropPoint;
    private Coordinates position;
    @JsonIgnore
    private double width = 30;
    @JsonIgnore
    private Rectangle mainRect;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> parked = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> worked = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> powerless = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Carriage> charging = new ArrayList<>();

    /**
     * Prazdný konštruktor, ktorý slúži pre deserializáciu yml
     */
    public Parking() {
    }

    /**
     * Konštruktor vytvorí inštanciu parkoviska.
     *
     * @param name meno.
     * @param point DropPoint, s ktorým parkovisko komunikuje.
     * @param position pozícia.
     */
    public Parking(String name, DropPoint point, Coordinates position) {
        this.name = name;
        this.dropPoint = point;
        this.position = position;
        this.makeGui();
    }

    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré budú parkovisko reprezentovať na scéne.
     *
     * @return zoznam vykreslitelných objektov
     */
    @Override
    public List<Shape> getGUI() {
        if(gui == null){
            this.makeGui();
        }
        return gui;
    }

    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o parkovisku.
     *
     * @return zoznam vykreslitelných objektov
     */
    @Override
    public List<Shape> getInfo() {
        return info;
    }

    /**
     * Metóda vytvorí a vráti zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o parkovisku.
     *
     * @return zoznam vykreslitelných objektov
     */
    @Override
    public List<Shape> updateInfo() {
        Text text = new Text(this.getContent());
        text.setWrappingWidth(125);
        info.add(text);
        return info;
    }

    /**
     * Metóda nastaví parking na inicializačné data.
     */
    public void reset(){
        parked = new ArrayList<>();
        worked = new ArrayList<>();
        powerless = new ArrayList<>();
        charging = new ArrayList<>();
        this.makeGui();
    }

    /**
     * Metóda zruší označenie parkoviska. Volaná je MainControllerom, potom ako bolo kliknuté na iný objekt.
     */
    @Override
    public void off() {
        mainRect.setFill(ORANGE);
    }

    /**
     * Metodá vyčistí zoznam informácii. Volaná je MainControlerrom, po tom, ako vykreslil aktualne informácie.
     */
    @Override
    public void infoClear() {
        info.clear();
    }

    /**
     * Metóda vytvorí zoznam vykreslitelných objektov, ktoré reprezentujú parkovisko na mape skladu.
     *
     */
    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX() - width/2, position.getY() - width/2, width, width);
        mainRect.setFill(ORANGE);
        gui.add(mainRect);

        Parking parking = this;

        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == ORANGE) {
                    Text text = new Text(parking.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    /**
     * Po zmene statusu vozík volá túto metodu, aby malo parkovisko prehlaď o všetkych vozíkoch a mohlo vytvoriť informácie do bočného panela.
     *
     * @param carriage vozík, ktorý zmenil svoj status.
     */
    public void updateCarriage(Carriage carriage){
        if (worked.contains(carriage)){
            worked.remove(carriage);
        } else if (parked.contains(carriage)){
            parked.remove(carriage);
        } else if (charging.contains(carriage)){
            charging.remove(carriage);
        }

        switch(carriage.getStatus()){
            case 0:
                parked.add(carriage);
                break;
            case 8:
                charging.add(carriage);
                break;
            case 9:
                powerless.add(carriage);
                break;
            default:
                worked.add(carriage);
        }
    }

    /**
     * Metóda vytvorí a vráti retazec, ktorý reprezentuje informacie o parovisku.
     *
     * @return retazec, ktorý reprezentuje informacie o parkovisku.
     */
    private String getContent(){
        String str = "Parked:\n";
        for (Carriage i : parked) {
            str += "   " + i.getName() + "\n";
        }
        str += "\nWorked:\n";
        for (Carriage i : worked) {
            str += "   " + i.getName() + "\n";
        }
        str += "\nCharging:\n";
        for (Carriage i : charging) {
            str += "   " + i.getName() + "\n";
        }
        str += "\nPowerless:\n";
        for (Carriage i : powerless) {
            str += "   " + i.getName() + "\n";
        }
        return str;
    }


    /**
     * Metóda vráti súradnice DropPointu. Táto metoda je volaná vozíkom, keď ide vyložiť naložený tovar.
     *
     * @return
     */
    public Coordinates getDropPointCoords(){
        return dropPoint.getPosition();
    }

    /**
     * Metóda vracia meno parkoviska.
     *
     * @return meno parkoviska.
     */
    public String getName() {
        return name;
    }


    /**
     * Metóda vráti objednávku, ktorá čaká na spracovanie. Ak taká je.
     * @return objednávku, ktorá čaká na spracovanie, inak null.
     */
    public Order getOrder(){
        if (dropPoint.getWaitingOrderSize() == 0){
            return null;
        }
        return dropPoint.getWaitingOrder();
    }

    /**
     * Metóda vráti pozíciu parkoviska.
     * @return pozíciu parkoviska.
     */
    public Coordinates getPosition() {
        return position;
    }

}
