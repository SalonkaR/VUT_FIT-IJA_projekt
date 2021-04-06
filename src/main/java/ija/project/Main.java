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
        elements.addAll(data.getCarriages());

        DropPoint dropPoint = data.getDropPoint().get(0);
        Parking parking = data.getParking().get(0);
        //Carriage carriage1 = new Carriage("carriage1", parking, new Coordinates(parking.getPosition().getX(), parking.getPosition().getY()));

        //elements.add(carriage1);

        controller.setElements(elements);


        for (Item item : data.getItems()){
            item.shareMe();
        }
        for (Regal regal : data.getRegals()){
            regal.shareMe();
        }

        controller.setData(data);

        List<Goods> goods = data.getGoods();


        //Item generator ussage
        for (ItemGenerator itemGenerator : data.getItemGenerators()){
            itemGenerator.setShelves(data.getShelves());
            itemGenerator.createIt();
        }

        //ItemGenerator g = data.getItemGenerators().get(0);
        //g.setShelves(data.getShelves());
        //g.createIt();

        HashMap<Goods, Integer> list1 = new HashMap<Goods, Integer>();
        list1.put(goods.get(0), 6);
        list1.put(goods.get(1), 50);
        list1.put(goods.get(2), 8);
        Order order1 = new Order("0001", dropPoint, list1);
        HashMap<Goods, Integer> list2 = new HashMap<Goods, Integer>();
        list2.put(goods.get(2), 2);
        //list2.put(goods.get(3), 4);
        //list2.put(goods.get(4), 8);
        Order order2 = new Order("0002", dropPoint, list2);

        controller.startTime(55);
    }
}
