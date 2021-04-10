package ija.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda Main slúži na spustenie aplikacie.
 *
 * @author Matúš Tvarožný - xtvaro00
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Metoda start slúži na vytvorenie hlavného okna aplikácie. Rozloženie sa načítava z fxml súboru a nastavý sa ako scéna pre primaryStage.
     * Načítava inicializačné data z yml súboru, ktoré potom predáva MainControlleru.
     *
     * @param primaryStage Okno, do ktorého sa vloží načítaná scéna.
     * @throws Exception
     * @see MainController
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //nastavenie scény
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root= loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


        //nacitanie inicializacných dát
        List<Drawable> elements  = new ArrayList<>();
        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);
        Data data =  mapper.readValue(new File("storage2.yml"), Data.class);
        elements.addAll(data.getShelves());
        elements.addAll(data.getDropPoint());
        elements.addAll(data.getParking());
        elements.addAll(data.getCarriages());


        //setup niektorých načítaných objektov
        for (OrderGenerator2 generator : data.getOrderGenerators2()){
            generator.setGoods(data.getGoods());
        }
        for (Item item : data.getItems()){
            item.shareMe();
        }
        for (Regal regal : data.getRegals()){
            regal.shareMe();
        }
        for (Carriage carriage : data.getCarriages()) {
            carriage.setPosition();
            carriage.sendStatus();
        }
        for (ItemGenerator itemGenerator : data.getItemGenerators()){
            itemGenerator.setShelves(data.getShelves());
            itemGenerator.createIt();
        }
        for (OrderGenerator orderGenerator : data.getOrderGenerators()){
            orderGenerator.setGoods(data.getGoods());
            orderGenerator.setDropPoint(data.getDropPoint().get(0));
            orderGenerator.generate();
        }

        //spustenie Controlleru
        MainController controller = loader.getController();
        controller.setOrderGenerators(data.getOrderGenerators2());
        controller.setElements(elements);
        controller.setData(data);
        controller.startTime(true);
    }
}
