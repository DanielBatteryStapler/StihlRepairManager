package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Login {
    @FXML TextField employeeNumberField = null;
    @FXML Label errorText = null;

    public static void showScene() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/Login.fxml"));
        Parent loadedPane = loader.load();

        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML
    void submit() throws Exception{
        Main main = Main.getInstance();

        Employee employee = main.workingLayer.getEmployeeWithNumber(employeeNumberField.getText());
        if(employee == null){
            errorText.setText("Invalid Employee Number");
        }
        else {
            main.currentEmployee = employee;
            MainSelection.showScene();
        }
    }
}
