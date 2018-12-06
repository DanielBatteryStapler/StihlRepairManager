package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.DateFormatter;
import edu.bsu.cs222.finalproject.backend.Print;
import edu.bsu.cs222.finalproject.database.Repair;
import edu.bsu.cs222.finalproject.database.RepairPart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Consumer;

public class RepairEditor {
    private Stage stage;
    private Repair repair;
    private Consumer<Repair> callback = null;

    static RepairEditor createInstance(Stage rootStage, Repair repair) throws Exception{
        Main main = Main.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairEditor.fxml"));
        Parent loadedPane = loader.load();
        RepairEditor editor = loader.getController();
        editor.repair = new Repair(repair);

        {
            editor.userViewer.setUser(main.workingLayer.getUserWithId(repair.userId));
            editor.itemViewer.setItem(main.workingLayer.getItemWithId(repair.itemId));

            editor.dateStarted.setText("Started: " + DateFormatter.formatDate(repair.dateStarted));
            if(repair.dateCompleted == null){
                editor.dateCompleted.setText("Completed: Not Complete");
            }
            else{
                editor.dateCompleted.setText("Completed: " + DateFormatter.formatDate(repair.dateCompleted));
                editor.markFinishedButton.setDisable(true);
                editor.updateRepairButton.setDisable(true);
                editor.descriptionField.setEditable(false);
                editor.descriptionCompletedField.setEditable(false);
                editor.newRepairPartButton.setDisable(true);
            }

            editor.descriptionField.setWrapText(true);
            editor.descriptionField.setText(repair.description);

            editor.descriptionCompletedField.setWrapText(true);
            editor.descriptionCompletedField.setText(repair.descriptionCompleted);
        }
        {//setup the table
            TableColumn<RepairPartViewData, String> quantityCol = new TableColumn<>("QTY");
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

            TableColumn<RepairPartViewData, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

            TableColumn<RepairPartViewData, String> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            editor.repairPartsTable.getColumns().addAll(quantityCol, nameCol, priceCol);

            editor.repairPartsTable.setRowFactory(table -> {
                TableRow<RepairPartViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        row.getItem().onClick(editor, row);
                    }
                });
                return row;
            });
            editor.updateRepairPartsTable();
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
    @FXML ItemViewer itemViewer = null;
    @FXML Label dateStarted = null;
    @FXML Label dateCompleted = null;

    @FXML TableView<RepairPartViewData> repairPartsTable = null;
    @FXML Button newRepairPartButton = null;


    @FXML TextArea descriptionField = null;
    @FXML TextArea descriptionCompletedField = null;

    @FXML Button markFinishedButton = null;
    @FXML Button updateRepairButton = null;

    void setCallback(Consumer<Repair> callback){
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
    void updateRepair(){
        Main main = Main.getInstance();

        repair.description = descriptionField.getText();
        repair.descriptionCompleted = descriptionCompletedField.getText();

        main.workingLayer.updateRepair(repair);

        if(callback != null){
            callback.accept(repair);
        }
        stage.close();
    }

    @FXML
    void printRepair(){
        Main main = Main.getInstance();

        repair.description = descriptionField.getText();
        repair.descriptionCompleted = descriptionCompletedField.getText();

        main.workingLayer.updateRepair(repair);
        Print.printRepair(repair);
        if(callback != null){
            callback.accept(repair);
        }
        stage.close();
    }

    @FXML
    void finishRepair() {
        Main main = Main.getInstance();
        try {
            ConfirmationDialog confirm = ConfirmationDialog.createInstance(stage);
            confirm.setQuestion("Are you sure you want to mark this Repair as Completed?\nYou can NOT undo this action.");
            confirm.setCallback(() -> {
                repair.description = descriptionField.getText();
                repair.descriptionCompleted = descriptionCompletedField.getText();
                repair.dateCompleted = new Date(Calendar.getInstance().getTime().getTime());//add the time completed field to mark it as finished

                main.workingLayer.updateRepair(repair);

                dateCompleted.setText("Completed: " + DateFormatter.formatDate(repair.dateCompleted));
                markFinishedButton.setDisable(true);
                updateRepairButton.setDisable(true);
                descriptionField.setEditable(false);
                descriptionCompletedField.setEditable(false);
                newRepairPartButton.setDisable(true);

                if(callback != null){
                    callback.accept(repair);
                }
            });
            confirm.show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void updateRepairPartsTable(){
        Main main = Main.getInstance();
        ArrayList<RepairPartViewData> viewData = new ArrayList<>();

        {
            ArrayList<RepairPart> repairPartsData = main.workingLayer.getRepairPartsOnRepair(repair.id);
            for(RepairPart i : repairPartsData){
                viewData.add(RepairPartViewData.createFromRepairPart(i));
            }
        }

        ObservableList<RepairPartViewData> observableListData = FXCollections.observableList(viewData);
        repairPartsTable.setItems(observableListData);
    }

    @FXML
    void newRepairPart() {
        Main main = Main.getInstance();

        try {
            RepairPart part = new RepairPart();
            part.repairId = repair.id;

            main.workingLayer.insertRepairPart(part);

            RepairPartEditor editor = RepairPartEditor.createInstance(stage, part);
            editor.setCallback(repairPart -> updateRepairPartsTable());
            editor.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static public class RepairPartViewData{
        RepairPart repairPart;

        String quantity;
        String name;
        String price;

        private RepairPartViewData(){

        }

        static RepairPartViewData createFromRepairPart(RepairPart repairPart){
            RepairPartViewData data = new RepairPartViewData();

            data.repairPart = new RepairPart(repairPart);

            data.quantity = "" + repairPart.quantity;
            data.name = repairPart.name;
            data.price = NumberFormat.getCurrencyInstance().format(repairPart.price / 100.0);

            return data;
        }

        public String getQuantity(){
            return quantity;
        }

        public String getName(){
            return name;
        }

        public String getPrice(){
            return price;
        }

        void onClick(RepairEditor controller, TableRow<RepairPartViewData> row){
            try {
                RepairPartEditor editor = RepairPartEditor.createInstance(controller.stage, row.getItem().repairPart);
                editor.setCallback(repairPart -> controller.updateRepairPartsTable());
                editor.disableViewingRepair();
                editor.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
