package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class UserCreator {
    private Stage stage;
    private Consumer<User> callback = null;
    private static String MODE = "";
    private static User USER = null;

    static UserCreator createInstance(Stage rootStage, String mode, User user) throws Exception {
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/UserCreator.fxml"));
        Parent loadedPane = loader.load();
        UserCreator creator = loader.getController();

        MODE = mode;
        USER = user;

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

    @FXML Label headerText = new Label();

    @FXML
    void submit() {
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

        USER.phoneNumber = PhoneNumber.toNormalized(phoneField.getText());
        USER.name = nameField.getText();
        USER.address = addressField.getText() + "\n" + cityField.getText() + "\n" + stateField.getText();

        if (MODE.equals("CREATE")) {
            Main.getInstance().workingLayer.insertUser(USER);
        }
        else {
            Main.getInstance().workingLayer.updateUser(USER);
        }

        if (callback != null) {
            callback.accept(USER);
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

    void setAddress(String address) {
        addressField.setText(address);
    }

    void setCity(String city) {
        cityField.setText(city);
    }

    void setState(String state) {
        stateField.setText(state);
    }

    void show() {
        if (MODE.equals("CREATE"))
            headerText.setText("Create New Customer");
        else
            headerText.setText("Edit Customer");

        stage.show();
    }
}


