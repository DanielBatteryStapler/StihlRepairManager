package edu.bsu.cs222.finalproject;

import edu.bsu.cs222.finalproject.backend.Config;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.backend.WorkingLayer;
import edu.bsu.cs222.finalproject.database.*;
import edu.bsu.cs222.finalproject.ui.Login;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main singletonInstance = null;

    public static Main getInstance(){
        return singletonInstance;
    }

    Config config = new Config();
    public WorkingLayer workingLayer = new WorkingLayer();
    public Stage stage = null;
    public Employee currentEmployee = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception{
        config.initialize(getClass().getResourceAsStream("/config.json"));

        singletonInstance = this;
        stage = _stage;

        Database database = TemporaryDatabase.createInstance();
        database.connectToServer(config.getDatabaseAddress(), config.getDatabaseUsername(), config.getDatabasePassword(), config.getDatabaseName());

        workingLayer.initialize(database);

        User user = new User();
        {
            user.name = "John Smith";
            user.phoneNumber = PhoneNumber.toNormalized("555-555-5555");
            user.address = "555 Fifth Avenue";
            workingLayer.insertUser(user);
        }
        Item item = new Item();
        {
            item.serialNumber = "#5555";
            item.modelNumber = "#5555";
            workingLayer.insertItem(item);
            workingLayer.makeNewPurchase(user, item);
        }
        {
            Repair repair = workingLayer.makeNewRepair(user, item);
            repair.description = "description of needed repairs....";
            workingLayer.updateRepair(repair);
        }
        {
            Employee employee = new Employee();
            employee.name = "Eugene Smithson";
            employee.number = "88";
            workingLayer.insertEmployee(employee);
        }

        Login.showScene();
    }
}