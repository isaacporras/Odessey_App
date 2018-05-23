package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HomePage {
    @FXML
    private AnchorPane MainWindow_AnchorPane;
    @FXML
    private Button Upload_Button;

    @FXML
    private Button LogOut_Button;

    @FXML
    void Upload_Button_Clicked(ActionEvent event) {

        Parent GetStarted_page = null;
        try {
            GetStarted_page = FXMLLoader.load(getClass().getResource("Upload_Song.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene GetStarted_scene = new Scene(GetStarted_page);
        Stage stage = new Stage();

        stage.setScene(GetStarted_scene);
        stage.show();
    }

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
