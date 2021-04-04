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
        List<Drawable> elements  = new ArrayList<>();

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(500, 500));



        Shelf shelf1 = new Shelf(coordinates.get(0),50,50);

        Data data = new Data();

        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);

        Shelf shelf2 = mapper.readValue(new File("data.yml"), Shelf.class);
        shelf2.makeGui();
        //mapper.writeValue(new File("data.yml"), shelf1);

        System.out.println(shelf2.getPosition());
        System.out.println(shelf1.toString());
        System.out.println(shelf2.toString());

        List<Drawable> shelfs= Arrays.asList(new Shelf(new Coordinate(100,100),50,50), new Shelf(new Coordinate(100,155),50,50), shelf1, shelf2);

        controler.setElements(shelfs);
        controler.startTime();
    }

}
