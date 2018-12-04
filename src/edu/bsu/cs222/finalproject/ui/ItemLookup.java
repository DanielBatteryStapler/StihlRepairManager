package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ItemLookup {
    private Stage stage;
    private Stage rootStage;

    static ItemLookup createInstance(Stage rootStage) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ItemLookup.fxml"));
        Parent loadedPane = loader.load();
        ItemLookup lookup = loader.getController();

        lookup.rootStage = rootStage;
        lookup.stage = new Stage();
        lookup.stage.initOwner(rootStage);
        lookup.stage.initModality(Modality.APPLICATION_MODAL);
        lookup.stage.setScene(new Scene(loadedPane));

        return lookup;
    }

    void show(){
        stage.show();
    }

    @FXML ItemSelector itemField = null;

    @FXML
    void submit() throws Exception{
        Item item = itemField.getItem();
        if(item == null){
            itemField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        ItemEditor editor = ItemEditor.createInstance(rootStage, item);
        stage.close();
        editor.show();
    }

    @FXML
    void cancel(){
        stage.close();
    }
}
