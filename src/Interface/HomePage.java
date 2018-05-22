package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage {
    @FXML
    private Button LogOut_Button;

    @FXML
    void LogOut(ActionEvent event) {
        Parent LogIn_page = null;
        try {
            LogIn_page = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene LogIn_scene = new Scene(LogIn_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(LogIn_scene);
        app_stage.show();
    }
}
