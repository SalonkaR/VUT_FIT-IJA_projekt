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
    private double maxLoad = 10;


    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private HashMap<Goods, Integer> need;
    @JsonIgnore
    private HashMap<Goods, List<Item>> inside;

    @JsonIgnore
    private HashMap<Regal, HashMap<Shelf, List<Item>>> sortedListItems;
    @JsonIgnore
    private Coordinates nextPoint;
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
                if(mainCircle.getFill() == GREEN) {
                    carriage.makeInfo();
                    mainCircle.setFill(RED);
                }
            }
        });
    }

    @JsonIgnore
    public void calculateRoad(){
        need = order.getList();
        sortedListItems = new HashMap<>();

        HashMap<Double, List<Item>> listItems = new HashMap<>();
        HashMap<Double, List<Item>> newListItems;

        for (Goods goods : need.keySet()){
            newListItems = goods.getItems(need.get(goods));
            for (double key : newListItems.keySet()){
                if (listItems.containsKey(key)){
                    for (Item item : newListItems.get(key)){
                        listItems.get(key).add(item);
                    }
                } else {
                    listItems.put(key, newListItems.get(key));
                }
            }
        }

        HashMap<Shelf, List<Item>> listItems2 = new HashMap<>();
        //zoznam (Shelf, list(Item))
        for (double key : listItems.keySet()){
            for (Item item : listItems.get(key)){
                if (listItems2.containsKey(item.getShelf())){
                    listItems2.get(item.getShelf()).add(item);
                } else {
                    List<Item> lst = new ArrayList<>();
                    lst.add(item);
                    listItems2.put(item.getShelf(), lst);
                }
            }
        }

        //zoznam ((Regal, (Shelf, list(Item)))

        for(Shelf shelf : listItems2.keySet()){
            if (sortedListItems.containsKey(shelf.getRegal())){
                sortedListItems.get(shelf.getRegal()).put(shelf, listItems2.get(shelf));
            } else {
                HashMap<Shelf, List<Item>> lst = new HashMap<>();
                lst.put(shelf, listItems2.get(shelf));
                sortedListItems.put(shelf.getRegal(), lst);
            }
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
    public Regal getNextRegal(){
        List<Regal> regLst = new ArrayList<>();
        regLst.addAll(sortedListItems.keySet());

        List<Double> cooLst = new ArrayList<>();
        HashMap<Double, List<Regal>> map = new HashMap<>();

        for (Regal regal: regLst){
            cooLst.add(regal.getTop().getY());
            if ( map.containsKey(regal.getTop().getY())){
                map.get(regal.getTop().getY()).add(regal);
            } else {
                List<Regal> lst = new ArrayList<>();
                lst.add(regal);
                map.put(regal.getTop().getY(), lst);
            }
        }

        Collections.sort(cooLst);
        return map.get(cooLst.get(0)).get(0);
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

        double fromNextPoint = abs(nextPoint.getX() - parking.getPosition().getX()) + abs(nextPoint.getY() - parking.getPosition().getX());
        double toNextPoint = abs(nextPoint.getX() - position.getX()) + abs(nextPoint.getY() - position.getX());
        double min = (fromNextPoint + toNextPoint) / speed;

        if (power < min * 1.05) {
            nextPoint = null;
            status = 7;
            return true;
        }
        System.out.println("min:" + min + "actual:" + power);
        return false;
    }

    @Override
    public void update() {
        switch (status){
            case 0: //waiting for order
                order = parking.getOrder();
                inside = new HashMap<>();
                load = 0;

                if (order != null) {
                    status += 1; // Go to processing order
                    parking.updateCarriage(this);
                    order.update();
                    this.calculateRoad();
                }
                break;
            case 1:
                // go to regal acces point

                if (nextPoint == null){
                    nextRegal = this.getNextRegal();
                    nextPoint = (nextRegal).getTop();
                    if (this.checkWayPower()){
                        break;
                    }
                }
                double diffY = nextPoint.getY() - position.getY() ;
                double diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0){
                    nextPoint = null;
                    status += 1;
                }
                this.checkPower();
                break;

            case 2:
                //move to shelf

                if (nextPoint == null){
                    nextShelf = (Shelf) sortedListItems.get(nextRegal).keySet().toArray()[0];
                    nextPoint = nextShelf.getAccessPoint();
                    if (this.checkWayPower()){
                        break;
                    }
                }

                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
                    status += 1;
                }
                this.checkPower();
                break;

            case 3:
                //load item from shelf
                List<Item> items = sortedListItems.get(nextRegal).get(nextShelf);

                if (this.checkLoad(items.get(0))){
                    break;
                }
                this.loadItem(nextShelf, items.get(0));
                items.remove(0);


                if (items.size() == 0){
                    sortedListItems.get(nextRegal).remove(nextShelf);
                    if (sortedListItems.get(nextRegal).size() == 0) {
                        sortedListItems.remove(nextRegal);

                        if (sortedListItems.size() == 0) {
                            nextPoint = nextRegal.getBottom();
                        }else{
                            Coordinates next = this.getNextRegal().getTop();

                            if (next.getY() > position.getY()) {
                                nextPoint = nextRegal.getBottom();
                            } else {
                                nextPoint = nextRegal.getTop();
                            }
                        }
                        this.checkWayPower();

                        status += 1;
                        nextRegal = null;
                        nextShelf = null;
                    } else {
                        nextShelf = (Shelf) sortedListItems.get(nextRegal).keySet().toArray()[0];
                        status = 2;
                    }

                }

                break;

            case 4:
                // back to street
                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
                    if (sortedListItems.size() == 0){
                        status +=1;
                    } else {
                        status = 1;
                    }
                }
                this.checkPower();
                break;

            case 5:
                // Go to Drop point
                if (nextPoint == null){
                    nextPoint = parking.getDropPointCoords();
                    if (this.checkWayPower()){
                        break;
                    }
                }

                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
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

                if (sortedListItems.size() > 0){
                    status = 1;
                    load = 0;
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
                if (nextPoint == null) {
                    nextPoint = parking.getPosition();
                }

                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
                    status += 1;
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
                        if (sortedListItems.size() == 0){
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
            nextPoint = null;
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
