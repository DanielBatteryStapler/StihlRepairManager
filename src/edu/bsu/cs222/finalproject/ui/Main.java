package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Database;
import edu.bsu.cs222.finalproject.database.TemporaryDatabase;
import edu.bsu.cs222.finalproject.database.User;
import edu.bsu.cs222.finalproject.database.WorkingLayer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    WorkingLayer workingLayer = new WorkingLayer();
    Stage stage = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception{
        stage = _stage;

        ConnectToDatabase.showScene(this);
    }

    void setupMainSelectionScene() {

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
                    setupAddPurchaseScene();
                }
        );
        gridRoot.add(addPurchase, 0, 1);

        stage.setScene(new Scene(gridRoot));
    }

    void setupAddPurchaseScene(){
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
                    setupMainSelectionScene();
                }
        );
        gridRoot.add(back, 0, 1);

        UserSelector userSelector = new UserSelector(stage, workingLayer);
        gridRoot.add(userSelector, 0, 2);



        stage.setScene(new Scene(gridRoot));
    }
}