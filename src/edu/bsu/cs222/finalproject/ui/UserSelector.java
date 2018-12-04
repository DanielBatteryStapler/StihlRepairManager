package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.database.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.util.ArrayList;
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
        rootGrid.getChildren().remove(controller.userTable);

        return controller;
    }

    private User selectedUser = null;
    private Consumer<User> callback = null;

    @FXML TextField phoneField = null;
    @FXML TextField nameField = null;

    @FXML StackPane presentingPane = null;

    @FXML Node userDataPane = null;
    @FXML UserViewer userViewer = null;

    @FXML Node newUserPane = null;

    void setCallback(Consumer<User> callback){
        this.callback = callback;
    }

    @FXML
    private void searchPhoneNumber() {
        Main main = Main.getInstance();

        if (!PhoneNumber.isValid(phoneField.getText())) {
            phoneField.setStyle("-fx-control-inner-background: #ff0000");
            setUser(null);
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


    @FXML TableView userTable = null;

    @FXML
    private void searchName() {
        Main main = Main.getInstance();

        ArrayList<User> users = main.workingLayer.getUsersWithName(nameField.getText());
        if(users.isEmpty()){
            selectedUser = null;

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(newUserPane);
            return;
        }

        ObservableList<User> usersObservableList = FXCollections.observableList(users);
        userTable = new TableView<>(usersObservableList);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleObjectProperty<>(param.getValue().name);
            }
        });

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone Number"));
        phoneCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleObjectProperty<>(PhoneNumber.toFormatted(param.getValue().phoneNumber));
            }
        });

        TableColumn<User, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        addressCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleObjectProperty<>(param.getValue().address);
            }
        });

        userTable.getColumns().addAll(nameCol, phoneCol, addressCol);

        presentingPane.getChildren().add(userTable);
    }

    @FXML
    void search() {
        //reset styles to default
        phoneField.setStyle("-fx-control-inner-background: #ffffff");
        nameField.setStyle("-fx-control-inner-background: #ffffff");

        if (nameField.getText().equals("")) {
            try {
                PhoneNumber.toNormalized(phoneField.getText());
                searchPhoneNumber();
            } catch (Exception e) {
                phoneField.setStyle("-fx-control-inner-background: #ff0000");
            }
        }
        else if (phoneField.getText().equals("")) {
            searchName();
        }
        else if (phoneField.getText().equals("") && nameField.getText().equals("")) {
            phoneField.setStyle("-fx-control-inner-background: #ff0000");
            nameField.setStyle("-fx-control-inner-background: #ff0000");
        }
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
