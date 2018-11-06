package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class PurchaseCreator {
    private Stage stage;
    private Stage rootStage;
    private Consumer<Purchase> callback = null;

    static PurchaseCreator createInstance(Stage rootStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/PurchaseCreator.fxml"));
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
    @FXML Label errorLabel = null;

    @FXML
    void submit() throws Exception{
        User user = userField.getUser();
        if(user == null){
            errorLabel.setText("You must Select a User First");
            return;
        }
        Item item = new Item();//make a dummy item first
        Main main = Main.getInstance();
        Purchase purchase = main.workingLayer.addNewPurchase(user, item);

        PurchaseEditor editor = PurchaseEditor.createInstance(rootStage, purchase);
        editor.setCallback(callback);
        stage.close();
        editor.show();
    }

    @FXML
    void cancel(){
        stage.close();
    }
}
