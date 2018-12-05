package edu.bsu.cs222.finalproject.ui;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.backend.PhoneNumber;
import edu.bsu.cs222.finalproject.database.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UserViewer extends StackPane {

    public static UserViewer instance() throws Exception{
        Main main = Main.getInstance();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getClass().getResource("/fxml/UserViewer.fxml"));
        Pane rootGrid = loader.load();
        UserViewer controller = loader.getController();
        controller.getChildren().add(rootGrid);

        return controller;
    }

    @FXML Label nameData = null;
    @FXML Label phoneData = null;
    @FXML Label addressData = null;

    void setUser(User user) {
        if(user == null){
            phoneData.setText("");
            nameData.setText("");
            addressData.setText("");
        }
        else {
            if (user.id == -1) {
                //if the id is -1, that means that it was not inserted into a database, so it can't be selected
                throw new RuntimeException("Attempted to set a UserViewer to be selecting a user that isn't in a database");
            }
            try {
                phoneData.setText("Phone Number: " + PhoneNumber.toFormatted(user.phoneNumber));
            }
            catch(Exception e){//just print the error
                e.printStackTrace();
            }
            nameData.setText("Name: " + user.name);
            addressData.setText("Address: " + user.address.replaceAll("\\n", ", "));
        }
    }
}
