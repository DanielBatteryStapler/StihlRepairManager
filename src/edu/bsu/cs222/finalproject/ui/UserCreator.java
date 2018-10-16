package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;

public class UserCreator {
    private UserSelector callback = null;
    private Stage stage;

    static UserCreator createInstance(Stage rootStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((new File("fxml/UserCreator.fxml")).toURI().toURL());
        Parent loadedPane = loader.load();
        UserCreator creator = loader.getController();

        creator.stage = new Stage();
        creator.stage.initOwner(rootStage);
        creator.stage.initModality(Modality.APPLICATION_MODAL);
        creator.stage.setScene(new Scene(loadedPane));

        return creator;
    }

    @FXML TextField nameField = null;
    @FXML TextField phoneField = null;
    @FXML TextField addressField = null;

    @FXML
    void submit(){
        User user = new User();
        user.name = nameField.getText();
        user.phoneNumber = phoneField.getText();
        user.address = addressField.getText();
        Main.getInstance().workingLayer.insertUser(user);
        if(callback != null){
            callback.setUser(user);
        }
        stage.close();
    }

    void setUserSelectorCallback(UserSelector callbackSelector){
        callback = callbackSelector;
    }

    void setPhoneNumber(String number){
        phoneField.setText(number);
    }

    void show(){
        stage.show();
    }
}
