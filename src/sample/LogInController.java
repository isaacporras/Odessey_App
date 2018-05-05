package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class LogInController {
    @FXML
    private Button LogInButton;

    @FXML
    private Button getStartedButton;

    @FXML
    void LogInButton(ActionEvent event) throws IOException{
        System.out.println("SE CLICKEO");
        Parent home_page = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Scene home_page_scene = new Scene(home_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();

    }
    @FXML
    void getStartedButton(ActionEvent event) throws IOException{
        System.out.println("GET STARTED");
        Parent GetStarted_page = FXMLLoader.load(getClass().getResource("Registrarse_Window.fxml"));
        Scene GetStarted_scene = new Scene(GetStarted_page);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(GetStarted_scene);
        app_stage.show();
    }

}
