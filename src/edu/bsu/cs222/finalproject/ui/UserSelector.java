package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Database;
import edu.bsu.cs222.finalproject.database.TemporaryDatabase;
import edu.bsu.cs222.finalproject.database.User;
import edu.bsu.cs222.finalproject.database.WorkingLayer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserSelector extends GridPane {

    private User selectedUser;
    private GridPane lastUiInsert;

    UserSelector(Stage rootWindow, WorkingLayer workingLayer){
        selectedUser = null;
        lastUiInsert = null;

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        //setPadding(new Insets(25, 25, 25, 25));


        Label titleLabel = new Label();
        titleLabel.setText("Select Customer:");
        add(titleLabel, 0, 0, 3, 1);

        Label phoneLabel = new Label();
        phoneLabel.setText("Phone Number:");
        add(phoneLabel, 0, 1, 1, 1);

        TextField phoneNumber = new TextField();
        add(phoneNumber, 1, 1, 1, 1);

        Button search = new Button();
        search.setText("Search");
        search.setOnAction((ActionEvent event) -> {
                User user = workingLayer.getUserWithPhoneNumber(phoneNumber.getText());
                if(user == null){
                    selectedUser = null;
                    GridPane gridPane = new GridPane();
                    gridPane.setAlignment(Pos.CENTER);
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    //gridPane.setPadding(new Insets(25, 25, 25, 25));

                    Label message = new Label();
                    message.setText("User Does not Exist, ");
                    gridPane.add(message, 0, 0);

                    Button newUser = new Button();
                    newUser.setText("Create One");
                    newUser.setOnAction((ActionEvent eventB) -> {
                            UserCreator userCreator = new UserCreator(workingLayer, rootWindow);
                            userCreator.setUserSelectorCallback(this);
                            userCreator.setPhoneNumber(phoneNumber.getText());
                            userCreator.show();
                        }
                    );
                    gridPane.add(newUser, 0, 1);

                    if(lastUiInsert != null) {
                        getChildren().remove(lastUiInsert);
                    }

                    add(gridPane, 0, 3, 2, 1);
                    lastUiInsert = gridPane;
                }
                else{
                    setUser(user);
                }
            }
        );
        add(search, 0, 2, 2, 1);
    }

    void setUser(User user) {
        if(user.id == -1){
            //if the id is -1, that means that it was not inserted into a database, so it can't be selected
            throw new RuntimeException("Attempted to set a UserSelector to be selecting a user that isn't in a database");
        }
        selectedUser = new User(user);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label phoneNumberData = new Label();
        phoneNumberData.setText("Phone Number: " + selectedUser.phoneNumber);
        gridPane.add(phoneNumberData, 0, 0);

        Label nameData = new Label();
        nameData.setText("Name: " + selectedUser.name);
        gridPane.add(nameData, 0, 1);

        Label addressData = new Label();
        addressData.setText("Address: " + selectedUser.address);
        gridPane.add(addressData, 0, 2);

        if(lastUiInsert != null) {
            getChildren().remove(lastUiInsert);
        }

        add(gridPane, 0, 3, 2, 1);
        lastUiInsert = gridPane;
    }

    User getUser() {
        return new User(selectedUser);
    }
}
