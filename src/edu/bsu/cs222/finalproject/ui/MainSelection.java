package edu.bsu.cs222.finalproject.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MainSelection {
    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/MainSelection.fxml"));
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
    void userLookup() throws Exception{
        UserLookup.showScene();
    }

    @FXML
    void addPurchase() throws Exception{
        Main main = Main.getInstance();
        PurchaseCreator creator = PurchaseCreator.createInstance(main.stage);
        creator.show();
    }

    @FXML
    void addRepair() throws Exception{
        Main main = Main.getInstance();
        RepairCreator creator = RepairCreator.createInstance(main.stage);
        creator.show();
    }
}
