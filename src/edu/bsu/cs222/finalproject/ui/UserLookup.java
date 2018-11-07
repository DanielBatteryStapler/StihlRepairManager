package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.Repair;
import edu.bsu.cs222.finalproject.database.User;
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

public class UserLookup {
    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/UserLookup.fxml"));
        Parent loadedPane = loader.load();
        UserLookup controller = loader.getController();

        {//setup the userSelector callback
            controller.userField.setCallback(user -> controller.search());
        }
        {//setup the table
            TableColumn dateCol = new TableColumn("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<UserLookupViewData, String>("Date"));
            TableColumn typeCol = new TableColumn("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<UserLookupViewData, String>("Type"));
            TableColumn modelCol = new TableColumn("Model #");
            modelCol.setCellValueFactory(new PropertyValueFactory<UserLookupViewData, String>("ModelNumber"));
            TableColumn serialCol = new TableColumn("Serial #");
            serialCol.setCellValueFactory(new PropertyValueFactory<UserLookupViewData, String>("SerialNumber"));
            controller.dataTable.getColumns().addAll(dateCol, typeCol, modelCol, serialCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<UserLookupViewData> row = new TableRow<>();
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

    @FXML UserSelector userField = null;
    @FXML Label errorLabel = null;
    @FXML TableView dataTable = null;

    @FXML
    void search() {
        User user = userField.getUser();
        if(user == null){
            errorLabel.setText("You must Select a User First");
            return;
        }
        Main main = Main.getInstance();
        ArrayList<UserLookupViewData> viewData = new ArrayList<>();

        {
            ArrayList<Purchase> rawPurchases = main.workingLayer.getPurchasesWithPurchaser(user.id);
            for (Purchase purchase : rawPurchases) {
                viewData.add(UserLookupViewData.createFromPurchase(purchase));
            }
        }
        {
            ArrayList<Repair> rawRepairs = main.workingLayer.getRepairsWithUser(user.id);
            for (Repair repair : rawRepairs) {
                viewData.add(UserLookupViewData.createFromRepair(repair));
            }
        }


        ObservableList<UserLookupViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
        dataTable.getSortOrder().add(dataTable.getColumns().get(0));//sort by the first  in the table
        TableColumn firstColumn = ((TableColumn)dataTable.getColumns().get(0));
        firstColumn.setSortType(TableColumn.SortType.DESCENDING);
        firstColumn.setSortable(true);
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

    @FXML
    void createNewRepair() throws Exception{
        Main main = Main.getInstance();
        RepairCreator repairCreator = RepairCreator.createInstance(main.stage);
        repairCreator.setCallback(repair -> {
            try {
                userField.setUser(main.workingLayer.getUserWithId(repair.userId));
            }
            catch(Exception e){
                e.printStackTrace();//print any errors that occur
            }
        });
        repairCreator.setUser(userField.getUser());
        repairCreator.show();
    }


    static public class UserLookupViewData{
        Purchase purchase;//one, and only one, of these{purchase, repair} must be null
        Repair repair;

        String date;
        String modelNumber;
        String serialNumber;

        private UserLookupViewData(){

        }

        static UserLookupViewData createFromPurchase(Purchase purchase){
            Main main = Main.getInstance();

            UserLookupViewData data = new UserLookupViewData();

            data.purchase = new Purchase(purchase);
            data.repair = null;//always set the other one to null

            data.date = purchase.date.toLocalDate().toString();
            Item item = main.workingLayer.getItemWithId(purchase.itemId);
            data.modelNumber = item.modelNumber;
            data.serialNumber = item.serialNumber;

            return data;
        }

        static UserLookupViewData createFromRepair(Repair repair){
            Main main = Main.getInstance();

            UserLookupViewData data = new UserLookupViewData();

            data.purchase = null;//always set the other one to null
            data.repair = new Repair(repair);

            data.date = repair.dateStarted.toLocalDate().toString();
            Item item = main.workingLayer.getItemWithId(repair.itemId);
            data.modelNumber = item.modelNumber;
            data.serialNumber = item.serialNumber;

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

        public String getModelNumber(){
            return modelNumber;
        }

        public String getSerialNumber(){
            return serialNumber;
        }

        void onClick(UserLookup controller, TableRow<UserLookupViewData> row){
            Main main = Main.getInstance();
            if(purchase != null) {
                try {
                    PurchaseEditor editor = PurchaseEditor.createInstance(main.stage, row.getItem().purchase);

                    editor.setCallback(purchase -> {
                        try {
                            controller.userField.setUser(main.workingLayer.getUserWithId(purchase.purchaserId));
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
                            controller.userField.setUser(main.workingLayer.getUserWithId(repair.userId));
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
