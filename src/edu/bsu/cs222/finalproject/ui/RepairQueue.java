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

public class RepairQueue {
    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairQueue.fxml"));
        Parent loadedPane = loader.load();
        RepairQueue controller = loader.getController();

        {//setup the table
            TableColumn<RepairQueue.RepairQueueViewData, String> numberCol = new TableColumn<>("Num");
            numberCol.setCellValueFactory(new PropertyValueFactory<>("Number"));

            TableColumn<RepairQueue.RepairQueueViewData, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));

            TableColumn<RepairQueue.RepairQueueViewData, String> modelCol = new TableColumn<>("Model #");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("ModelNumber"));

            TableColumn<RepairQueue.RepairQueueViewData, String> serialCol = new TableColumn<>("Serial #");
            serialCol.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));

            controller.dataTable.getColumns().addAll(numberCol, dateCol, modelCol, serialCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<RepairQueue.RepairQueueViewData> row = new TableRow<>();
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

    @FXML TableView<RepairQueue.RepairQueueViewData> dataTable = null;

    @FXML
    void search() {
        Main main = Main.getInstance();
        ArrayList<RepairQueue.RepairQueueViewData> viewData = new ArrayList<>();

        {
            ArrayList<Repair> rawRepairs = main.workingLayer.getInProgressRepairs();
            for (Repair repair : rawRepairs) {
                viewData.add(RepairQueue.RepairQueueViewData.createFromRepair(repair));
            }
        }


        ObservableList<RepairQueue.RepairQueueViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
        dataTable.getSortOrder().add(dataTable.getColumns().get(0));//sort by the second column in the table
        TableColumn<RepairQueue.RepairQueueViewData, ?> secondColumn = dataTable.getColumns().get(1);
        secondColumn.setSortType(TableColumn.SortType.DESCENDING);
        secondColumn.setSortable(true);
    }

    static public class RepairQueueViewData{
        Repair repair;

        String date;
        String modelNumber;
        String serialNumber;

        private RepairQueueViewData(){

        }

        static RepairQueue.RepairQueueViewData createFromRepair(Repair repair){
            Main main = Main.getInstance();

            RepairQueue.RepairQueueViewData data = new RepairQueue.RepairQueueViewData();

            data.repair = new Repair(repair);

            data.date = repair.dateStarted.toLocalDate().toString();
            Item item = main.workingLayer.getItemWithId(repair.itemId);
            data.modelNumber = item.modelNumber;
            data.serialNumber = item.serialNumber;

            return data;
        }

        public String getNumber(){
            return "" + repair.repairNumber;
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

        void onClick(RepairQueue controller, TableRow<RepairQueue.RepairQueueViewData> row){
            Main main = Main.getInstance();
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
