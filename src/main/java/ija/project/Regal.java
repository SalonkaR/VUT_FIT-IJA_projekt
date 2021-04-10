package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;

/**
 * Trieda reprezentujuca regal. Regaly obsahuju police. Regal nie je vykreslovany.
 *
 * @author Matúš Tvarožný - xtvaro00
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Regal {
    private String name;
    private ArrayList<Shelf> shelves;
    @JsonIgnore
    public Coordinates top;
    @JsonIgnore
    public Coordinates bottom;

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml.
     */
    private Regal() {
    }

    /**
     * Konstruktor, ktory nastavi regalu nazov a police, ktore do neho patria.
     * @param name Nazov regalu
     * @param shelves Police, ktore regal obsahuje
     */
    @JsonIgnore
    public Regal(String name, ArrayList<Shelf> shelves) {
        this.name = name;
        this.shelves = shelves;
    }

    /**
     * Funkcia vyzdiela niektore z atributov regalu, pre vzajomne prepojenie s inymi triedami.
     */
    public void shareMe(){
        for (Shelf shelf : shelves){
            shelf.setRegal(this);
        }
    }

    /**
     * Funkcia vracia nazov regalu.
     * @return Nazov regalu
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia vracia zoznam polic, ktore sa v regale nachadzaju.
     * @return Zoznam polic
     */
    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    /**
     * Funckia nastavi nazov regalu.
     * @param name Nazov regalu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Funkcia nastavi police, ktore budu do regalu patrit.
     * @param shelves Zoznam polic regalu
     */
    public void setShelves(ArrayList<Shelf> shelves) {
        this.shelves = shelves;
    }

    /**
     * Funkcia prida policu do regalu.
     * @param shelf Police pridavana do regalu
     */
    @JsonIgnore
    public void addShelf(Shelf shelf){
        shelves.add(shelf);
    }

    /**
     * Funkcia nastavi hornu a spodnu hranicu regalu v koordinatoch Coordinates.
     */
    @JsonIgnore
    public void setCoordinates(){
        if (!shelves.isEmpty()){
            double x = shelves.get(0).getPosition().getX();
            double topY = shelves.get(0).getPosition().getY();
            double bottomY = shelves.get(0).getPosition().getY();

            for (Shelf shelf : shelves){
                topY = Math.min(shelf.getPosition().getY(), topY);
                bottomY = Math.max(shelf.getPosition().getY(),bottomY);
            }
            int diff;
            if (shelves.get(0).isAccessPointBool()){ //true = left
                diff = - 10;
            } else {
                diff = 10 + (int)shelves.get(0).getWidth();
            }

            this.top = new Coordinates(x + diff, topY - 10);
            this.bottom = new Coordinates(x + diff, bottomY + 10 + (int)shelves.get(0).getHeight());
        }
    }

    /**
     * Funkcia vracia hornu hranicu regalu v koordinatoch Coordinates.
     * @return Horna hranica regalu
     */
    @JsonIgnore
    public Coordinates getTop() {
        if (top == null){
            this.setCoordinates();
        }
        return top;
    }

    /**
     * Funkcia vracia spodnu hranicu regalu v koordinatoch Coordinates.
     * @return Spodna hranica regalu
     */
    @JsonIgnore
    public Coordinates getBottom() {
        if (bottom == null){
            this.setCoordinates();
        }
        return bottom;
    }
}
