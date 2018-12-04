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
        if(singletonInstance == null){//if the singleton doesn't exist, that means that this is being run as a test, so just make one
            System.out.println("Manually creating a 'Main' Instance, this should only happen when running tests");
            singletonInstance = new Main();
            return singletonInstance;
        }
        return singletonInstance;
    }

    public WorkingLayer workingLayer = new WorkingLayer();
    public Stage stage = null;
    public Employee currentEmployee = null;

    public Main() {
        Config config = new Config();
        try {
            config.initialize(getClass().getResourceAsStream("/config.json"));
        } catch (Exception e) {
            System.err.println("Error when creating opening Config File as part of Main initialization");
            e.printStackTrace();
        }

        Database database;
        switch (config.getDatabaseType()) {
            case "temporary":
                database = TemporaryDatabase.createInstance();
                //because the database is by default just blank, let's throw in some example values
            break;
            case "mysql":
                database = MySqlDatabase.createInstance();
                database.connectToServer(config.getDatabaseAddress(), config.getDatabaseUsername(), config.getDatabasePassword(), config.getDatabaseName());
                break;
            case "mysqlCreateTables":
                MySqlDatabase mySqlDatabase = MySqlDatabase.createInstance();
                mySqlDatabase.connectToServer(config.getDatabaseAddress(), config.getDatabaseUsername(), config.getDatabasePassword(), config.getDatabaseName());
                mySqlDatabase.createDatabaseTables();
                database = mySqlDatabase;
                break;
            default:
                throw new RuntimeException("Error when creating database, invalid database type '" + config.getDatabaseType() + "' specified in configuration file");
        }

        workingLayer.initialize(database);

        if(config.getDatabaseType().equals("temporary")){
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

                RepairPart repairPart = new RepairPart();
                repairPart.needToBuy = true;
                repairPart.repairId = repair.id;
                repairPart.price = 6599;
                repairPart.quantity = 3;
                repairPart.name = "Some Screws";
                workingLayer.insertRepairPart(repairPart);
            }
            {
                Employee employee = new Employee();
                employee.name = "Eugene Smithson";
                employee.number = "88";
                workingLayer.insertEmployee(employee);
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception{
        if(singletonInstance != null){
            throw new RuntimeException("JavaFX attempted to create the 'Main' singleton instance, but it somehow already exists!");
        }
        singletonInstance = this;

        stage = _stage;

        Login.showScene();
    }

    @Override
    public void stop(){
        workingLayer.close();
    }
}