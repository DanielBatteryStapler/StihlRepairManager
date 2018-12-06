package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.database.User;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
        rootGrid.getChildren().remove(controller.userSelectionPane);

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

    @FXML GridPane userSelectionPane = null;

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


    @FXML TableView<User> userTable = null;

    @SuppressWarnings("unchecked") //the reason this is used is for the same reason it's used in another place in the project, refer to that comment for why this is here
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
        nameCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().name));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone Number"));
        phoneCol.setCellValueFactory(param -> new SimpleObjectProperty<>(PhoneNumber.toFormatted(param.getValue().phoneNumber)));

        TableColumn<User, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        addressCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().address.replaceAll("\\n", ", ")));

        userTable.getColumns().addAll(nameCol, phoneCol, addressCol);

        userSelectionPane.getChildren().set(0, userTable);

        presentingPane.getChildren().clear();
        presentingPane.getChildren().addAll(userSelectionPane);
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
        if (phoneField.getText().equals("") && nameField.getText().equals("")) {
            phoneField.setStyle("-fx-control-inner-background: #ff0000");
            nameField.setStyle("-fx-control-inner-background: #ff0000");
        }
        else if (!phoneField.getText().equals("") && !nameField.getText().equals("")) {
            phoneField.setStyle("-fx-control-inner-background: #ff0000");
            nameField.setStyle("-fx-control-inner-background: #ff0000");
        }
    }

    @FXML
    void selectUserByName() {
        if (userTable.getSelectionModel().getSelectedItem() == null)
            return;

        setUser(userTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void editSelectedUser() throws Exception {
        Main main = Main.getInstance();
        UserCreator userCreator = UserCreator.createInstance(main.stage, "EDIT", getUser());
        userCreator.setCallback(this::setUser);

        User temp = getUser();
        if (temp == null)
            return;

        userCreator.setName(temp.name);
        userCreator.setPhoneNumber(temp.phoneNumber);

        //this is to make sure that if there's an improperly entered address it won't throw an IndexOutOfBounds exception
        String[] fullAddress = temp.address.split("\\n");
        for (int i = 0; i < fullAddress.length; i++) {
            switch (i) {
                case 0:
                    userCreator.setAddress(fullAddress[0]);
                    break;
                case 1:
                    userCreator.setCity(fullAddress[1]);
                    break;
                case 2:
                    userCreator.setState(fullAddress[2]);
                    break;
                default:
                    break;
            }
        }

        userCreator.show();
    }

    @FXML
    void createNewUser() throws Exception{
        Main main = Main.getInstance();
        UserCreator userCreator = UserCreator.createInstance(main.stage, "CREATE", new User());
        userCreator.setCallback(this::setUser);
        userCreator.setName(nameField.getText());
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
