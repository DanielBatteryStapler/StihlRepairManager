package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

public class UserSelector extends StackPane {

    public static UserSelector instance() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/UserSelector.fxml"));
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
    void searchPhoneNumber() {
        Main main = Main.getInstance();
        if(!PhoneNumber.isValid(phoneField.getText())){
            setUser(null);

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(newUserPane);
            return;
        }
        User user = main.workingLayer.getUserWithPhoneNumber(PhoneNumber.toNormalized(phoneField.getText()));
        if(user == null){
            selectedUser = null;

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(newUserPane);
            return;
        }
        setUser(user);
    }

    @FXML
    void createNewUser() throws Exception{
        Main main = Main.getInstance();
        UserCreator userCreator = UserCreator.createInstance(main.stage);
        userCreator.setCallback(this::setUser);
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
