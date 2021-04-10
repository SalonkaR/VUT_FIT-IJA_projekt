package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static javafx.scene.paint.Color.*;

/**
 * Trieda Carriage reprezentuje vozík. Jeho východzia pozícia je na Parking.
 * Vždy si zoberie jednu objednávku, tu vyzdvzhne zo skladu a zanesie do DropPointu
 *
 * @author Jakub Sokolík - xsokol14
 * @version 1.0
 * @see Parking
 * @see DropPoint
 * @see Order
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Carriage implements Drawable, Mover {
    @JsonIgnore
    // flag určuje smer zbierania /zhora alebo z dola        
    boolean fromTop;
    private String name;
    private Parking parking;
    @JsonIgnore
    private Coordinates position;
    @JsonIgnore
    private int speed = 4;
    @JsonIgnore
    // status určuje čo v akom stádiu sa nachadza vozik
    private int status;
    @JsonIgnore
    private Circle mainCircle;
    @JsonIgnore
    private Order order;
    @JsonIgnore
    private int power = 6000;
    @JsonIgnore
    private int maxPower = 6000;
    @JsonIgnore
    // naložená hmotnosť
    private double load;
    @JsonIgnore
    private double maxLoad = 50;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    //tovar, ktorý ešte musí vyzdvyhnuť
    private HashMap<Goods, Integer> need;
    @JsonIgnore
    //aktualne naložený tovar
    private HashMap<Goods, List<Item>> inside;

    @JsonIgnore
    // <Xposition regal, List<Regals>>
    private HashMap<Double, List<Regal>> sortedRegals;
    @JsonIgnore
    private HashMap<Regal, List<Shelf>> regals;
    @JsonIgnore
    private HashMap<Shelf, List<Item>> shelves;
    @JsonIgnore
    private HashMap<Regal, Regal> regalReferencs;
    @JsonIgnore
    //zoznam lines, ktorý znazornuje cesticku
    private List<Line> path = new ArrayList<>();
    @JsonIgnore
    private Coordinates nextRegalPoint;
    @JsonIgnore
    private Coordinates nextShelfPoint;
    @JsonIgnore
    private Regal nextRegal;
    @JsonIgnore
    private Shelf nextShelf;
    @JsonIgnore
    // flag urcuje, kde zaciana vozik cesticku true -> parking false -: droppoint
    private boolean pathStart;


    /**
     * Prazdný konštruktor, ktorý slúži pre deserializáciu yml
     */
    public Carriage() {
    }

    /**
     * Konštruktor nastavuje meno vozíka a predáva mu referenciu na parkovisko, ku ktoremu patrí.
     * @param name meno
     * @param parking parkovisko, ktoré je pre vozík vychodziou pozíciou
     */
    @JsonIgnore
    public Carriage(String name, Parking parking) {
        this.name = name;
        this.parking = parking;
        this.position = new Coordinates(parking.getPosition().getX(), parking.getPosition().getY());
        this.status = 0;
        parking.updateCarriage(this);
        makeGui();
    }

    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré budú vozík reprezentovať na scéne.
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
     * Metóda vracia zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o vozíku.
     *
     * @return zoznam vykreslitelných objektov
     */
    @Override
    public List<Shape> getInfo() {
        return info;
    }

    /**
     * Metóda vytvorí a vráti zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o vozíku.
     *
     * @return zoznam vykreslitelných objektov
     */
    @Override
    public List<Shape> updateInfo() {
        this.makeInfo();
        return info;
    }

    /**
     * Metóda nastaví vozík na inicializačné data. Polohu nastaví na priradené Parking.
     */
    public void reset(){
        power = maxPower;
        load = 0;
        need = null;
        status = 0;
        position = new Coordinates(parking.getPosition().getX(), parking.getPosition().getY());
        inside = null;
        sortedRegals = null;
        regals = null;
        shelves = null;
        regalReferencs = null;
        nextRegal = null;
        nextRegalPoint = null;
        nextShelf = null;
        nextShelfPoint = null;
        parking.updateCarriage(this);
        this.makeGui();

    }

    /**
     * Metóda zruší označenie vozíku. Volaná je MainControllerom, potom ako bolo kliknuté na iný objekt.
     */
    @Override
    public void off() {
        if (status == 9){
            mainCircle.setFill(BLACK);
        }else {
            mainCircle.setFill(GREEN);
        }
    }

    /**
     * Metodá vyčistí zoznam informácii. Volaná je MainControlerrom, po tom, ako vykreslil aktualne informácie.
     */
    @Override
    public void infoClear() {
        info.clear();
    }


    /**
     * Metóda riadi správanie vozíka podľa aktualneho statusu.<br>
     * Statusy:<br>
     * 
     *     - Čakanie na dalšiu objednávku<br>
     *     - Pohyb smerom k regalu<br>
     *     - Pohyb k polici<br>
     *     - Nakladanie tovaru. Rýchlosť 1 tovar, za jedno vykreslenie<br>
     *     - Pohyb k vykladaciemu miestu - DropPointu<br>
     *     - Vyloženie všetkého tovaru (naraz)<br>
     *     - Pohyb k parkovaciemu miestu - Parking<br>
     *     - Nabíjanie<br>
     *     - Vybytý vozík<br>
     *
     * 
     */
    @Override
    public void update() {

        switch (status){
            case 0: //waiting for order
                order = parking.getOrder();
                if (order != null) {
                    load = 0;
                    pathStart = true;
                    inside = new HashMap<>();
                    status += 1; // Go to processing order
                    parking.updateCarriage(this);
                    order.update();
                    this.calculateRoad();
                    this.makePath();
                    fromTop = true;
                }
                break;
            case 1:
                // go to regal acces point
                if (nextRegalPoint == null){

                    
                    nextRegal = this.getNextRegal();
                    nextRegalPoint = this.getNextRegalPoint();
                    nextShelf = this.getNextShelf();

                    
                    nextShelfPoint = nextShelf.getAccessPoint();

                    if (this.checkWayPower()){
                        break;
                    }
                    if (this.checkLoad(shelves.get(nextShelf).get(0))){
                        break;
                    }
                }
                double diffY = nextRegalPoint.getY() - position.getY() ;
                double diffX = nextRegalPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0){
                    
                    status += 1;
                }
                this.checkPower();
                break;

            case 2:
                //move to shelf
                if (nextShelf == null){
                    nextShelf = getNextShelf();
                    nextShelfPoint = nextShelf.getAccessPoint();
                    
                    
                    if (this.checkWayPower()){
                        break;
                    }
                    if (this.checkLoad(shelves.get(nextShelf).get(0))){
                        break;
                    }
                }

                diffY = nextShelfPoint.getY() - position.getY() ;
                diffX = nextShelfPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    status += 1;
                }
                this.checkPower();
                break;

            case 3:
                //load item from shelf

                this.loadItem(nextShelf, shelves.get(nextShelf).get(0));
                shelves.get(nextShelf).remove(0);

                if (shelves.get(nextShelf).size() == 0){
                    //vybrate vsetky itemy z police
                    shelves.remove(nextShelf);
                    this.getFromRegals(nextRegal).remove(nextShelf);
                    if (this.getFromRegals(nextRegal).size() == 0){
                        //vybral si itemy zo vsetkych polic v regali
                        if (regals.containsKey(nextRegal)) {
                            regals.remove(nextRegal);
                        } else {
                            regals.remove(regalReferencs.get(nextRegal));
                        }
                        sortedRegals.get(nextRegal.getTop().getX()).remove(nextRegal);
                        if (sortedRegals.get(nextRegal.getTop().getX()).size() == 0){
                            //vybral si vsetky regale v stlpci
                            fromTop = !fromTop;
                            sortedRegals.remove(nextRegal.getTop().getX());
                            if (sortedRegals.size() == 0){
                                //order is ready
                                
                                status = 5;
                                nextShelfPoint = null;
                                nextShelfPoint = null;
                                break;
                            }
                            //Regal reg = this.getNextRegal();
                            //if (reg.getTop().getY() == nextRegal.getTop().getY()){
                                //musis vypocitat obchdzku, pretoze ides do oposit regalu
                                //
                                //nextRegal = reg;
                                nextRegal = getNextRegal();//n
                                nextShelf = getNextShelf();
                                nextShelfPoint = nextShelf.getAccessPoint();
                                this.getBestWay();

                                status = 1;

                                if (this.checkWayPower()){
                                    break;
                                }
                                if (this.checkLoad(shelves.get(nextShelf).get(0))){
                                    break;
                                }
                                break;
                        }
                        nextRegalPoint = null;
                        status = 1;
                        break;
                    }
                    nextShelf = null;
                    status = 2;
                    break;
                }
                if (this.checkLoad(shelves.get(nextShelf).get(0))){
                    break;
                }

                break;
            case 5:
                // Go to Drop point
                if (nextShelfPoint == null){

                    nextShelfPoint = parking.getDropPointCoords();
                    if (this.checkWayPower()){
                        break;
                    }
                }

                diffY = nextShelfPoint.getY() - position.getY() ;
                diffX = nextShelfPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextShelfPoint = null;
                    nextRegalPoint = null;
                    fromTop = true;
                    status += 1;
                }
                this.checkPower();
                break;
            case 6:
                //selling items
                for (Goods goods : inside.keySet()){
                    for (Item item : inside.get(goods)){
                        item.sell();
                    }
                }
                load = 0;
                if (sortedRegals.size() > 0){
                    status = 1;
                    inside = new HashMap<>();
                    makePath();
                    break;
                }

                need = null;
                inside = null;

                order.update();
                order = null;
                path.clear();
                path.add(new Line(position.getX(), position.getY(), position.getX(), parking.getPosition().getY()));
                path.add(new Line(position.getX(), parking.getPosition().getY(), parking.getPosition().getX(), parking.getPosition().getY()));
                status += 1;
                break;
            case 7:
                //go to parking
                if (nextShelfPoint == null) {

                    nextShelfPoint = parking.getPosition();
                }

                diffY = nextShelfPoint.getY() - position.getY() ;
                diffX = nextShelfPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextShelfPoint = null;
                    status += 1;
                    fromTop = true;
                    nextRegal = null;
                    parking.updateCarriage(this);
                }
                this.checkPower();
                break;
            case 8:
                makePath();
                //Caharging

                if (power < maxPower){
                    power += min(5, maxPower-power);
                } else {

                    if (order == null){
                        status = 0;
                    } else {
                        if (sortedRegals.size() == 0){
                            status = 5;
                        } else {
                            status = 1;
                        }
                    }
                    parking.updateCarriage(this);
                }
                break;
            case 9:
                //powerOff
                break;
        }

    }

    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré reprezentujú aktualnu cestu vozíka.
     *
     * @return zoznam vykreslitelných objektov, ktoré reprezentujú aktualnu cestu vozíka.
     */
    public List<Line> getPath(){
        return path;
    }

    /**
     * Metóda pridá regál do mapy regalov, podľa jeho x pozície.
     *
     * @param regal regal, ktorý sa pridavá.
     */
    @JsonIgnore
    public void addRegal(Regal regal){
        if (sortedRegals.containsKey(regal.getTop().getX())){
            sortedRegals.get(regal.getTop().getX()).add(regal);
        } else {
            List<Regal> lst = new ArrayList<>();
            lst.add(regal);
            sortedRegals.put(regal.getTop().getX(), lst);
        }
    }

    /**
     * Metóda pridá policu do mapy políc, podľa toho, do akeho regalu patrí.
     *
     * @param shelf polica, ktorá sa pridáva.
     */
    @JsonIgnore
    public void addShelf(Shelf shelf){
        if (regals.containsKey(shelf.getRegal())){
            regals.get(shelf.getRegal()).add(shelf);
        } else {
            List<Shelf> lst = new ArrayList<>();
            lst.add(shelf);
            regals.put(shelf.getRegal(), lst);
            this.addRegal(shelf.getRegal());
        }
    }

    /**
     * Metóda si vyžiada od aktuálneho Orderu, zoznam itemov, ktoré ma zo skladu vytiahnuť. Následne pre tieto itemy vytvorí potrebné Mapy.
     *
     */
    @JsonIgnore
    public void calculateRoad(){
        sortedRegals = new HashMap<>();
        regals = new HashMap<>();
        shelves = new HashMap<>();
        need = order.getList();
        List<Item> items = new ArrayList<>();

        for (Goods goods : need.keySet()){
            items.addAll(goods.getItems(need.get(goods)));
        }

        this.makeLists(items);
        regalReferencs = new HashMap<>();


        this.mergeRegals();


    }

    /**
     * Metóda kontroluje, či daný item je možne naložiť, vzhľadom na kapacitu vozíka.
     *
     * @param item item ktorý chceme naložiť.
     * @return true ak sa objekt nedá naložit, false ak sa dá.
     */
    private boolean checkLoad(Item item) {
        if (load + item.getWeight() > maxLoad){
            status = 5;
            nextShelfPoint = null;
            pathStart = false;
            path.clear();
            path.add(new Line(position.getX(), position.getY(), position.getX(), parking.getDropPointCoords().getY()));
            path.add(new Line(position.getX(), parking.getDropPointCoords().getY(), parking.getDropPointCoords().getX(), parking.getDropPointCoords().getY()));
            return true;
        }
        return false;
    }

    /**
     * Metóda kontroluje, či baterka vozíka neklesla pod 0, ak ano, vozík sa vybije.
     */
    @JsonIgnore
    public void checkPower (){
        if (power <= 0){
            status = 9;
            mainCircle.setFill(BLACK);
            parking.updateCarriage(this);
        }
    }

    /**
     * Metóda kontroluje, či aktualna bateria postačí na vyzdvihnutie dalšieho itemu.
     *
     * @return true, ak baterka nepostačuje inak false.
     */
    @JsonIgnore
    public boolean checkWayPower(){

        double fromNextPoint = abs(nextShelfPoint.getX() - parking.getPosition().getX()) + abs(nextShelfPoint.getY() - parking.getPosition().getX());
        double toNextPoint = abs(nextShelfPoint.getX() - position.getX()) + abs(nextShelfPoint.getY() - position.getX());
        double min = (fromNextPoint + toNextPoint) / speed;

        if (power < min * 1.1) {
            nextShelfPoint = null;
            nextRegalPoint = null;
            status = 7;
            pathStart = true;
            path.clear();
            path.add(new Line(position.getX(), position.getY(), position.getX(), parking.getPosition().getY()));
            path.add(new Line(position.getX(), parking.getPosition().getY(), parking.getPosition().getX(), parking.getPosition().getY()));
            return true;
        }
        return false;
    }

    /**
     * Metóda vypočíta kratšiu cestu medzi regalmi. Uvažuje sa o ceste zo spodku alebo z vrchu.
     */
    public void getBestWay(){
        Double topWay = abs(nextShelf.getAccessPoint().getY() - nextRegal.getTop().getY()) + abs(position.getY() - nextRegal.getTop().getY());
        Double bottomWay = abs(nextShelf.getAccessPoint().getY() - nextRegal.getBottom().getY()) + abs(position.getY() - nextRegal.getBottom().getY());
        if (topWay < bottomWay){
            nextRegalPoint = nextRegal.getTop();
            return;
        }
        nextRegalPoint = nextRegal.getBottom();
    }

    /**
     * Metóda vracia zoznam políc, ktoré trreba navštíviť v nasledujúcom regáli.
     *
     * @param regal regal v ktorom sa vozík nachadza
     * @return zoznam políc, ktoré trreba navštíviť v nasledujúcom regáli.
     */
    public List<Shelf> getFromRegals(Regal regal){
        if (regals.containsKey(regal)){
            return regals.get(regal);
        } else {
            return regals.get(regalReferencs.get(regal));
        }
    }

    /**
     * Metóda naloží item do vozíka.
     *
     * @param shelf  polica z ktorej nakladáme.
     * @param item konkretný item, ktorý nakladme.
     */
    @JsonIgnore
    public void loadItem(Shelf shelf, Item item) {
        int pcs = need.get(item.getGoods()) - 1;
        if (pcs == 0){
            need.remove(item.getGoods());
        } else {
            need.put(item.getGoods(), pcs);
        }

        if (inside.containsKey(item.getGoods())){
            inside.get(item.getGoods()).add(item);
        } else {
            List<Item> lst = new ArrayList<>();
            lst.add(item);
            inside.put(item.getGoods(), lst);
        }

        this.load += item.getWeight();
        shelf.removeItem(item);

    }

    /**
     * Metóda vytvorí zoznam vykreslitelných objektov, ktoré reprezentujú vozík.
     *
     */
    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        mainCircle = new Circle(position.getX(), position.getY(), 6, GREEN);
        gui.add(mainCircle);

        Carriage carriage = this;

        mainCircle.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {

                carriage.makeInfo();
                mainCircle.setFill(RED);

            }
        });
    }

    /**
     * Metóda  vytvorí zoznam vykreslitelných objektov, ktorý reprezentuje údaje o vozíku.
     */
    @JsonIgnore
    public void makeInfo(){
        Text text = new Text(this.getContent());
        text.setWrappingWidth(125);

        //battery
        double powerCoef = ((double)power / maxPower);
        Rectangle powerRec = new Rectangle(0,4, 150 * powerCoef , 10);
        int red = 255;
        int green = 255;

        if (powerCoef < 0.5) {
            green = (int) (510*powerCoef);
        } else {
            red = (int)(510*(1 - powerCoef));
        }

        int blue = 0;
        powerRec.setFill(Color.rgb(red,green,blue));

        //load
        double loadCoef = ((double)load / maxLoad);
        Rectangle loadRec = new Rectangle(0,84, 150 * loadCoef , 10);
        red = 255;
        green = 255;

        if (loadCoef < 0.5) {
            red = (int) (510*loadCoef);
        } else {
            green = (int)(510*(1 - loadCoef));
        }

        blue = 0;
        loadRec.setFill(Color.rgb(red,green,blue));

        //load

        info.add(powerRec);
        info.add(loadRec);
        info.add(text);

    }

    /**
     * Metóda roztriedi itemy do mapy, podla toho, do ktorej police patria.
     *
     * @param items zoznam itemov
     */
    @JsonIgnore
    public void makeLists(List<Item> items){
        Shelf shelf;
        for (Item item : items) {
            shelf = item.getShelf();
            if (shelves.containsKey(shelf)){
                shelves.get(shelf).add(item);
            } else {
                List<Item> lst = new ArrayList<>();
                lst.add(item);
                shelves.put(shelf, lst);
                this.addShelf(shelf);
            }
        }
    }

    /**
     * Metóda vytvorí zoznam vykreslitelných objektov, ktorý reprezentuje cesticku vozíka.
     */
    public void makePath(){
        path.clear();
        if (order == null){
            return;
        }
        List<Coordinates> coordPath= new ArrayList<>();
        coordPath.add(new Coordinates(position.getX(), position.getY()));


        if (pathStart){
            //zaciname z parking
            coordPath.add(parking.getPosition());
        } else {
            //zaciname z dropointu
            coordPath.add(parking.getDropPointCoords());
        }
        List<Double> sortedX = new ArrayList<>();
        for (Double x : sortedRegals.keySet()){
            sortedX.add(x);
        }

        Collections.sort(sortedX);
        Map<Double, Regal> nextRegal = new HashMap<>();
        Map<Double, Shelf> nextShelves = new HashMap<>();
        List<Double> sortedYRegal = new ArrayList<>();
        List<Double> sortedYShelf = new ArrayList<>();
        Coordinates nextShelf;
        boolean fromTop = true;
        Regal lastRegal;
        Coordinates lastShelf = null;

        for (Double X : sortedX) {
            nextRegal.clear();
            nextShelves.clear();
            sortedYShelf.clear();
            sortedYRegal.clear();


            //pridanie acess pointu regalu
            for (Regal regal : sortedRegals.get(X)) {
                nextRegal.put(regal.getTop().getY(), regal);
                sortedYRegal.add(regal.getTop().getY());
            }

            Collections.sort(sortedYRegal);

            if (lastShelf == null) {

                coordPath.add(nextRegal.get(sortedYRegal.get(0)).getTop());

            }else{
                //calculate best way
                //calculate first shelf
                if (!fromTop){
                    Collections.reverse(sortedYRegal);
                }
                lastRegal = nextRegal.get(sortedYRegal.get(0));

                for (Shelf shelf : regals.get(lastRegal)) {
                    nextShelves.put(shelf.getAccessPoint().getY(), shelf);
                    sortedYShelf.add(shelf.getAccessPoint().getY());

                }

                Collections.sort(sortedYShelf);

                if (fromTop) {
                    nextShelf = (nextShelves.get(sortedYShelf.get(0)).getAccessPoint());
                } else {
                    Collections.reverse(sortedYShelf);
                    nextShelf = (nextShelves.get(sortedYShelf.get(0)).getAccessPoint());
                }


                Double topWay = abs(nextShelf.getY() - lastRegal.getTop().getY()) + abs(lastShelf.getY() - lastRegal.getTop().getY());
                Double bottomWay = abs(nextShelf.getY() - lastRegal.getBottom().getY()) + abs(lastShelf.getY() - lastRegal.getBottom().getY());

                //pridanie vchodu do regala
                if (topWay < bottomWay){
                    coordPath.add(lastRegal.getTop());

                } else {
                    coordPath.add(lastRegal.getBottom());
                }
                //pridanie 1. police
                coordPath.add(nextShelf);
            }

            //pridanie poslednej shelf
            Collections.reverse(sortedYRegal);
            lastRegal = nextRegal.get(sortedYRegal.get(0));

            for (Shelf shelf : regals.get(lastRegal)) {
                nextShelves.put(shelf.getAccessPoint().getY(), shelf);
                sortedYShelf.add(shelf.getAccessPoint().getY());

            }

            Collections.sort(sortedYShelf);

            if (!fromTop) {

                lastShelf = nextShelves.get(sortedYShelf.get(0)).getAccessPoint();
            } else {
                Collections.reverse(sortedYShelf);
                lastShelf = nextShelves.get(sortedYShelf.get(0)).getAccessPoint();
            }
            coordPath.add(lastShelf);

            fromTop = !fromTop;
        }
        coordPath.add(parking.getDropPointCoords());


        // vytvorenie lines

        Coordinates previous = null;
        for (Coordinates cords : coordPath){
            if (previous == null){
                previous = cords;
                continue;
            }

            path.add(new Line(previous.getX(), previous.getY(), previous.getX(), cords.getY()));
            path.add(new Line(previous.getX(), cords.getY(), cords.getX(), cords.getY()));
            previous = cords;

        }
    }

    /**
     * Metóda spoji regále, ktoré majú rovnaké prístupové body.
     */
    public void mergeRegals(){
        Map <Double, Regal> tmp;
        List<Regal> toRemove = new ArrayList<>();

        for (Double x :sortedRegals.keySet()){
            tmp = new HashMap<>();
            for (Regal regal : sortedRegals.get(x)){
                if (tmp.containsKey(regal.getTop().getY())){
                    //merge
                    regals.get(tmp.get(regal.getTop().getY())).addAll(regals.get(regal));
                    regalReferencs.put(regal,tmp.get(regal.getTop().getY()));
                    regals.remove(regal);
                    toRemove.add(regal);
                } else {
                    tmp.put(regal.getTop().getY(), regal);
                }
            }
            for (Regal regal2 : toRemove){
                sortedRegals.get(x).remove(regal2);

            }
        }
    }

    /**
     * Metóda rieši pohyb vozíka.
     *
     * @param diffX posunutie na x osi.
     * @param diffY posunutie na y osi.
     */
    @JsonIgnore
    public void move(double diffX, double diffY){
        power -= 1;
        boolean direction;
        double diff;
        if (diffY == 0){
            direction = false;
            diff = diffX;
        } else {
            diff = diffY;
            direction = true;
        }


        if (diff > 0){
            diff = Double.min(diff, speed);
        } else {
            diff = -Double.min(-diff, speed);
        }


        if (direction) {   //true = y
            //axis y
            mainCircle.setTranslateY(mainCircle.getTranslateY() + diff);
            position.setY(position.getY() + diff);
        } else {
            //axis x
            mainCircle.setTranslateX(mainCircle.getTranslateX() + diff);
            position.setX(position.getX() + diff);
        }
    }

    /**
     * Metóda pošle status vozíka Parkingu, aby mal prehľad o vozíkoch.
     *
     */
    public void sendStatus(){
        parking.updateCarriage(this);
    }

    /**
     * Metóda nastaví pozíciu vozíka.
     */
    @JsonIgnore
    public void setPosition(){
        this.position = new Coordinates(parking.getPosition().getX(), parking.getPosition().getY());
    }

    /**
     * Metóda vytvorí a vráti retazec, ktorý reprezentuje informacie o vozíku.
     *
     * @return retazec, ktorý reprezentuje informacie o vozíku.
     */
    @JsonIgnore
    public String getContent(){

        double scale = Math.pow(10, 2);
        double rLoad = Math.round(load * scale) / scale;

        if (order != null){
            switch(status){
                case 7:
                    return name + "\n\nI need more power\nOrder: " + order.getName() + "\n\nLoad:\n" + rLoad + "kg\n\n" + this.getInside() + "\nNeed:\n" + this.getNeed();
                case 8:
                    return name + "\n\nCharging\nOrder: " + order.getName() + "\n\nLoad:\n" + rLoad + "kg\n\n" + this.getInside() + "\nNeed:\n" + this.getNeed();
                case 9:
                    return name + "\n\nI'm powerless\nOrder: " + order.getName() + "\n\nLoad:\n" + rLoad + "kg\n\n" + this.getInside() + "\nNeed:\n" + this.getNeed();
                default:
                    return name + "\n\nOrder: " + order.getName() + "\n\nLoad:\n" + rLoad + "kg\n\n"+ this.getInside() + "\nNeed:\n" + this.getNeed();
            }
        }
        switch(status){
            case 7:
                return name + "\n\nSearching for some parking place";
            case 8:
                return name + "\n\nCharging";
            case 9:
                return name + "\n\n I'm powerless";
        }
        return name + "\n\n I'm idle";

    }

    /**
     * Metóda vytvorí a vráti rezatec, ktorý reprezentuje naložený tovar
     *
     * @return rezatec reprezentujúci naložený tovar.
     */
    @JsonIgnore
    public String getInside(){
        String str = "";
        for (Goods i : inside.keySet()) {
            str += i.getName() + "-" + inside.get(i).size() + "ks\n" ;
        }
        return str;

    }

    /**
     * Metóda vráti meno vozíka.
     *
     * @return meno vozíka.
     */
    public String getName() {
        return name;
    }

    /**
     * Metóda vytvorí a vráti rezatec, ktorý reprezentuje tovar, ktotý treba naložiať.
     *
     * @return rezatecreprezentujúci naložený tovar.
     */
    @JsonIgnore
    public String getNeed(){
        String str = "";
        for (Goods i : need.keySet()) {
            str += i.getName() + "-" + need.get(i) + "ks\n" ;
        }
        return str;
    }

    /**
     * Metóda vráti regál, do ktorého pôjde vozík najbližsie.
     *
     * @return regál, do ktorého pôjde vozík najbližsie.
     */
    @JsonIgnore
    public Regal getNextRegal(){
        List<Double> xs = new ArrayList<>();
        xs.addAll(sortedRegals.keySet());
        Collections.sort(xs);

        Map<Double, Regal> nextRegals = new HashMap<>();
        List<Double> lst = new ArrayList<>();

        for (Regal regal : sortedRegals.get(xs.get(0))){
            nextRegals.put(regal.getTop().getY(),regal);

        }


        lst.addAll(nextRegals.keySet());

        if (fromTop) {
            Collections.sort(lst);
        } else {
            Collections.sort(lst);
            Collections.reverse(lst);
        }


        return nextRegals.get(lst.get(0));
    }

    /**
     * Metóda vráti súradnice regálu, do ktorého pôjde vozík najbližsie.
     * @return súradnice regálu, do ktorého pôjde vozík najbližsie.
     */
    @JsonIgnore
    public Coordinates getNextRegalPoint() {
        if (fromTop){
            return nextRegal.getTop();
        }
        return nextRegal.getBottom();
    }

    /**
     * Metóda vráti policu, do ktoréj pôjde vozík najbližsie.
     * @return policu, do ktoréj pôjde vozík najbližsie.
     */
    public Shelf getNextShelf(){
        Map<Double, Shelf> nextShelves = new HashMap<>();


        for (Shelf shelf : this.getFromRegals(nextRegal)) {
            nextShelves.put(shelf.getAccessPoint().getY(), shelf);
        }


        List<Double> lst = new ArrayList<>();
        lst.addAll(nextShelves.keySet());

        if (fromTop) {
            Collections.sort(lst);
        } else {
            Collections.sort(lst);
            Collections.reverse(lst);
        }

        return nextShelves.get(lst.get(0));

    }

    /**
     * Metóda vráti status vozíka.
     * @return status vozíka.
     */
    @JsonIgnore
    public int getStatus() {
        return status;
    }

    /**
     * Metóda vráti priradené parkovisko.
     *
     * @return priradené parkovisko.
     */
    public Parking getParking() {
        return parking;
    }
}
