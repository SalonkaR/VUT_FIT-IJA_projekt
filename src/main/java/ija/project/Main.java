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

        for (Item item : data.getItems()){
            item.shareMe();
        }
        for (Regal regal : data.getRegals()){
            regal.shareMe();
        }
        for (Carriage carriage : data.getCarriages()) {
            carriage.setPosition();
        }
        //Item generator usage
        for (ItemGenerator itemGenerator : data.getItemGenerators()){
            itemGenerator.setShelves(data.getShelves());
            itemGenerator.createIt();
        }
        //Item generator usage
        for (OrderGenerator orderGenerator : data.getOrderGenerators()){
            orderGenerator.setGoods(data.getGoods());
            orderGenerator.setDropPoint(data.getDropPoint().get(0));
            orderGenerator.generate();
        }

        controller.setElements(elements);
        controller.setData(data);
        controller.startTime(55);
    }
}
