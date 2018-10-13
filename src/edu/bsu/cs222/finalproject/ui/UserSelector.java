package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.File;

public class UserSelector extends StackPane {

    public static UserSelector instance() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((new File("fxml/UserSelector.fxml")).toURI().toURL());
        Pane rootGrid = loader.load();
        UserSelector controller = loader.getController();
        controller.getChildren().add(rootGrid);

        rootGrid.getChildren().remove(controller.userDataPane);
        rootGrid.getChildren().remove(controller.newUserPane);

        return controller;
    }

    private User selectedUser = null;

    @FXML TextField phoneField = null;

    @FXML StackPane presentingPane = null;

    @FXML Node userDataPane = null;
    @FXML Label nameData = null;
    @FXML Label phoneData = null;
    @FXML Label addressData = null;

    @FXML Node newUserPane = null;

    @FXML
    void searchPhoneNumber(){
        Main main = Main.getInstance();
        User user = main.workingLayer.database.getUserWithPhoneNumber(phoneField.getText());
        if(user == null){
            selectedUser = null;

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(newUserPane);
        }
        else{
            setUser(user);
        }
    }

    @FXML
    void createNewUser() throws Exception{
        Main main = Main.getInstance();
        UserCreator userCreator = UserCreator.createInstance(main.stage);
        userCreator.setUserSelectorCallback(this);
        userCreator.setPhoneNumber(phoneField.getText());
        userCreator.show();
    }

    void setUser(User user) {
        if(user.id == -1){
            //if the id is -1, that means that it was not inserted into a database, so it can't be selected
            throw new RuntimeException("Attempted to set a UserSelector to be selecting a user that isn't in a database");
        }
        selectedUser = new User(user);
        phoneData.setText("Phone Number: " + selectedUser.phoneNumber);
        nameData.setText("Name: " + selectedUser.name);
        addressData.setText("Address: " + selectedUser.address);

        presentingPane.getChildren().clear();
        presentingPane.getChildren().add(userDataPane);
    }

    User getUser() {
        return new User(selectedUser);
    }

}
