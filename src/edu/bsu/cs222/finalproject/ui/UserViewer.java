package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UserViewer extends StackPane {

    public static UserViewer instance() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/UserViewer.fxml"));
        Pane rootGrid = loader.load();
        UserViewer controller = loader.getController();
        controller.getChildren().add(rootGrid);

        return controller;
    }

    private User selectedUser = null;

    @FXML Label nameData = null;
    @FXML Label phoneData = null;
    @FXML Label addressData = null;

    void setUser(User user) {
        if(user.id == -1){
            //if the id is -1, that means that it was not inserted into a database, so it can't be selected
            throw new RuntimeException("Attempted to set a UserViewer to be selecting a user that isn't in a database");
        }
        selectedUser = new User(user);
        phoneData.setText("Phone Number: " + selectedUser.phoneNumber);
        nameData.setText("Name: " + selectedUser.name);
        addressData.setText("Address: " + selectedUser.address);
    }
}
