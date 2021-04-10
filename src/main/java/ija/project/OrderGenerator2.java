package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Trieda OrderGenerator2 generuje objednávky počas behu programu vždy za nejaký čas.
 *
 * @author Jakub Sokolík - xsokol14
 * @version 1.0
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class OrderGenerator2 {
    private String name;
    private int time;
    private int maxGoods;
    private int maxItems;
    private DropPoint dropPoint;

    @JsonIgnore
    private int cnt = 0;
    @JsonIgnore
    private int cntTime = 0;
    @JsonIgnore
    private Random random = new Random();
    @JsonIgnore
    private List<Goods> goods;

    /**
     * Prazdný konštruktor, ktorý slúži pre deserializáciu yml
     */
    public OrderGenerator2() {
    }

    /**
     * Konštruktor vytvára inštanciu OrderGenerator2
     *
     * @param name meno
     * @param point DropPoint, ktorý manažuje všetky vytvorené Ordery.
     * @param time Čas, za ktorý sa vygeneruje nová objednávka. V počtoch framov.
     * @param maxItems Celkový maximalný počet Itemov na objednávku.
     * @param maxGoods Celkový počet druhov tovarov na objednávku.
     */
    @JsonIgnore
    public OrderGenerator2(String name, DropPoint point, int time, int maxItems, int maxGoods) {
        this.name = name;
        this.time = time;
        this.dropPoint = point;
        this.maxItems = maxItems;
        this.maxGoods = maxGoods;
    }

    /**
     * Metóda generu objednávku
     */
    @JsonIgnore
    public void generate(){
        cntTime += 1;


        if (cntTime == time){
            cntTime = 0;
            int numItems = 0;
            int currentNumItems = 0;
            HashMap<Goods, Integer> list = new HashMap<>();

            int numOfGoods = 0;
            while (numOfGoods == 0) {
                numOfGoods = random.nextInt(maxGoods + 1);
            }

            for (int i = 0; i<= numOfGoods; i++){
                if (maxItems - currentNumItems < 2){
                    currentNumItems -= 2;
                }
                while (numItems == 0) {
                    numItems = random.nextInt(maxItems - currentNumItems);
                }
                currentNumItems += numItems;
                list.put(goods.get(random.nextInt(goods.size())), numItems);
            }

            new Order(name + "/" + cnt, dropPoint, list);
            cnt += 1;
        }
    }

    /**
     * Metóda resetne order generátor na východzie hodnoty.
     *
     */
    public void reset(){
        cnt = 0;
        cntTime = 0;
    }


    /**
     * Metóda slúži na nastavenie zoznamu všetkých dostupých tovarov.
     *
     * @param goods zoznamu všetkých dostupých tovarov.
     */
    @JsonIgnore
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

    /**
     * Metóda vracia čas, za ktorý je vygenrová nová objednávka.
     *
     * @return čas, za ktorý je vygenrová nová objednávka.
     */
    public int getTime() {
        return time;
    }

    /**
     * Metóda vracia maximálnu hodnotu druhov tovaru na objednávku.
     *
     * @return maximálna hodnota druhov tovaru na objednávku.
     */
    public int getMaxGoods() {
        return maxGoods;
    }

    /**
     * Metóda vracia maximálnu hodnotu kusov tovaru na objednávku.
     *
     * @return maximálna hodnota kusov tovaru na objednávku.
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * Metóda vracia DropPoint.
     *
     * @return DropPoint.
     */
    public DropPoint getDropPoint() {
        return dropPoint;
    }
}
