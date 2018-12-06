package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.Currency;
import edu.bsu.cs222.finalproject.database.Repair;
import edu.bsu.cs222.finalproject.database.RepairPart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.function.Consumer;

public class RepairPartEditor {
    private Stage stage;
    private RepairPart repairPart;
    private Consumer<RepairPart> callback = null;

    static RepairPartEditor createInstance(Stage rootStage, RepairPart repairPart) throws Exception{
        Main main = Main.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairPartEditor.fxml"));
        Parent loadedPane = loader.load();
        RepairPartEditor editor = loader.getController();
        editor.repairPart = new RepairPart(repairPart);

        {
            editor.quantityField.setText("" + repairPart.quantity);
            editor.nameField.setText(repairPart.name);
            editor.priceField.setText(NumberFormat.getCurrencyInstance().format(repairPart.price / 100.0));

            if(!repairPart.needToBuy){
                editor.markAsBoughtButton.setDisable(true);
            }
        }
        {
            editor.stage = new Stage();
            editor.stage.initOwner(rootStage);
            editor.stage.initModality(Modality.APPLICATION_MODAL);
            editor.stage.setScene(new Scene(loadedPane));
        }

        return editor;
    }

    @FXML TextField quantityField = null;
    @FXML TextField nameField = null;
    @FXML TextField priceField = null;

    @FXML Button markAsBoughtButton = null;
    @FXML Button viewRepairButton = null;

    void setCallback(Consumer<RepairPart> callback){
        this.callback = callback;
    }

    void show(){
        stage.show();
    }

    void disableViewingRepair(){
        viewRepairButton.setDisable(true);
    }

    @FXML
    void exit(){
        stage.close();
    }

    @FXML
    void updateRepairPart() {
        Main main = Main.getInstance();

        quantityField.setStyle("-fx-control-inner-background: #ffffff");
        nameField.setStyle("-fx-control-inner-background: #ffffff");
        priceField.setStyle("-fx-control-inner-background: #ffffff");

        try {
            repairPart.quantity = Integer.parseInt(quantityField.getText());
            if(repairPart.quantity < 0){
                throw new NumberFormatException("Number cannot be negative");
            }
        }
        catch(NumberFormatException e){
            quantityField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }

        if(nameField.getText().isEmpty()){
            nameField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        repairPart.name = nameField.getText();

        if(!Currency.isValid(priceField.getText())){
            priceField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        repairPart.price = Currency.toNormalized(priceField.getText());

        main.workingLayer.updateRepairPart(repairPart);

        if(callback != null) {
            callback.accept(repairPart);
        }

        stage.close();
    }

    @FXML
    void markAsBought(){
        repairPart.needToBuy = false;//this will be pushed to the database once the user hits "update"
        markAsBoughtButton.setDisable(true);
    }

    @FXML
    public void viewRepair() throws Exception{
        Main main = Main.getInstance();

        Repair repair = main.workingLayer.getRepairWithId(repairPart.repairId);
        RepairEditor editor = RepairEditor.createInstance(stage, repair);
        stage.close();
        editor.setCallback(repair_ -> {
            if(callback != null){
                callback.accept(null);
            }
        });
        editor.show();
    }
}
