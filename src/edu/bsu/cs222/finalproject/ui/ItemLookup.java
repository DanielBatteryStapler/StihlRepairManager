package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.Repair;
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

import java.util.ArrayList;

public class ItemLookup {
    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ItemLookup.fxml"));
        Parent loadedPane = loader.load();
        ItemLookup controller = loader.getController();

        {//setup the userSelector callback
            controller.itemField.setCallback(item -> controller.search());
        }
        {//setup the table
            TableColumn<ItemLookup.ItemLookupViewData, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));

            TableColumn<ItemLookup.ItemLookupViewData, String> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));

            controller.dataTable.getColumns().addAll(dateCol, typeCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<ItemLookup.ItemLookupViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        row.getItem().onClick(controller, row);
                    }
                });
                return row;
            });
        }

        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }

    @FXML ItemSelector itemField = null;
    @FXML Label errorLabel = null;
    @FXML TableView<ItemLookup.ItemLookupViewData> dataTable = null;

    @FXML
    void search() {
        Item item = itemField.getItem();
        if(item == null){
            errorLabel.setText("You must Select an Item First");
            return;
        }
        Main main = Main.getInstance();
        ArrayList<ItemLookup.ItemLookupViewData> viewData = new ArrayList<>();

        {
            Purchase purchase = main.workingLayer.getPurchaseOnItem(item.id);
            if(purchase != null){
                viewData.add(ItemLookup.ItemLookupViewData.createFromPurchase(purchase));
            }
        }
        {
            ArrayList<Repair> rawRepairs = main.workingLayer.getRepairsOnItem(item.id);
            for (Repair repair : rawRepairs) {
                viewData.add(ItemLookup.ItemLookupViewData.createFromRepair(repair));
            }
        }


        ObservableList<ItemLookup.ItemLookupViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
        dataTable.getSortOrder().add(dataTable.getColumns().get(0));//sort by the first  in the table
        TableColumn<ItemLookup.ItemLookupViewData, ?> firstColumn = dataTable.getColumns().get(0);
        firstColumn.setSortType(TableColumn.SortType.DESCENDING);
        firstColumn.setSortable(true);
    }

    static public class ItemLookupViewData{
        Purchase purchase;//one, and only one, of these{purchase, repair} must be null
        Repair repair;

        String date;

        private ItemLookupViewData(){

        }

        static ItemLookup.ItemLookupViewData createFromPurchase(Purchase purchase){
            ItemLookup.ItemLookupViewData data = new ItemLookup.ItemLookupViewData();

            data.purchase = new Purchase(purchase);
            data.repair = null;//always set the other one to null

            data.date = purchase.date.toLocalDate().toString();

            return data;
        }

        static ItemLookup.ItemLookupViewData createFromRepair(Repair repair){
            ItemLookup.ItemLookupViewData data = new ItemLookup.ItemLookupViewData();

            data.purchase = null;//always set the other one to null
            data.repair = new Repair(repair);

            data.date = repair.dateStarted.toLocalDate().toString();

            return data;
        }

        public String getType() {
            if(purchase != null){
                return "Purchase";
            }//if purchase is null, it must be a repair
            return "Repair";
        }

        public String getDate(){
            return date;
        }

        void onClick(ItemLookup controller, TableRow<ItemLookup.ItemLookupViewData> row){
            Main main = Main.getInstance();
            if(purchase != null) {
                try {
                    PurchaseEditor editor = PurchaseEditor.createInstance(main.stage, row.getItem().purchase);

                    editor.setCallback(purchase -> {
                        try {
                            controller.itemField.setItem(main.workingLayer.getItemWithId(purchase.itemId));
                        } catch (Exception e) {
                            e.printStackTrace();//print any errors that occur
                        }
                    });

                    editor.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{//if purchase is null, then it must be a repair
                try {
                    RepairEditor editor = RepairEditor.createInstance(main.stage, row.getItem().repair);

                    editor.setCallback(repair -> {
                        try {
                            controller.itemField.setItem(main.workingLayer.getItemWithId(repair.itemId));
                        } catch (Exception e) {
                            e.printStackTrace();//print any errors that occur
                        }
                    });

                    editor.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
