package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Database;
import edu.bsu.cs222.finalproject.database.TemporaryDatabase;
import edu.bsu.cs222.finalproject.database.User;
import edu.bsu.cs222.finalproject.database.WorkingLayer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    WorkingLayer workingLayer = new WorkingLayer();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {

        setupConnectToDatabaseSelectionScene(stage);
        stage.show();
    }

    void setupConnectToDatabaseSelectionScene(Stage stage) {
        GridPane gridRoot = new GridPane();
        gridRoot.setAlignment(Pos.CENTER);
        gridRoot.setHgap(10);
        gridRoot.setVgap(10);
        gridRoot.setPadding(new Insets(25, 25, 25, 25));

        Label pageLabel = new Label("Connect to Database");
        gridRoot.add(pageLabel, 0, 0, 2, 1);

        Label addressLabel = new Label("Address:");
        gridRoot.add(addressLabel, 0, 1, 1, 1);
        TextField address = new TextField();
        gridRoot.add(address, 1, 1, 1, 1);

        Label usernameLabel = new Label("Username:");
        gridRoot.add(usernameLabel, 0, 2, 1, 1);
        TextField username = new TextField();
        gridRoot.add(username, 1, 2, 1, 1);

        Label passwordLabel = new Label("Password:");
        gridRoot.add(passwordLabel, 0, 3, 1, 1);
        TextField password = new TextField();
        gridRoot.add(password, 1, 3, 1, 1);

        Button connectButton = new Button();
        connectButton.setText("Connect");
        connectButton.setOnAction((ActionEvent event) -> {
                    Database database = TemporaryDatabase.createInstance();
                    database.connectToServer(address.getText(), username.getText(), password.getText());
                    {
                        User user = new User();
                        user.name = "John Smith";
                        user.phoneNumber = "555-5555";
                        user.address = "555 Fifth Avenue";
                        database.insertUser(user);
                    }
                    workingLayer.initialize(database);
                    //now that the database is connected, show the real ui
                    setupMainSelectionScene(stage);
                }
        );
        gridRoot.add(connectButton, 0, 4, 2, 1);

        stage.setScene(new Scene(gridRoot));
    }

    void setupMainSelectionScene(Stage stage) {

        GridPane gridRoot = new GridPane();
        gridRoot.setAlignment(Pos.CENTER);
        gridRoot.setHgap(10);
        gridRoot.setVgap(10);
        gridRoot.setPadding(new Insets(25, 25, 25, 25));

        Label pageLabel = new Label("Select an Action");
        gridRoot.add(pageLabel, 0, 0);

        Button addPurchase = new Button();
        addPurchase.setText("Add Purchase");
        addPurchase.setOnAction((ActionEvent event) -> {
                    setupAddPurchaseScene(stage);
                }
        );
        gridRoot.add(addPurchase, 0, 1);

        stage.setScene(new Scene(gridRoot));
    }

    void setupAddPurchaseScene(Stage stage){
        GridPane gridRoot = new GridPane();
        gridRoot.setAlignment(Pos.CENTER);
        gridRoot.setHgap(10);
        gridRoot.setVgap(10);
        gridRoot.setPadding(new Insets(25, 25, 25, 25));

        Label pageLabel = new Label("Add Purchase");
        gridRoot.add(pageLabel, 0, 0);

        Button back = new Button();
        back.setText("Back to Selection");
        back.setOnAction((ActionEvent event) -> {
                    setupMainSelectionScene(stage);
                }
        );
        gridRoot.add(back, 0, 1);

        UserSelector userSelector = new UserSelector(stage, workingLayer);
        gridRoot.add(userSelector, 0, 2);



        stage.setScene(new Scene(gridRoot));
    }
}