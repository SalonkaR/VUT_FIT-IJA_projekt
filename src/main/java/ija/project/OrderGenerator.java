package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Trieda OrderGenerator generuje objednávky pred spustením programu.
 *
 * @author Matúš Tvarožný - xtvaro00
 * @version 1.0
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class OrderGenerator {
    private String name;
    private int maxGoods;
    private int maxItems;
    @JsonIgnore
    private int count;
    @JsonIgnore
    private DropPoint dropPoint;
    @JsonIgnore
    private List<Goods> goods;

    /**
     * Prazdný konštruktor, ktorý slúži pre deserializáciu yml
     */
    public OrderGenerator() {
    }

    /**
     * Konštruktor vytvára inštanciu triedy OrderGenerátor. Nastavuje jej meno, maximalný počet druhov tovaru a maximalny počet ks Itemeov jedného druhu.
     * Tieto údaje sú potrebné pre generovanie novej objednávky.
     *
     * @param name meno
     * @param maxGoods maximalný počet druhov tovaru.
     * @param maxItems maximalny počet ks Itemeov jedného druhu.
     */
    @JsonIgnore
    public OrderGenerator(String name, int maxGoods, int maxItems) {
        this.name = name;
        this.maxGoods = maxGoods;
        this.maxItems = maxItems;
    }

    /**
     * Metoda slúži na vygenerovanie objednávky.
     */
    public void generate(){
        Random random = new Random();
        HashMap<Goods, Integer> list = new HashMap<Goods, Integer>();
        int r;
        int j;
        for (int i = 0; i < maxGoods; i++){
            r = random.nextInt(goods.size());
            j = random.nextInt(maxItems);
            if (j == 0){
                j = 1;
            }
            list.put(goods.get(r), j);
        }
        Order order = new Order(this.name, this.dropPoint, list);
    }

    /**
     * Metóda slúži na nastavenie DropPointu. Toto prepojenie je dôležité, aby sa po vytvorení objednávky vedela objednavka poslať drop pointu, ktorý ich manažuje.
     *
     * @param dropPoint DropPoint.
     */
    public void setDropPoint(DropPoint dropPoint) {
        this.dropPoint = dropPoint;
    }

    /**
     * Metóda slúži na nastavenie zoznamu všetkých dostupých tovarov.
     *
     * @param goods zoznamu všetkých dostupých tovarov.
     */
    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    /**
     * Metóda vracia meno OrderGenerátoru.
     *
     * @return meno OrderGenerátoru.
     */
    public String getName() {
        return name;
    }
}
