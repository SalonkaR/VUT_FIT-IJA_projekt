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
 * Trieda DropPoint reprezentuje vydajne miesto, ktore ma zaroven pod zastitom vsetky objednavky ci uz cakajuce,
 * momentalne vykonavane alebo hotove. Voziky chodia na drop point a nechavaju tu uz hotove pozbierane objednavky.
 *
 * @author Jakub Sokolik - xsokol14
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class DropPoint implements Drawable{
    private String name;
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
    private ArrayList<Order> waiting = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> processing = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> done = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Order> capacityless = new ArrayList<>();
    private int maxToSee = 5;

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml
     */
    public DropPoint() {
    }

    /**
     * Funkcia vracia nazov drop pointu
     * @return nazov drop pointu
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia vracia poziciu drop pointu za pomoci triedy Coordinates
     * @return pocicia drop pointu
     */
    public Coordinates getPosition() {
        return position;
    }

    /**
     * Pomocna funkcia po stlaceni tlacidla reset vynuluje zoznamy s objednavkami a nanovo vytvori GUI
     */
    public void reset(){
        waiting =  new ArrayList<>();
        done = new ArrayList<>();
        processing = new ArrayList<>();
        capacityless = new ArrayList<>();
        this.makeGui();
    }

    /**
     * Konstruktor drop pointu, ktory nastavi jeho nazov a poziciu na platne
     * @param name Nazov drop pointu
     * @param position Pozicia drop pointu
     */
    @JsonIgnore
    public DropPoint(String name, Coordinates position) {
        this.name = name;
        this.position = position;
        this.makeGui();
    }

    /**
     * Funkcia vykresli drop point na platno vo forme stvoruholnika a zaobstarava kliknutie nan pre vypisanie prehladu objednavok
     */
    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX() - width/2, position.getY() - width/2, width, width);
        mainRect.setFill(ORANGE);
        gui.add(mainRect);

        DropPoint point = this;

        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == ORANGE) {
                    Text text = new Text(point.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    /**
     * Funkcia objednavku z argumentu prida do zoznamu cakajucich objednavok
     * @param order Objednavka zaradena na cakaciu listinu
     */
    @JsonIgnore
    public void addOrder(Order order){
        waiting.add(order);
    }

    /**
     * Funkcia zoznam objednavok z argumentu pripoji k zoznamu cakajucich objednavok
     * @param lst Zoznam objednavok, ktore budu pridane na cakaciu listinu
     */
    @JsonIgnore
    public void addOrder(ArrayList<Order> lst){
        waiting.addAll(lst);
    }

    /**
     * Funkcia vytvarajuca text, ktory obsahuje osobitne zoznamy objednavok podla ich statusu,
     * neskor sa tento text pouziva pri vypise po kliknuti na drop point
     * @return String s rozdelenim objednavok podla ich statusu do 4 celkov
     */
    @JsonIgnore
    private String getContent(){
        String str = "Waiting:\n";
        int cnt = 0;
        for (Order i : waiting) {
            str += i.getName() + "\n";
            if (cnt == maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nProcessing:\n";
        for (Order i : processing) {
            str += i.getName() + "\n";
            if (cnt == 2*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nDone:\n";
        for (Order i : done) {
            str += i.getName() + "\n";
            if (cnt == 3*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }

        str += "\nOut of storage capacity:\n";
        for (Order i : capacityless) {
            str += i.getName() + "\n";
            if (cnt == 4*maxToSee){
                str += "...";
                break;
            }
            cnt +=1;
        }
        return str;
    }

    /**
     * Funkcia zmeni status objednavky na zaklade argumentu status
     * @param status Nove cislo statusu, ktory bude objednavke prideleny
     * @param order Objednavka, ktorej bude zmeneny status
     */
    @JsonIgnore
    public void updateOrder(int status, Order order) {
        if (status == 0){
            waiting.remove(order);
            processing.add(0, order);
        }else{
            processing.remove(order);
            done.add(0, order);
            if (done.size() > 15){
                done.remove(done.size() - 1);
            }
        }
    }

    /**
     * Funkcia vracia objednavku ktora je prva na cakacej listine, ta je z cakacej listiny odstranena
     * @return Objednavka, ktora je odstranena z cakacej listiny
     */
    @JsonIgnore
    public Order getWaitingOrder(){
        Order order = waiting.get(0);
        if (order.checkCapacity()) {
            return order;
        } else {
            waiting.remove(0);
            capacityless.add(0, order);
            if (capacityless.size() > 15){
                capacityless.remove(capacityless.size() - 1);
            }
            return null;
        }
    }

    /**
     * Funkcia vracia velkost zoznamu s cakajucimi objednavkami
     * @return Pocet cakajucih objednavok
     */
    @JsonIgnore
    public int getWaitingOrderSize(){
        return waiting.size();
    }

    /**
     * Funkcia vracia GUI drop pointu
     * @return Zoznam shapov reprezentujuci GUI drop pointu
     */
    @Override
    public List<Shape> getGUI() {
        if(gui == null){
            this.makeGui();
        }
        return gui;
    }

    /**
     * Funkcia vracia objednavky priluchajuce drop pointu
     * @return zoznam shapov, ktory obsahuje text s objednavkami prisluchajucimi drop pointu
     */
    @Override
    public List<Shape> getInfo() {
        return info;
    }

    /**
     * Funkcia vracia aktualizovane objednavky prisluchajuce drop pointu
     * @return zoznam shapov, ktory obsahuje text s objednavkami prisluchajucimi drop pointu
     */
    @Override
    public List<Shape> updateInfo() {
        Text text = new Text(this.getContent());
        text.setWrappingWidth(125);
        info.add(text);
        return info;
    }

    /**
     * Pomocna funckia, ktora pri prekliknuti na iny objekt opat sfarbi policu do vychodzej farby
     */
    @Override
    public void off() {
        mainRect.setFill(ORANGE);
    }

    /**
     * Pomocna funkcia, ktora vycisti zoznam so shapami prisluchajuci objednavkam
     */
    @Override
    public void infoClear() {
        info.clear();
    }

}