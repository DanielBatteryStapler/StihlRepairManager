package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Repair;
import edu.bsu.cs222.finalproject.database.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RepairCreator {
    private Stage stage;
    private Stage rootStage;
    private Consumer<Repair> callback = null;

    static RepairCreator createInstance(Stage rootStage) throws Exception{
        Main main = Main.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairCreator.fxml"));
        Parent loadedPane = loader.load();
        RepairCreator creator = loader.getController();

        creator.rootStage = rootStage;
        creator.stage = new Stage();
        creator.stage.initOwner(rootStage);
        creator.stage.initModality(Modality.APPLICATION_MODAL);
        creator.stage.setScene(new Scene(loadedPane));

        return creator;
    }

    void setCallback(Consumer<Repair> callback){
        this.callback = callback;
    }

    void setItem(Item item){
        itemField.setItem(item);
    }

    void setUser(User user){
        userField.setUser(user);
    }

    void show(){
        stage.show();
    }

    @FXML ItemSelector itemField = null;
    @FXML UserSelector userField = null;

    @FXML
    void submit() throws Exception{
        Item item = itemField.getItem();
        User user = userField.getUser();
        if(item == null){
            itemField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        else if (user == null) {
            userField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }

        Main main = Main.getInstance();
        Repair repair = main.workingLayer.makeNewRepair(user, item);

        RepairEditor editor = RepairEditor.createInstance(rootStage, repair);
        editor.setCallback(callback);
        stage.close();
        editor.show();
    }

    @FXML
    void cancel(){
        stage.close();
    }
}
