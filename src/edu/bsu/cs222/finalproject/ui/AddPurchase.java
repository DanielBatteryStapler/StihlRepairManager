package edu.bsu.cs222.finalproject.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;

public class AddPurchase {
    static void showScene() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((new File("fxml/AddPurchase.fxml")).toURI().toURL());
        Parent loadedPane = loader.load();
        //AddPurchase controller = loader.getController();

        Main main = Main.getInstance();
        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML UserSelector userSelector = null;

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }
}
