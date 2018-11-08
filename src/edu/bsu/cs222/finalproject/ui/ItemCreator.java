package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ItemCreator {
    private Stage stage;
    private Consumer<Item> callback = null;

    static ItemCreator createInstance(Stage rootStage) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ItemCreator.fxml"));
        Parent loadedPane = loader.load();
        ItemCreator creator = loader.getController();

        creator.stage = new Stage();
        creator.stage.initOwner(rootStage);
        creator.stage.initModality(Modality.APPLICATION_MODAL);
        creator.stage.setScene(new Scene(loadedPane));

        return creator;
    }

    void setCallback(Consumer<Item> callback){
        this.callback = callback;
    }

    void setSerialNumber(String serialNumber){
        serialField.setText(serialNumber);
    }

    void show(){
        stage.show();
    }

    @FXML TextField modelField = null;
    @FXML TextField serialField= null;
    @FXML Label errorLabel = null;

    @FXML
    void submit() {

        if(modelField.getText().equals("") || serialField.getText().equals("")){
            errorLabel.setText("Model # and Serial # Must Be Filled Out");
            return;
        }

        Item newItem = new Item();
        newItem.modelNumber = modelField.getText();
        newItem.serialNumber = serialField.getText();

        Main main = Main.getInstance();
        main.workingLayer.insertItem(newItem);

        if(callback != null){
            callback.accept(newItem);
        }

        stage.close();
    }

    @FXML
    void cancel(){
        stage.close();
    }
}
