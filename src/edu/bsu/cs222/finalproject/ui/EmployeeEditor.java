package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class EmployeeEditor {

    static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/EmployeeEditor.fxml"));
        Parent loadedPane = loader.load();
        EmployeeEditor controller = loader.getController();

        {//setup the table
            TableColumn<EmployeeViewData, String> modelCol = new TableColumn<>("Employee Name");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

            TableColumn<EmployeeViewData, String> serialCol = new TableColumn<>("Number");
            serialCol.setCellValueFactory(new PropertyValueFactory<>("Number"));

            controller.dataTable.getColumns().addAll(modelCol, serialCol);

            controller.dataTable.setRowFactory(table -> {
                TableRow<EmployeeViewData> row = new TableRow<>();
                row.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getClickCount() == 2 && !row.isEmpty()){
                        row.getItem().onClick(controller, row);
                    }
                });
                return row;
            });
            controller.search();
        }

        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    void backToSelection() throws Exception{
        MainSelection.showScene();
    }

    @FXML Label errorLabel = null;
    @FXML TableView<EmployeeViewData> dataTable = null;

    @FXML TextField employeeNameField = null;
    @FXML TextField employeeNumberField = null;


    @FXML
    private void search() {
        Main main = Main.getInstance();
        ArrayList<EmployeeViewData> viewData = new ArrayList<>();

        {
            ArrayList<Employee> employeeData = main.workingLayer.getAllEmployees();
            for(Employee i : employeeData){
                viewData.add(EmployeeViewData.createFromEmployee(i));
            }
        }

        ObservableList<EmployeeViewData> observableListData = FXCollections.observableList(viewData);
        dataTable.setItems(observableListData);
    }

    @FXML
    public void newEmployee() {
        Employee employee = new Employee();
        employee.name = employeeNameField.getText();
        employee.number = employeeNumberField.getText();

        Main main = Main.getInstance();
        main.workingLayer.insertEmployee(employee);

        employeeNameField.clear();
        employeeNumberField.clear();

        search();
    }

    @FXML
    public void removeEmployee() {
        Main main = Main.getInstance();

        if (dataTable == null) {
            errorLabel.setText("Select an employee before trying to remove");
            return;
        }

        EmployeeViewData employeeViewData = dataTable.getSelectionModel().getSelectedItem();

        if(employeeViewData.employee.id == main.currentEmployee.id){
            errorLabel.setText("You cannot remove the employee that you are currently logged in as!");
            employeeNameField.setStyle("-fx-control-inner-background: #ff0000");
        }
        else{
            try {
                ConfirmationDialog confirm = ConfirmationDialog.createInstance(main.stage);
                confirm.setQuestion("Are you sure you want to delete this Employee?\nYou can NOT undo this action.");
                confirm.setCallback(() -> {
                    main.workingLayer.deleteEmployee(employeeViewData.employee);
                    search();
                });
                confirm.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static public class EmployeeViewData{
        Employee employee;

        String name;
        String number;

        private EmployeeViewData(){

        }

        static EmployeeViewData createFromEmployee(Employee employee){
            EmployeeViewData data = new EmployeeViewData();

            data.employee = new Employee(employee);

            data.name = employee.name;
            data.number = employee.number;

            return data;
        }

        public String getName(){
            return name;
        }

        public String getNumber(){
            return number;
        }

        void onClick(EmployeeEditor controller, TableRow<EmployeeViewData> row){
            Main main = Main.getInstance();

            if(row.getItem().employee.id == main.currentEmployee.id){
                controller.errorLabel.setText("You cannot remove the employee that you are currently logged in as!");
                controller.employeeNameField.setStyle("-fx-control-inner-background: #ff0000");
            }
            else{
                try {
                    ConfirmationDialog confirm = ConfirmationDialog.createInstance(main.stage);
                    confirm.setQuestion("Are you sure you want to delete this Employee?\nYou can NOT undo this action.");
                    confirm.setCallback(() -> {
                        main.workingLayer.deleteEmployee(row.getItem().employee);
                        controller.search();
                    });
                    confirm.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
