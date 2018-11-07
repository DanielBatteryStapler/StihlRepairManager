package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class PurchaseEditor {
    private Stage stage;
    private Stage rootStage;
    private Purchase purchase;
    private Consumer<Purchase> callback = null;

    static PurchaseEditor createInstance(Stage rootStage, Purchase purchase) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/PurchaseEditor.fxml"));
        Parent loadedPane = loader.load();
        PurchaseEditor editor = loader.getController();
        editor.purchase = purchase;
        editor.rootStage = rootStage;

        {
            editor.userViewer.setUser(main.workingLayer.getUserWithId(purchase.purchaserId));
            Item item = main.workingLayer.getItemWithId(purchase.itemId);
            editor.modelField.setText(item.modelNumber);
            editor.serialField.setText(item.serialNumber);
        }
        {
            editor.stage = new Stage();
            editor.stage.initOwner(rootStage);
            editor.stage.initModality(Modality.APPLICATION_MODAL);
            editor.stage.setScene(new Scene(loadedPane));
        }

        return editor;
    }

    @FXML UserViewer userViewer = null;
    @FXML TextField modelField = null;
    @FXML TextField serialField = null;

    void setCallback(Consumer<Purchase> callback){
        this.callback = callback;
    }

    void show(){
        stage.show();
    }

    @FXML
    void exit(){
        stage.close();
    }

    @FXML
    void updatePurchase(){
        Main main = Main.getInstance();
        Item item = main.workingLayer.getItemWithId(purchase.itemId);

        item.modelNumber = modelField.getText();
        item.serialNumber = serialField.getText();

        main.workingLayer.updateItem(item);

        if(callback != null){
            callback.accept(purchase);
        }
        stage.close();
    }

    @FXML
    void deletePurchase(){
        Main main = Main.getInstance();
        //Item item = main.workingLayer.getItemWithId(purchase.itemId);

        main.workingLayer.deletePurchase(purchase);//only delete the purchase
        //main.workingLayer.deleteItem(item);

        if(callback != null){
            callback.accept(null);
        }
        stage.close();
    }

    @FXML
    void createRepair() throws Exception{
        Main main = Main.getInstance();
        RepairCreator creator = RepairCreator.createInstance(rootStage);
        creator.setItem(main.workingLayer.getItemWithId(purchase.itemId));
        creator.setUser(main.workingLayer.getUserWithId(purchase.purchaserId));
        creator.setCallback(repair -> {
            if(callback != null){
                callback.accept(purchase);
            }
        });
        stage.close();
        creator.show();
    }
}
