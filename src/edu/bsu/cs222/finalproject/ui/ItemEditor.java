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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ItemEditor {
    private Stage stage;
    private Stage rootStage;
    private Item item;
    private Consumer<Item> callback = null;

    static ItemEditor createInstance(Stage rootStage, Item item) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ItemEditor.fxml"));
        Parent loadedPane = loader.load();
        ItemEditor editor = loader.getController();
        editor.item = item;
        editor.rootStage = rootStage;

        {
            editor.modelField.setText(item.modelNumber);
            editor.serialField.setText(item.serialNumber);
        }
        {//setup the table
            TableColumn<ItemEditor.ItemViewData, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));

            TableColumn<ItemEditor.ItemViewData, String> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));

            TableColumn<ItemEditor.ItemViewData, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

            editor.dataTable.getColumns().addAll(dateCol, typeCol, nameCol);

            editor.dataTable.setRowFactory(table -> {
                TableRow<ItemEditor.ItemViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        row.getItem().onClick(editor, row);
                    }
                });
                return row;
            });
            editor.search();
        }
        {
            editor.stage = new Stage();
            editor.stage.initOwner(rootStage);
            editor.stage.initModality(Modality.APPLICATION_MODAL);
            editor.stage.setScene(new Scene(loadedPane));
        }

        return editor;
    }

    @FXML TextField modelField = null;
    @FXML TextField serialField = null;
    @FXML TableView<ItemEditor.ItemViewData> dataTable = null;

    void setCallback(Consumer<Item> callback){
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
    private void search(){
        Main main = Main.getInstance();
        ArrayList<ItemEditor.ItemViewData> viewData = new ArrayList<>();

        {
            Purchase purchase = main.workingLayer.getPurchaseOnItem(item.id);
            if(purchase != null){
                viewData.add(ItemEditor.ItemViewData.createFromPurchase(purchase));
            }
        }
        {
            ArrayList<Repair> rawRepairs = main.workingLayer.getRepairsOnItem(item.id);
            for (Repair repair : rawRepairs) {
                viewData.add(ItemEditor.ItemViewData.createFromRepair(repair));
            }
        }

        ObservableList<ItemEditor.ItemViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
        dataTable.getSortOrder().add(dataTable.getColumns().get(0));//sort by the first  in the table
        TableColumn<ItemEditor.ItemViewData, ?> firstColumn = dataTable.getColumns().get(0);
        firstColumn.setSortType(TableColumn.SortType.DESCENDING);
        firstColumn.setSortable(true);
    }

    @FXML
    void updateItem(){
        Main main = Main.getInstance();

        item.modelNumber = modelField.getText();
        item.serialNumber = serialField.getText();

        main.workingLayer.updateItem(item);

        if(callback != null){
            callback.accept(item);
        }
        stage.close();
    }

    @FXML
    void createRepair() throws Exception{
        Main main = Main.getInstance();
        RepairCreator creator = RepairCreator.createInstance(rootStage);
        creator.setItem(main.workingLayer.getItemWithId(item.id));
        creator.setCallback(repair -> search());
        stage.close();
        creator.show();
    }

    static public class ItemViewData{
        Purchase purchase;//one, and only one, of these{purchase, repair} must be null
        Repair repair;

        String date;
        String name;

        private ItemViewData(){

        }

        static ItemEditor.ItemViewData createFromPurchase(Purchase purchase){
            Main main = Main.getInstance();

            ItemEditor.ItemViewData data = new ItemEditor.ItemViewData();

            data.purchase = new Purchase(purchase);
            data.repair = null;//always set the other one to null

            data.date = purchase.date.toLocalDate().toString();
            data.name = main.workingLayer.getUserWithId(purchase.purchaserId).name;

            return data;
        }

        static ItemEditor.ItemViewData createFromRepair(Repair repair){
            Main main = Main.getInstance();

            ItemEditor.ItemViewData data = new ItemEditor.ItemViewData();

            data.purchase = null;//always set the other one to null
            data.repair = new Repair(repair);

            data.date = repair.dateStarted.toLocalDate().toString();
            data.name = main.workingLayer.getUserWithId(repair.userId).name;

            return data;
        }

        public String getDate(){
            return date;
        }

        public String getType() {
            if(purchase != null){
                return "Purchase";
            }//if purchase is null, it must be a repair
            return "Repair";
        }

        public String getName(){
            return name;
        }

        void onClick(ItemEditor controller, TableRow<ItemEditor.ItemViewData> row){
            Main main = Main.getInstance();
            if(repair != null){//only do something if it's a repair
                try {
                    RepairEditor editor = RepairEditor.createInstance(main.stage, row.getItem().repair);

                    editor.setCallback(repair -> {
                        try {
                            controller.search();
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
