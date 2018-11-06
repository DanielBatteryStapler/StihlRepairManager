package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ListPurchases {
    static void showScene() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/ListPurchases.fxml"));
        Parent loadedPane = loader.load();
        ListPurchases controller = loader.getController();

        {//setup the userSelector callback
            controller.userField.setCallback(user -> controller.search());
        }
        {//setup the table
            TableColumn dateCol = new TableColumn("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("Date"));
            TableColumn modelCol = new TableColumn("Model #");
            modelCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("ModelNumber"));
            TableColumn serialCol = new TableColumn("Serial #");
            serialCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("SerialNumber"));
            controller.dataTable.getColumns().addAll(dateCol, modelCol, serialCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<PurchaseViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        Main main = Main.getInstance();
                        PurchaseEditor editor = null;
                        try {
                            editor = PurchaseEditor.createInstance(main.stage, row.getItem().purchase);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                            editor.setCallback(purchase -> {
                                try {
                                    controller.userField.setUser(main.workingLayer.getUserWithId(purchase.purchaserId));
                                }
                                catch(Exception e){
                                    e.printStackTrace();//print any errors that occur
                                }
                            });

                        editor.show();
                    }
                });
                return row;
            });
        }

        Main main = Main.getInstance();
        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }

    @FXML UserSelector userField = null;
    @FXML Label errorLabel = null;
    @FXML TableView dataTable = null;

    void search() {
        User user = userField.getUser();
        if(user == null){
            errorLabel.setText("You must Select a User First");
            return;
        }
        Main main = Main.getInstance();
        ArrayList<Purchase> rawPurchases = main.workingLayer.getPurchasesWithPurchaser(user.id);
        ArrayList<PurchaseViewData> purchaseData = new ArrayList<>();
        for(Purchase purchase : rawPurchases){
            purchaseData.add(new PurchaseViewData(purchase));
        }

        ObservableList<PurchaseViewData> observableListData = FXCollections.observableList(purchaseData);
        dataTable.setItems(observableListData);
    }

    @FXML
    void createNewPurchase() throws Exception{
        Main main = Main.getInstance();
        PurchaseCreator purchaseCreator = PurchaseCreator.createInstance(main.stage);
        purchaseCreator.setCallback(purchase -> {
            try {
                userField.setUser(main.workingLayer.getUserWithId(purchase.purchaserId));
            }
            catch(Exception e){
                e.printStackTrace();//print any errors that occur
            }
        });
        purchaseCreator.setUser(userField.getUser());
        purchaseCreator.show();
    }

    public class PurchaseViewData{
        PurchaseViewData(Purchase purchase){
            Main main = Main.getInstance();
            date = purchase.date.toLocalDate().toString();
            Item item = main.workingLayer.getItemWithId(purchase.itemId);
            modelNumber = item.modelNumber;
            serialNumber = item.serialNumber;
            this.purchase = new Purchase(purchase);
        }
        String date;
        String modelNumber;
        String serialNumber;
        Purchase purchase;
        public String getDate(){
            return date;
        }
        public String getModelNumber(){
            return modelNumber;
        }
        public String getSerialNumber(){
            return serialNumber;
        }
    }
}
