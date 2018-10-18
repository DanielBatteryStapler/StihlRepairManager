package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }

    @FXML UserSelector userField = null;
    @FXML TextField modelField = null;
    @FXML TextField serialField = null;
    @FXML Label errorLabel = null;

    @FXML
    void submit() {
        User user = userField.getUser();
        if(user == null){
            errorLabel.setText("You must Select a User First");
        }
        Item item = new Item();
        item.modelNumber = modelField.getText();
        item.serialNumber = serialField.getText();
        Main main = Main.getInstance();
        main.workingLayer.addNewPurchase(user, item);
        errorLabel.setText("Purchase Successfully Added!");
    }
}
