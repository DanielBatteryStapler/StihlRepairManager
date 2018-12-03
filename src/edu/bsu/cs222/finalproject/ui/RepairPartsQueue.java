package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.Repair;
import edu.bsu.cs222.finalproject.database.RepairPart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RepairPartsQueue {
    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairPartsQueue.fxml"));
        Parent loadedPane = loader.load();
        RepairPartsQueue controller = loader.getController();

        {//setup the table
            TableColumn<RepairPartViewData, String> quantityCol = new TableColumn<>("QTY");
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

            TableColumn<RepairPartViewData, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

            TableColumn<RepairPartViewData, String> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            controller.dataTable.getColumns().addAll(quantityCol, nameCol, priceCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<RepairPartViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        row.getItem().onClick(controller, row);
                    }
                });
                return row;
            });
        }

        controller.search();

        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }

    @FXML TableView<RepairPartViewData> dataTable = null;

    @FXML
    void search() {
        Main main = Main.getInstance();
        ArrayList<RepairPartViewData> viewData = new ArrayList<>();

        {
            ArrayList<RepairPart> rawParts = main.workingLayer.getRepairPartsInQueue();
            for (RepairPart part : rawParts) {
                viewData.add(RepairPartViewData.createFromRepairPart(part));
            }
        }


        ObservableList<RepairPartViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
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

        void onClick(RepairPartsQueue controller, TableRow<RepairPartViewData> row){
            Main main = Main.getInstance();

            //TODO: do something
        }
    }
}
