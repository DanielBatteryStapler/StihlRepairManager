package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.database.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ItemSelector extends StackPane {

    public static ItemSelector instance() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(System.class.getResource("/fxml/ItemSelector.fxml"));
        Pane rootGrid = loader.load();
        ItemSelector controller = loader.getController();
        controller.getChildren().add(rootGrid);

        rootGrid.getChildren().remove(controller.itemDataPane);
        rootGrid.getChildren().remove(controller.newItemPane);

        return controller;
    }

    private Item selectedItem = null;
    private Consumer<Item> callback = null;

    @FXML TextField serialField = null;

    @FXML StackPane presentingPane = null;

    @FXML Node itemDataPane = null;
    @FXML ItemViewer itemViewer = null;

    @FXML Node newItemPane = null;

    void setCallback(Consumer<Item> callback){
        this.callback = callback;
    }

    @FXML
    void searchSerialNumber() {
        Main main = Main.getInstance();

        ArrayList<Item> items = main.workingLayer.searchItemsWithSerial(serialField.getText());

        if(items.size() == 0){
            setItem(null);

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(newItemPane);
            return;
        }

        setItem(items.get(0));
    }

    @FXML
    void createNewItem() throws Exception{
        Main main = Main.getInstance();
        ItemCreator itemCreator = ItemCreator.createInstance(main.stage);
        itemCreator.setCallback(this::setItem);
        itemCreator.setSerialNumber(serialField.getText());
        itemCreator.show();

    }

    void setItem(Item item) {
        if(item == null) {
            selectedItem = null;
            itemViewer.setItem(null);
            presentingPane.getChildren().clear();
        }
        else{
            if (item.id == -1) {
                //if the id is -1, that means that it was not inserted into a database, so it can't be selected
                throw new RuntimeException("Attempted to set a ItemSelector to be selecting a item that isn't in a database");
            }
            selectedItem = new Item(item);
            itemViewer.setItem(item);

            presentingPane.getChildren().clear();
            presentingPane.getChildren().add(itemDataPane);
            if(callback != null) {
                callback.accept(item);
            }
        }
    }

    Item getItem() {
        if(selectedItem == null){
            return null;
        }
        return new Item(selectedItem);
    }

}
