package ija.project;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * Rozhranie Drawable je rozhrnanie pre všetky tiedy, ktoré sa budú vykreslovať na mapu skladiska.
 */
public interface Drawable {
    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré budú objekt reprezentovať na scéne.
     *
     * @return zoznam vykreslitelných objektov
     */
    List<Shape> getGUI();

    /**
     * Metóda vracia zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o objekte.
     *
     * @return zoznam vykreslitelných objektov
     */
    List<Shape> getInfo();

    /**
     * Metóda vytvorí a vráti zoznam vykreslitelných objektov, ktoré sa vykreslia do bočneho panela a reprezentujú aktuálne informácie o objekte.
     *
     * @return zoznam vykreslitelných objektov
     */
    List<Shape> updateInfo();

    /**
     * Metóda nastaví objekt na inicializačné data.
     */
    void reset();

    /**
     * Metóda zruší označenie objektu. Volaná je MainControllerom, potom ako bolo kliknuté na iný objekt.
     */
    void off();

    /**
     * Metodá vyčistí zoznam informácii. Volaná je MainControlerrom, po tom, ako vykreslil aktualne informácie.
     */
    void infoClear();

}
