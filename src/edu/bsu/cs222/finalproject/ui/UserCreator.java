package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
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

    static UserCreator createInstance(Stage rootStage) throws Exception {
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/UserCreator.fxml"));
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
    @FXML TextField stateField = null;
    @FXML TextField cityField = null;

    @FXML
    void submit() {
        User user = new User();

        //reset boxes to white so it's not persistent
        nameField.setStyle("-fx-control-inner-background: #ffffff");
        phoneField.setStyle("-fx-control-inner-background: #ffffff");
        addressField.setStyle("-fx-control-inner-background: #ffffff");

        if (nameField.getText().equals("")) {
            nameField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        else if (addressField.getText().equals("")) {
            addressField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        else if (stateField.getText().equals("")) {
            stateField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        else if (cityField.getText().equals("")) {
            cityField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }
        else if (!PhoneNumber.isValid(phoneField.getText())) {
            phoneField.setStyle("-fx-control-inner-background: #ff0000");
            return;
        }

        user.phoneNumber = PhoneNumber.toNormalized(phoneField.getText());
        user.name = nameField.getText();
        user.address = addressField.getText() + "\n" + cityField.getText() + "\n" + stateField.getText();

        Main.getInstance().workingLayer.insertUser(user);
        if (callback != null) {
            callback.accept(user);
        }
        stage.close();
    }

    void setCallback(Consumer<User> callback) {
        this.callback = callback;
    }

    void setPhoneNumber(String number) {
        phoneField.setText(number);
    }

    void setName(String name) {
        nameField.setText(name);
    }

    void show() {
        stage.show();
    }
}


