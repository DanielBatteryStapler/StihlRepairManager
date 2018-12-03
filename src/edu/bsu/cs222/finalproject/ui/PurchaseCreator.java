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

public class PurchaseCreator {
    private Stage stage;
    private Stage rootStage;
    private Consumer<Purchase> callback = null;

    static PurchaseCreator createInstance(Stage rootStage) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/PurchaseCreator.fxml"));
        Parent loadedPane = loader.load();
        PurchaseCreator creator = loader.getController();

        creator.rootStage = rootStage;
        creator.stage = new Stage();
        creator.stage.initOwner(rootStage);
        creator.stage.initModality(Modality.APPLICATION_MODAL);
        creator.stage.setScene(new Scene(loadedPane));

        return creator;
    }

    void setCallback(Consumer<Purchase> callback){
        this.callback = callback;
    }

    void setUser(User user){
        userField.setUser(user);
    }

    void show(){
        stage.show();
    }

    @FXML UserSelector userField = null;

    @FXML
    void submit() throws Exception{
        User user = userField.getUser();
        if(user == null){
            userField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        Item item = new Item();//make a dummy item first
        Main main = Main.getInstance();
        main.workingLayer.insertItem(item);
        Purchase purchase = main.workingLayer.makeNewPurchase(user, item);

        ItemEditor editor = ItemEditor.createInstance(rootStage, item);
        editor.setCallback(item_ -> callback.accept(purchase));
        stage.close();
        editor.show();
    }

    @FXML
    void cancel(){
        stage.close();
    }
}
