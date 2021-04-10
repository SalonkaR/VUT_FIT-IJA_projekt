package ija.project;

import javafx.scene.shape.Line;

import java.util.List;

/**
 * Rozhranie Mover je rozhrnanie pre všetky tiedy, ktoré sa budú pohybovať po mape skladiska.
 */
public interface Mover {
    /**
     * Metóda vyvolá posunutie objektu.
     */
    void update();

    /**
     * Metóda vráti zoznam vykrelitelných objektov, ktoré reprezentuju cetu objektu.
     *
     * @return zoznam vykrelitelných objektov, ktoré reprezentuju cetu objektu.
     */
    List<Line> getPath();
}
