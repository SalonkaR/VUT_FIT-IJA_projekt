package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static javafx.scene.paint.Color.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Carriage implements Drawable, Mover {
    private String name;
    private Parking parking;
    @JsonIgnore
    private Coordinates position;
    @JsonIgnore
    private int speed = 3;
    @JsonIgnore
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
    private double load;
    @JsonIgnore
    private double maxLoad = 100;
    @JsonIgnore
    boolean fromTop;
    @JsonIgnore
    boolean checkWay;

    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private HashMap<Goods, Integer> need;
    @JsonIgnore
    private HashMap<Goods, List<Item>> inside;
    @JsonIgnore
    private int cnt; 


    @JsonIgnore
    private HashMap<Double, List<Regal>> sortedRegals;
    @JsonIgnore
    private HashMap<Regal, List<Shelf>> regals;
    @JsonIgnore
    private HashMap<Shelf, List<Item>> shelves;
    @JsonIgnore
    private HashMap<Regal, Regal> regalReferencs;
    @JsonIgnore
    private Coordinates nextRegalPoint;
    @JsonIgnore
    private Coordinates nextShelfPoint;
    @JsonIgnore
    private Regal nextRegal;
    @JsonIgnore
    private Shelf nextShelf;


    //empty constructor for jackson(yml)
    public Carriage() {
    }

    public String getName() {
        return name;
    }

    public Parking getParking() {
        return parking;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    @JsonIgnore
    public Carriage(String name, Parking parking) {
        this.name = name;
        this.parking = parking;
        this.position = new Coordinates(parking.getPosition().getX(), parking.getPosition().getY());
        this.status = 0;
        parking.updateCarriage(this);
        makeGui();
    }
    @JsonIgnore
    public void setPosition(){
        this.position = new Coordinates(parking.getPosition().getX(), parking.getPosition().getY());
    }

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

    @JsonIgnore
    public String getNeed(){
        String str = "";
        for (Goods i : need.keySet()) {
            str += i.getName() + "-" + need.get(i) + "ks\n" ;
        }
        return str;
    }

    @JsonIgnore
    public String getInside(){
        String str = "";
        for (Goods i : inside.keySet()) {
            str += i.getName() + "-" + inside.get(i).size() + "ks\n" ;
        }
        return str;

    }
    @Override
    public List<Shape> getGUI() {
        if(gui == null){
            this.makeGui();
        }
        return gui;
    }

    @Override
    public List<Shape> getInfo() {
        return info;
    }

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

    @Override
    public List<Shape> updateInfo() {
        this.makeInfo();
        return info;
    }


    @Override
    public void off() {
        if (status == 9){
            mainCircle.setFill(BLACK);
        }else {
            mainCircle.setFill(GREEN);
        }
    }

    @Override
    public void infoClear() {
        info.clear();
    }

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

    @JsonIgnore
    public void checkPower (){
        if (power <= 0){
            status = 9;
            mainCircle.setFill(BLACK);
            parking.updateCarriage(this);
        }
    }

    @JsonIgnore
    public boolean checkWayPower(){

        double fromNextPoint = abs(nextShelfPoint.getX() - parking.getPosition().getX()) + abs(nextShelfPoint.getY() - parking.getPosition().getX());
        double toNextPoint = abs(nextShelfPoint.getX() - position.getX()) + abs(nextShelfPoint.getY() - position.getX());
        double min = (fromNextPoint + toNextPoint) / speed;

        if (power < min * 1.1) {
            nextShelfPoint = null;
            nextRegalPoint = null;
            status = 7;
            print("Dosla mi energia");
            return true;
        }
        return false;
    }

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

    @JsonIgnore
    public Coordinates getNextRegalPoint() {
        if (fromTop){
            return nextRegal.getTop();
        }
        return nextRegal.getBottom();
    }

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

    public void getBestWay(){
        Double topWay = abs(nextShelf.getAccessPoint().getY() - nextRegal.getTop().getY()) + abs(position.getY() - nextRegal.getTop().getY());
        Double bottomWay = abs(nextShelf.getAccessPoint().getY() - nextRegal.getBottom().getY()) + abs(position.getY() - nextRegal.getBottom().getY());
        if (topWay < bottomWay){
            nextRegalPoint = nextRegal.getTop();
            return;
        }
        nextRegalPoint = nextRegal.getBottom();

        Coordinates test = getNextRegalPoint();
        if (test.getY() != nextRegalPoint.getY()){
            
            checkWay = true;
        }
    }

    public List<Shelf> getFromRegals(Regal regal){
        if (regals.containsKey(regal)){
            return regals.get(regal);
        } else {
            return regals.get(regalReferencs.get(regal));
        }
    }
    
    public void print(String msg){
        System.out.println(cnt + ": " + msg);
        cnt += 1;
    }


    @Override
    public void update() {
        switch (status){
            case 0: //waiting for order
                order = parking.getOrder();
                inside = new HashMap<>();
                load = 0;

                if (order != null) {
                    cnt = 0;
                    print("Nova objednavka");
                    status += 1; // Go to processing order
                    parking.updateCarriage(this);
                    order.update();
                    this.calculateRoad();
                    fromTop = true;
                }
                break;
            case 1:
                // go to regal acces point
                if (nextRegalPoint == null){
                    print("Nastavil som si suradnice pre regal:");
                    nextRegal = this.getNextRegal();
                    nextRegalPoint = this.getNextRegalPoint();
                    nextShelf = this.getNextShelf();
                    print(nextRegalPoint.toString());
                    nextShelfPoint = nextShelf.getAccessPoint();
                    print("Nastavil som si suradnice pre policu:");
                    print(nextShelfPoint.toString());
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
                    print("idem do police");
                    status += 1;
                }
                this.checkPower();
                break;

            case 2:
                //move to shelf
                if (nextShelf == null){
                    nextShelf = getNextShelf();
                    nextShelfPoint = nextShelf.getAccessPoint();
                    print("nastavil som si suradnice police:");
                    print(nextShelfPoint.toString());
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
                                print("Dokoncil som objednavku");
                                status = 5;
                                nextShelfPoint = null;
                                nextShelfPoint = null;
                                break;
                            }
                            //Regal reg = this.getNextRegal();
                            //if (reg.getTop().getY() == nextRegal.getTop().getY()){
                                //musis vypocitat obchdzku, pretoze ides do oposit regalu
                                //print("dalsi regal je na rovnakej urovni, z ktorej strany ho obidem?");
                                print("idem do dalsieho regalu");
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
                            /*}
                            print("Idem do noveho stlpca");
                            nextRegalPoint = null;
                            status  = 1 ;
                            if (this.checkWayPower()){
                                break;
                            }
                            if (this.checkLoad(shelves.get(nextShelf).get(0))){
                                break;
                            }
                            break;*/
                        }
                        print("v tomto regali som hotovy");
                        print("idem do regala");
                        nextRegalPoint = null;
                        status = 1;
                        break;
                    }
                    print("v tejto polici som hotovy");
                    print("idem do police");
                    nextShelf = null;
                    status = 2;
                    break;
                }
                if (this.checkLoad(shelves.get(nextShelf).get(0))){
                    break;
                }

                break;

           /* case 4:
                // back to street
                if (nextRegalPoint == null){
                    nextRegalPoint = getNextRegalPoint();
                    print("nastavil som si suradnice regalu pre vratenie sa");
                    print(nextRegalPoint.toString());
                }
                diffY = nextRegalPoint.getY() - position.getY() ;
                diffX = nextRegalPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextRegalPoint = null;
                    if (sortedRegals.size() == 0){
                        status +=1;
                        print("objednavka je hotova");
                    } else {
                        print("idem do noveho regala");
                        status = 1;
                        if (checkWay){
                            if (this.getNextRegalPoint().getY() != position.getY()){
                                
                                fromTop = !fromTop;
                                nextRegalPoint = getNextRegalPoint();
                                fromTop = !fromTop;
                                checkWay = false;
                            }
                        }
                    }
                }
                this.checkPower();
                break;
*/
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
                    break;
                }

                need = null;
                inside = null;

                order.update();
                order = null;

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

    private boolean checkLoad(Item item) {
        if (load + item.getWeight() > maxLoad){
            status = 5;
            nextShelfPoint = null;
            print("Dosla mi miesto");
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Carriage{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                ", status=" + status +
                ", mainCircle=" + mainCircle +
                ", gui=" + gui +
                ", info=" + info +
                '}';
    }

    @JsonIgnore
    public int getStatus() {
        return status;
    }
}
