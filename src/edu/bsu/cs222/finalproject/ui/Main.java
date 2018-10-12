package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.WorkingLayer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main singletonInstance = null;

    public static Main getInstance(){
        return singletonInstance;
    }

    WorkingLayer workingLayer = new WorkingLayer();
    Stage stage = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage _stage) throws Exception{
        singletonInstance = this;
        stage = _stage;

        ConnectToDatabase.showScene();
    }
}