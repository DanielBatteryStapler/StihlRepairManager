package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationDialog {
    private Stage stage;
    private Runnable callback = null;

    static ConfirmationDialog createInstance(Stage rootStage) throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ConfirmationDialog.fxml"));
        Parent loadedPane = loader.load();
        ConfirmationDialog controller = loader.getController();

        controller.stage = new Stage();
        controller.stage.initOwner(rootStage);
        controller.stage.initModality(Modality.APPLICATION_MODAL);
        controller.stage.setScene(new Scene(loadedPane));

        return controller;
    }

    void setQuestion(String question) {
        questionLabel.setText(question);
    }

    void setCallback(Runnable callback){
        this.callback = callback;
    }

    void show(){
        stage.show();
    }

    @FXML Label questionLabel = null;

    @FXML
    void yes() {
        if(callback != null){
            callback.run();
        }
        stage.close();
    }

    @FXML
    void no(){
        stage.close();
    }
}
