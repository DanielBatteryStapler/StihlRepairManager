package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Database;
import edu.bsu.cs222.finalproject.database.TemporaryDatabase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.File;

public class ConnectToDatabase {
    static void showScene() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((new File("fxml/ConnectToDatabase.fxml")).toURI().toURL());
        Parent loadedPane = loader.load();
        //ConnectToDatabase controller = loader.getController();

        Main main = Main.getInstance();
        Scene scene = new Scene(loadedPane);
        main.stage.setScene(scene);
        main.stage.show();
    }

    @FXML TextField addressField = null;
    @FXML TextField usernameField = null;
    @FXML TextField passwordField = null;

    @FXML
    void connectToDatabase() throws Exception{
        Database database = TemporaryDatabase.createInstance();
        database.connectToServer(addressField.getText(), usernameField.getText(), passwordField.getText());
        {
            User user = new User();
            user.name = "John Smith";
            user.phoneNumber = "555-5555";
            user.address = "555 Fifth Avenue";
            database.insertUser(user);
        }
        Main main = Main.getInstance();
        main.workingLayer.initialize(database);
        //now that the database is connected, show the real ui
        MainSelection.showScene();
    }
}
