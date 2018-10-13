package edu.bsu.cs222.finalproject.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.File;

public class MainSelection {
    static void showScene() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((new File("fxml/MainSelection.fxml")).toURI().toURL());
        Parent loadedPane = loader.load();
        MainSelection controller = loader.getController();

        Main main = Main.getInstance();

        controller.welcomeText.setText("Welcome, " + main.currentEmployee.name + "!");

        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    Label welcomeText = null;

    @FXML
    void addPurchase() throws Exception{
        AddPurchase.showScene();
    }
}
