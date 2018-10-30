package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Purchase;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class UserCreator {
    private Stage stage;
    private Consumer<User> callback = null;

    static UserCreator createInstance(Stage rootStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/UserCreator.fxml"));
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

        if (isPhoneNumberValid(phoneField.getText()) && makePhoneNumberAllNumbers(phoneField.getText()) != null) {
            user.phoneNumber = makePhoneNumberAllNumbers(phoneField.getText());
        }
        else {
            //throw a tantrum
        }

        user.address = addressField.getText();
        Main.getInstance().workingLayer.insertUser(user);
        if(callback != null){
            callback.accept(user);
        }
        stage.close();
    }

    void setCallback(Consumer<User> callback){
        this.callback = callback;
    }

    void setPhoneNumber(String number){
        phoneField.setText(number);
    }

    void show(){
        stage.show();
    }

    private Boolean isPhoneNumberValid(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\D+", "");

        int iterator;

        for (iterator = 0;iterator < phoneNumber.length();iterator++)
            if (iterator == 10)
                return true;

        return false;
    }

    private String makePhoneNumberAllNumbers(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\D+", "");

        try {
            Integer.parseInt(phoneNumber); //double check that it's all ints
            return phoneNumber;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
