package ija.project;

import ija.project.Cordinate;
import ija.project.MainController;
import ija.project.Shelf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root= loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controler = loader.getController();
        controler.setElements(Arrays.asList(new Shelf(new Cordinate(100,100),50,50), new Shelf(new Cordinate(100,155),50,50)));
    }
}
