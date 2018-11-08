package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.backend.Print;
import edu.bsu.cs222.finalproject.database.Repair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RepairEditor {
    private Stage stage;
    private Repair repair;
    private Consumer<Repair> callback = null;

    static RepairEditor createInstance(Stage rootStage, Repair repair) throws Exception{
        Main main = Main.getInstance();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/RepairEditor.fxml"));
        Parent loadedPane = loader.load();
        RepairEditor editor = loader.getController();
        editor.repair = new Repair(repair);

        {
            editor.userViewer.setUser(main.workingLayer.getUserWithId(repair.userId));
            editor.itemViewer.setItem(main.workingLayer.getItemWithId(repair.itemId));

            editor.dateStarted.setText("Started: " + repair.dateStarted.toLocalDate().toString());

            editor.descriptionField.setText(repair.description);
            editor.descriptionCompletedField.setText(repair.descriptionCompleted);
        }
        {
            editor.stage = new Stage();
            editor.stage.initOwner(rootStage);
            editor.stage.initModality(Modality.APPLICATION_MODAL);
            editor.stage.setScene(new Scene(loadedPane));
        }

        return editor;
    }

    @FXML UserViewer userViewer = null;
    @FXML ItemViewer itemViewer = null;
    @FXML Label dateStarted = null;

    @FXML TextArea descriptionField = null;
    @FXML TextArea descriptionCompletedField = null;

    void setCallback(Consumer<Repair> callback){
        this.callback = callback;
    }

    void show(){
        stage.show();
    }

    @FXML
    void exit(){
        stage.close();
    }

    @FXML
    void updateRepair(){
        Main main = Main.getInstance();

        repair.description = descriptionField.getText();
        repair.descriptionCompleted = descriptionCompletedField.getText();

        main.workingLayer.updateRepair(repair);

        if(callback != null){
            callback.accept(repair);
        }
        stage.close();
    }

    @FXML
    void deleteRepair(){
        Main main = Main.getInstance();

        main.workingLayer.deleteRepair(repair);

        if(callback != null){
            callback.accept(null);
        }
        stage.close();
    }
}
