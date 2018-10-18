package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

public class ListPurchases {
    static void showScene() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/ListPurchases.fxml"));
        Parent loadedPane = loader.load();
        //ListPurchases controller = loader.getController();

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

    @FXML
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

        dataTable.getColumns().clear();//clear the table first

        ObservableList<PurchaseViewData> observableListData = FXCollections.observableList(purchaseData);
        dataTable.setItems(observableListData);

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("Date"));
        TableColumn modelCol = new TableColumn("Model #");
        modelCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("ModelNumber"));
        TableColumn serialCol = new TableColumn("Serial #");
        serialCol.setCellValueFactory(new PropertyValueFactory<PurchaseViewData, String>("SerialNumber"));
        dataTable.getColumns().addAll(dateCol, modelCol, serialCol);
    }

    public class PurchaseViewData{
        PurchaseViewData(Purchase purchase){
            Main main = Main.getInstance();
            date = purchase.date.toLocalDate().toString();
            Item item = main.workingLayer.getItemWithId(purchase.itemId);
            modelNumber = item.modelNumber;
            serialNumber = item.serialNumber;
        }
        String date;
        String modelNumber;
        String serialNumber;
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
