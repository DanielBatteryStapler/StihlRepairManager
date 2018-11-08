package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ItemViewer extends StackPane {

    public static ItemViewer instance() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/ItemViewer.fxml"));
        Pane rootGrid = loader.load();
        ItemViewer controller = loader.getController();
        controller.getChildren().add(rootGrid);

        return controller;
    }

    @FXML Label modelData = null;
    @FXML Label serialData = null;

    void setItem(Item item) {
        if(item == null){
            modelData.setText("");
            serialData.setText("");
        }
        else {
            if (item.id == -1) {
                //if the id is -1, that means that it was not inserted into a database, so it can't be selected
                throw new RuntimeException("Attempted to set a ItemViewer to be selecting a item that isn't in a database");
            }
            modelData.setText("Model #: " + item.modelNumber);
            serialData.setText("Serial #: " + item.serialNumber);
        }
    }
}
