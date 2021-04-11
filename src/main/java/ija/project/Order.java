package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.List;

/**
 * Trieda Order reprezentuje objednavku, ktora je vykonavana(zbierana) vozikmi
 *
 * @author Jakub Sokolík - xsokol14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name") //, scope = Order.class
public class Order {
    private String name;
    private List<Goods> goods;
    @JsonIgnore
    private HashMap<Goods, Integer> list;
    @JsonIgnore
    private int status = 0;
    // 0- waiting
    // 1- processig
    // 2 -done
    @JsonIgnore
    private DropPoint dropPoint;

    /**
     * Prazdny konstruktor, ktory sluzi pre deserializaciu yml
     */
    private Order() {
    }

    /**
     * Funkcia vracia nazov objednavky
     * @return Nazov objednavky
     */
    public String getName() {
        return name;
    }

    /**
     * Funkcia vracia zoznam goodsov, ktore su v objednavke obsiahnute
     * @return Zoznam obsahujuci goods
     */
    public List<Goods> getGoods() {
        return goods;
    }

    /**
     * Funkcia nastavuje objednavke meno podla zadaneho atributu
     * @param name Meno objednavky
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Funkcia nastavuje objednavke goods, ktore bude obsahovat
     * @param goods Zoznam goodsov
     */
    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    /**
     * Konstruktor, ktory nastavi objednavke nazov, drop point a tovary s ich poctom
     * @param name Nazov objednavky
     * @param point Drop point
     * @param list Hashmapa obsahujuca tovary s ich poctom
     */
    @JsonIgnore
    public Order(String name, DropPoint point, HashMap<Goods, Integer> list) {
        this.name = name;
        this.dropPoint = point;
        this.list = list;
        dropPoint.addOrder(this);
    }

    /**
     * Funkcia aktualizuje status objednavky
     */
    @JsonIgnore
    public void update(){
        if (status < 2){
            dropPoint.updateOrder(status, this);
            status += 1;
        }
    }

    /**
     * Funkcia vracajuca bool hodnotu toho ci je na sklade potrebny tovar
     * @return Bool hodnota
     */
    public boolean checkCapacity (){
        for ( Goods goods : list.keySet()){
            if (goods.sizeFree() < list.get(goods)){
                return false;
            }
        }
        return true;
    }

    /**
     * Funkcia vracia status objednavky vo forme integeru
     * @return Aktualny status objednavky
     */
    @JsonIgnore
    public int getStatus() {
        return status;
    }

    /**
     * Funkcia vracia obsah objednavky vo forme hashmapy
     * @return Hashmapa objednavky, ktora pozostava z goods a ku nemu prisluchajucemu pozadovanemu poctu
     */
    @JsonIgnore
    public HashMap<Goods, Integer> getList() {
        return list;
    }

}
