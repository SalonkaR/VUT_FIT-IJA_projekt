package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.event.EventHandler;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static javafx.scene.paint.Color.*;

/**
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Shelf implements Drawable {
    private String id;
    private Coordinates position;
    private boolean accessPointBool;  //true = left, false = right
    @JsonIgnore
    private Coordinates accessPoint;
    @JsonIgnore
    private double width = 30;
    @JsonIgnore
    private double height = 30;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private Map<Goods, List<Item>> items = new HashMap();
    @JsonIgnore
    private List<Shape> info = new ArrayList<>();
    @JsonIgnore
    private Rectangle mainRect;
    @JsonIgnore
    private Circle accessCircle;
    @JsonIgnore
    private Regal regal;

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml
     */
    private Shelf(){
    }

    /**
     * Konstruktor, ktory nastavi id, poziciu, pristupovy bod a rozmery danej police kvoli spravnemu vykresleniu na platne
     * @param id ID police
     * @param position Pozicia police vykreslenej na platne, zadavana v koordinatoch x,y
     * @param accessPointBool Pristupovy bod k danej polici(mame na vyber bud zprava(false) alebo zlava(true))
     * @param accessPoint Pristupovy bod k danej polici zadavany v koordinatoch x,y
     * @param width Vyska police pre spravne vykreslenie na platne
     * @param height Sirka police pre spravne vykreslenie na platne
     */
    @JsonIgnore
    public Shelf(String id, Coordinates position, boolean accessPointBool, Coordinates accessPoint, double width, double height) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
        this.accessPointBool = accessPointBool;
        this.accessPoint = accessPoint;
        this.makeAccessPoint();
        this.makeGui();
    }

    public void reset() {
        items = new HashMap();
        this.makeGui();
    }
    /**
     * Funkcia vykresli policu na platno vo forme stvoruholnika a zaobstarava kliknutie na nu pre vypisanie jej aktualneho obsahu
     */
    @JsonIgnore
    public void makeGui(){
        gui = new ArrayList<>();
        mainRect = new Rectangle(position.getX(), position.getY(), width, height);
        mainRect.setFill(SKYBLUE);
        gui.add(mainRect);

        this.makeAccessPoint();
        //accessCircle = new Circle(accessPoint.getX(), accessPoint.getY(),3, LIMEGREEN);
        //gui.add(accessCircle);

        Shelf shelf = this;
        mainRect.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                if(mainRect.getFill() == SKYBLUE) {
                    Text text = new Text(shelf.getContent());
                    text.setWrappingWidth(125);
                    info.add(text);
                    mainRect.setFill(RED);
                }
            }
        });
    }

    /**
     * Funkcia vracia poziciu danej police
     * @return Pozicia police
     */
    public Coordinates getPosition() {
        return position;
    }

    /**
     * Funkcia vracia ID danej police
     * @return ID police
     */
    public String getId() {
        return id;
    }

    /**
     * Funckia nastavi suradnice pristupoveho bodu k danej polici na zaklade bool hodnoty accessPointBool,
     * pozicie a rozmerov danej police
     */
    public void makeAccessPoint() {
        double y = this.getPosition().getY() + height/2;
        double x;
        if (accessPointBool){
            //accessPoint = Left
            x = this.getPosition().getX() - 10;
        } else {
            //accessPoint = Right
            x = this.getPosition().getX() + width + 10;
        }
        accessPoint = new Coordinates(x,y);
    }

    /**
     * Funkcia odstrani z police jeden z itemov(prvy), ktory patri pod dane goods z parametru
     * @param goods Goods, ktoreho sa z police odstrani jeden kus
     * @return Ak je polica prazdna alebo sa na nej dany goods nenachadza vracia NULL, inak vracia odstraneny item
     */
    @JsonIgnore
    public Item removeItem(Goods goods){
        List<Item> lst = (List)this.items.get(goods);
        if (lst == null) {
            return null;
        } else {
            return lst.isEmpty() ? null : (Item)lst.remove(0);
        }
    }

    /**
     * Funkcia odstrani z police dany konkretny item
     * @param item Item na odstranenie z police
     */
    @JsonIgnore
    public void removeItem(Item item){
        List<Item> lst = (List)this.items.get(item.getGoods());
        lst.remove(item);
    }

    /**
     * Funkcia na policu prida dany item
     * @param item Item na pridanie
     */
    @JsonIgnore
    public void addItem(Item item){
        Goods goods = item.getGoods();
        if(this.items.containsKey(goods)){
            ((List)this.items.get(goods)).add(item);
        }else {
            List<Item> lst = new ArrayList();
            lst.add(item);
            this.items.put(goods, lst);
        }
    }

    /**
     * Funkcia vracia obsah police na platno
     * @return zoznam shapov, ktory obsahuje text s obsahom police
     */
    @Override
    public List<Shape> getInfo() {
        return info;
    }

    /**
     * Funkcia vracia aktualizovany obsah police na platno
     * @return zoznam shapov, ktory obsahuje text s obsahom police
     */
    @Override
    public List<Shape> updateInfo() {
        Text text = new Text(this.getContent());
        text.setWrappingWidth(125);
        info.add(text);
        return info;
    }

    /**
     * Funkcia vracia GUI police
     * @return Zoznam shapov reprezentujuci GUI police
     */
    @Override
    public List<Shape> getGUI() {
        if(accessPoint == null){
            this.makeAccessPoint();
        }
        if(gui == null){
            this.makeGui();
        }
        return gui;
    }

    /**
     * Funkcia, ktora ulozi do textovej podoby obsah police
     * @return Obsah police vo forme stringu
     */
    private String getContent(){
        String str = "";
        for (Goods i : items.keySet()) {
            str += i.getName() + "-" + items.get(i).size() + "ks\n" ;
        }
        return str;
    }

    /**
     * Pomocna funckia, ktora pri prekliknuti na iny objekt vycisti informacie o obsahu police
     */
    public void infoClear(){
        info.clear();
    }

    /**
     * Pomocna funckia, ktora pri prekliknuti na iny objekt opat sfarbi policu do vychodzej farby
     */
    public void off(){
        mainRect.setFill(SKYBLUE);
    }

    /**
     * Funkcia vracia regal do ktoreho polica patri
     * @return Regal do ktoreho je polica zaradena
     */
    public Regal getRegal() {
        return regal;
    }

    /**
     * Funkcia nastavi polici regal do ktoreho patri
     * @param regal Regal do ktoreho bude polica zaradena
     */
    @JsonIgnore
    public void setRegal(Regal regal) {
        this.regal = regal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelf)) return false;

        Shelf shelf = (Shelf) o;

        return id.equals(shelf.id);
    }

    /**
     * Funkcia vracajuca unikatny hash kod na ID police
     * @return Hash kod
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Funkcia v podobe bool vyrazu vracia poziciu pristupoveho bodu k polici (nalavo alebo napravo)
     * @return Orientacia pristupoveho bodu vzhladom na policu
     */
    public boolean isAccessPointBool() {
        return accessPointBool;
    }

    /**
     * Funkcia vracia koordinaty pristupoveho bodu k polici, ak este nie su vytvorene zavola funkciu na ich vytvorenie
     * @return Koordinaty potrebneho pristupoveho bodu
     */
    public Coordinates getAccessPoint() {
        if (accessPoint == null){
            this.makeAccessPoint();
        }
        return accessPoint;
    }

    /**
     * Funkcia, ktora vracia sirku police
     * @return Sirka police
     */
    public double getWidth() {
        return width;
    }

    /**
     * Funkcia, ktora vracia vysku police
     * @return Vyska police
     */
    public double getHeight() {
        return height;
    }
}
