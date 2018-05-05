package sample;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegistrarseController {
    @FXML
    private Button BackButton;

    @FXML
    void BackButton(ActionEvent event) throws IOException {
        System.out.println("SE CLICKEO BACK");
        Parent LogIn_page = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene LogIn_scene = new Scene(LogIn_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(LogIn_scene);
        app_stage.show();
    }
}
