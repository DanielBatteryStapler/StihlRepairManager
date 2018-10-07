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
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.soap.Text;

public class UserCreator {
    UserSelector callback;
    Stage stage;

    TextField phoneNumber;

    public UserCreator(WorkingLayer workingLayer, Stage rootStage) {
        callback = null;

        stage = new Stage();
        stage.initOwner(rootStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label pageLabel = new Label("Add New User");
        grid.add(pageLabel, 0, 0, 2, 1);

        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, 1, 1, 1);
        TextField name = new TextField();
        grid.add(name, 1, 1, 1, 1);

        Label phoneNumberLabel = new Label("Phone Number:");
        grid.add(phoneNumberLabel, 0, 2, 1, 1);
        phoneNumber = new TextField();
        grid.add(phoneNumber, 1, 2, 1, 1);

        Label addressLabel = new Label("Address:");
        grid.add(addressLabel, 0, 3, 1, 1);
        TextField address = new TextField();
        grid.add(address, 1, 3, 1, 1);

        Button createButton = new Button();
        createButton.setText("Submit");
        createButton.setOnAction((ActionEvent event) -> {
                User user = new User();
                user.name = name.getText();
                user.phoneNumber = phoneNumber.getText();
                user.address = address.getText();
                workingLayer.addNewUser(user);
                if(callback != null){
                    callback.setUser(user);
                }
                stage.close();
            }
        );
        grid.add(createButton, 0, 4, 2, 1);

        stage.setScene(new Scene(grid));
    }

    public void setUserSelectorCallback(UserSelector callbackSelector){
        callback = callbackSelector;
    }

    public void setPhoneNumber(String number){
        phoneNumber.setText(number);
    }

    public void show(){
        stage.show();
    }
}
