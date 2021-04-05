package ija.project;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.*;

import static javafx.scene.paint.Color.GREEN;

public class Carriage implements Drawable, Mover {
    private String name;
    private Coordinates position;
    private int speed = 2;
    private int status;
    private Circle mainCircle;
    private Parking parking;
    private Order order;


    private List<Shape> gui = new ArrayList<>();
    private List<Shape> info = new ArrayList<>();
    private HashMap<Goods, Integer> need;
    private HashMap<Goods, List<Item>> inside;

    private HashMap<Regal, HashMap<Shelf, List<Item>>> sortedListItems;
    private Coordinates nextPoint;
    private Regal nextRegal;
    private Shelf nextShelf;


    public Carriage(String name, Parking parking, Coordinates position) {
        this.name = name;
        this.parking = parking;
        this.position = position;
        this.status = 0;
        mainCircle = new Circle(position.getX(), position.getY(), 6, GREEN);
        gui.add(mainCircle);


    }

    public String getName() {
        return name;
    }

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

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public List<Shape> getInfo() {
        return info;
    }

    @Override
    public void off() {

    }

    @Override
    public void infoClear() {

    }

    public void move(double diffX, double diffY){
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

        shelf.removeItem(item);

    }

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

    @Override
    public void update() {
        switch (status){
            case 0: //waiting for order
                order = parking.getOrder();
                inside = new HashMap<>();

                if (order != null) {
                    status += 1; // Go to processing order
                    order.update();
                    this.calculateRoad();
                    System.out.println("0:I Have new order" + order);
                }
                break;
            case 1:
                // go to regal acces point
                if (nextPoint == null){
                    nextRegal = this.getNextRegal();
                    nextPoint = (nextRegal).getTop();
                }
                double diffY = nextPoint.getY() - position.getY() ;
                double diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0){
                    nextPoint = null;
                    status += 1;
                }
                break;

            case 2:
                //move to shelf
                if (nextPoint == null){

                    nextShelf = (Shelf) sortedListItems.get(nextRegal).keySet().toArray()[0];
                    nextPoint = nextShelf.getAccessPoint();
                }

                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
                    status += 1;
                }
                break;

            case 3:
                //load item from shelf
                List<Item> items = sortedListItems.get(nextRegal).get(nextShelf);

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
                // back to upper street
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
                break;

            case 5:
                // Go to Drop point
                if (nextPoint == null){
                    nextPoint = parking.getDropPointCoords();
                }

                diffY = nextPoint.getY() - position.getY() ;
                diffX = nextPoint.getX() - position.getX() ;

                this.move(diffX, diffY);

                if (diffX == 0 && diffY == 0) {
                    nextPoint = null;
                    status += 1;
                }
                break;
            case 6:
                //selling items
                for (Goods goods : inside.keySet()){
                    for (Item item : inside.get(goods)){
                        item.sell();
                    }
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
                    status = 0;
                }
                break;
        }

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
}
