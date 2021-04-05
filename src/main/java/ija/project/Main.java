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
import java.util.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root= loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controller = loader.getController();
        List<Drawable> elements  = new ArrayList<>();

        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);
        Data data =  mapper.readValue(new File("data.yml"), Data.class);
        elements.addAll(data.getShelves());
        elements.addAll(data.getDropPoint());
        elements.addAll(data.getParking());

        //Carriage carriage1 = new Carriage("carriage1", parking, parking.getPosition());

        //elements.add(carriage1);

        controller.setElements(elements);


        for (Item item : data.getItems()){
            item.shareMe();
        }

        controller.setData(data);
/*
        HashMap<Goods, Integer> list1 = new HashMap<Goods, Integer>();
        list1.put(goods1, 3);
        list1.put(goods2, 2);
        Order order1 = new Order("0001", dropPoint, list1);
*/
        controller.startTime();
    }
}
