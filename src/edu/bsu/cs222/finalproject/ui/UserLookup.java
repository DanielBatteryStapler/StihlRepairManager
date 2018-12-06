package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class UserLookup {
    @SuppressWarnings("unchecked") //Needed for "controller.dataTable.getColumns().addAll(...)" because it should be able to figure out type safety but decides that it can't
    //For a better explanation: https://stackoverflow.com/questions/1445233/is-it-possible-to-solve-the-a-generic-array-of-t-is-created-for-a-varargs-param
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
            TableColumn<UserLookupViewData, String> modelCol = new TableColumn<>("Model #");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("ModelNumber"));
            modelCol.setMinWidth(150);

            TableColumn<UserLookupViewData, String> serialCol = new TableColumn<>("Serial #");
            serialCol.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));

            controller.dataTable.getColumns().addAll(modelCol, serialCol);

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
    @FXML TableView<UserLookupViewData> dataTable = null;

    @FXML
    private void search() {
        User user = userField.getUser();
        if(user == null){
            userField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        Main main = Main.getInstance();
        ArrayList<UserLookupViewData> viewData = new ArrayList<>();

        {
            ArrayList<Purchase> rawPurchases = main.workingLayer.getPurchasesWithPurchaser(user.id);
            for (Purchase purchase : rawPurchases) {
                boolean isDuplicate = false;
                for (UserLookupViewData data : viewData) {
                    if (purchase.itemId == data.item.id) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    viewData.add(UserLookupViewData.createFromItem(main.workingLayer.getItemWithId(purchase.itemId)));
                }
            }
        }
        {
            ArrayList<Repair> rawRepairs = main.workingLayer.getRepairsWithUser(user.id);
            for (Repair repair : rawRepairs) {
                boolean isDuplicate = false;
                for (UserLookupViewData data : viewData) {
                    if (repair.itemId == data.item.id) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    viewData.add(UserLookupViewData.createFromItem(main.workingLayer.getItemWithId(repair.itemId)));
                }
            }
        }


        ObservableList<UserLookupViewData> observableListData = FXCollections.observableList(viewData);
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
        Item item;

        String modelNumber;
        String serialNumber;

        private UserLookupViewData(){

        }

        static UserLookupViewData createFromItem(Item item){
            UserLookupViewData data = new UserLookupViewData();

            data.item = new Item(item);

            data.modelNumber = item.modelNumber;
            data.serialNumber = item.serialNumber;

            return data;
        }

        public String getModelNumber(){
            return modelNumber;
        }

        public String getSerialNumber(){
            return serialNumber;
        }

        void onClick(UserLookup controller, TableRow<UserLookupViewData> row){
            Main main = Main.getInstance();

            try {
                ItemEditor editor = ItemEditor.createInstance(main.stage, row.getItem().item);
                editor.setCallback(item -> controller.search());
                editor.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
