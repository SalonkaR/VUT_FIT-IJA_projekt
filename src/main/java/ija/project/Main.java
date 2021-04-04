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

        List<Cordinate> coordinates = new ArrayList<>();
        coordinates.add(new Cordinate(500, 500));

        controler.setElements(Arrays.asList(new Shelf(new Cordinate(100,100),50,50), new Shelf(new Cordinate(100,155),50,50)));

        Shelf shelf1 = new Shelf(coordinates.get(0),50,50);
        controler.setElements(Arrays.asList(shelf1));

        Data data = new Data();

        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);

        Shelf shelf2 = mapper.readValue(new File("data.yml"), Shelf.class);
        shelf2.makeGui();
        //mapper.writeValue(new File("data.yml"), shelf1);
        controler.setElements(Arrays.asList(shelf2));
        System.out.println(shelf2.getPosition());
        System.out.println(shelf1.toString());
        System.out.println(shelf2.toString());
    }
}
