package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

public class UserSelector extends StackPane {

    public static UserSelector instance() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/UserSelector.fxml"));
        Pane rootGrid = loader.load();
        UserSelector controller = loader.getController();
        controller.getChildren().add(rootGrid);

        rootGrid.getChildren().remove(controller.userDataPane);
        rootGrid.getChildren().remove(controller.newUserPane);

        return controller;
    }

    private User selectedUser = null;
    private Consumer<User> callback = null;

    @FXML TextField phoneField = null;

    @FXML StackPane presentingPane = null;

    @FXML Node userDataPane = null;
    @FXML UserViewer userViewer = null;

    @FXML Node newUserPane = null;

    void setCallback(Consumer<User> callback){
        this.callback = callback;
    }

    @FXML
    void searchPhoneNumber() throws Exception{
        Main main = Main.getInstance();
        User user = main.workingLayer.getUserWithPhoneNumber(phoneField.getText());
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
        userCreator.setCallback(user -> setUser(user));
        userCreator.setPhoneNumber(phoneField.getText());
        userCreator.show();

    }

    void setUser(User user) {
        if(user == null) {
            selectedUser = null;
            userViewer.setUser(null);
            presentingPane.getChildren().clear();
        }
        else{
            if (user.id == -1) {
                //if the id is -1, that means that it was not inserted into a database, so it can't be selected
                throw new RuntimeException("Attempted to set a UserSelector to be selecting a user that isn't in a database");
            }
            selectedUser = new User(user);
            userViewer.setUser(user);

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(userDataPane);
            if(callback != null) {
                callback.accept(user);
            }
        }
    }

    User getUser() {
        if(selectedUser == null){
            return null;
        }
        return new User(selectedUser);
    }

}
