package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.backend.Config;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.backend.WorkingLayer;
import edu.bsu.cs222.finalproject.database.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main singletonInstance = null;

    static Main getInstance(){
        return singletonInstance;
    }

    Config config = new Config();
    WorkingLayer workingLayer = new WorkingLayer();
    Stage stage = null;
    Employee currentEmployee = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception{
        config.initialize(getClass().getResource("/config.json"));

        singletonInstance = this;
        stage = _stage;

        Database database = TemporaryDatabase.createInstance();
        database.connectToServer(config.getDatabaseAddress(), config.getDatabaseUsername(), config.getDatabasePassword(), config.getDatabaseName());
        {
            User user = new User();
            user.name = "John Smith";
            user.phoneNumber = PhoneNumber.toNormalized("555-555-5555");
            user.address = "555 Fifth Avenue";
            database.insertUser(user);
        }
        {
            Employee employee = new Employee();
            employee.name = "Eugene Smithson";
            employee.number = "88";
            database.insertEmployee(employee);
        }
        workingLayer.initialize(database);

        Login.showScene();
    }
}