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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root= loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controler = loader.getController();
        List<Drawable> shelves  = new ArrayList<>();

        List<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(500, 500));

        //controler.setElements(Arrays.asList(new Shelf(new Coordinates(100,100),50,50), new Shelf(new Coordinates(100,155),50,50)));

        //Shelf shelf1 = new Shelf(coordinates.get(0),50,50);
        //controler.setElements(Arrays.asList(shelf1));

        //Data data = new Data(coordinates, shelf1);

        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);
        Map map =  mapper.readValue(new File("data.yml"), Map.class);
        shelves.addAll(map.getShelves());



        DropPoint dropPoint = new DropPoint(new Coordinates(600,600));
        Parking parking = new Parking(dropPoint, new Coordinates(50,50));
        Carriage carriage1 = new Carriage("carriage1", parking, parking.getPosition());

        shelves.add(parking);
        shelves.add(carriage1);
        shelves.add(dropPoint);



        controler.setElements(shelves);
        System.out.println(shelves);
        //Shelf shelf2 = mapper.readValue(new File("data.yml"), Shelf.class);
        //shelf2.makeGui();
        //mapper.writeValue(new File("data.yml"), shelf2);
        //controler.setElements(Arrays.asList(shelf2));
        //System.out.println(shelf2.getPosition());
        //System.out.println(shelf1.toString());
        //System.out.println(shelf2.toString());

        //mapper.writeValue(new File("data.yml"), data);

        //List<Drawable> shelfs= Arrays.asList(new Shelf(new Coordinates(100,100),50,50), new Shelf(new Coordinates(100,155),50,50), shelf1, shelf2);

        Goods goods1 = new Goods("Stolicka");
        Goods goods2 = new Goods("Stol");
        Goods goods3 = new Goods("Taniere");

        Item item11 = new Item(goods1, (Shelf)shelves.get(0));
        Item item12 = new Item(goods1, (Shelf)shelves.get(0));
        Item item13 = new Item(goods1, (Shelf)shelves.get(1));
        Item item14 = new Item(goods1, (Shelf)shelves.get(1));
        Item item15 = new Item(goods1, (Shelf)shelves.get(1));

        Item item21 = new Item(goods2, (Shelf)shelves.get(0));
        Item item22 = new Item(goods2, (Shelf)shelves.get(2));
        Item item23 = new Item(goods2, (Shelf)shelves.get(3));

        Item item31 = new Item(goods3, (Shelf)shelves.get(2));
        Item item32 = new Item(goods3, (Shelf)shelves.get(2));
        Item item33 = new Item(goods3, (Shelf)shelves.get(2));
        Item item34 = new Item(goods3, (Shelf)shelves.get(2));
        Item item35 = new Item(goods3, (Shelf)shelves.get(2));

        HashMap<Goods, Integer> list1 = new HashMap<Goods, Integer>();
        list1.put(goods1, 3);
        list1.put(goods2, 2);
        Order order1 = new Order("0001", dropPoint, list1);

        controler.startTime();


    }
}
